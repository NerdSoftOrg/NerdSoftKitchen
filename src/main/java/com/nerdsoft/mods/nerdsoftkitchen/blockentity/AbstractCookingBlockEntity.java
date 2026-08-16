package com.nerdsoft.mods.nerdsoftkitchen.blockentity;

import com.nerdsoft.mods.nerdsoftkitchen.registry.data.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;

public abstract class AbstractCookingBlockEntity extends BlockEntity implements Container {

    private static final String ITEMS_KEY = "Items";
    private static final String PROGRESS_KEY = "CookProgress";
    private static final String TIME_KEY = "CookTime";
    private static final double STILL_VALID_RADIUS_SQ = 64.0;
    private static final RandomSource SEED_SOURCE = RandomSource.create();
    protected final NonNullList<ItemStack> items;
    protected final int[] cookProgress;
    protected final int[] cookTime;
    protected final long[] slotSeeds;
    final ItemStack[] cachedOutput;
    private final int totalSlots;
    int cookingSlotCount;
    int nonEmptySlotCount;

    protected AbstractCookingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int totalSlots) {
        super(type, pos, state);
        this.totalSlots = totalSlots;
        this.items = NonNullList.withSize(totalSlots, ItemStack.EMPTY);
        this.cookProgress = new int[totalSlots];
        this.cookTime = new int[totalSlots];
        this.slotSeeds = new long[totalSlots];
        this.cachedOutput = new ItemStack[totalSlots];
        for (int i = 0; i < totalSlots; i++) {
            this.slotSeeds[i] = SEED_SOURCE.nextLong();
        }
    }

    public static void genericTick(Level level, BlockPos pos, BlockState state, AbstractCookingBlockEntity entity) {
        if (entity.cookingSlotCount == 0 || !entity.isBlockActive(level, state)) {
            return;
        }

        ItemStack[] cachedOutput = entity.cachedOutput;
        int[] cookProgress = entity.cookProgress;
        int[] cookTime = entity.cookTime;
        int totalSlots = entity.totalSlots;
        boolean dirty = false;

        for (int slot = 0; slot < totalSlots; slot++) {
            ItemStack output = cachedOutput[slot];
            if (output == null || !entity.isSlotActive(slot)) {
                continue;
            }

            cookProgress[slot]++;
            float progress = Mth.clamp((float) cookProgress[slot] / cookTime[slot], 0.0f, 1.0f);
            entity.items.get(slot).set(ModDataComponents.COOK_PROGRESS.get(), progress);

            if (cookProgress[slot] >= cookTime[slot]) {
                entity.items.set(slot, ItemStack.EMPTY);
                cookProgress[slot] = 0;
                cachedOutput[slot] = null;
                entity.cookingSlotCount--;
                entity.nonEmptySlotCount--;
                dirty = true;
                entity.onCookComplete(level, pos, slot, output);
            }
        }

        if (dirty) {
            entity.markUpdated();
        }
    }

    /**
     * {@code setChanged()} + a full block-update broadcast. Centralizes the
     * {@code sendBlockUpdated(pos, state, state, 3)} pattern that was previously duplicated in
     * {@code genericTick} and in every subclass's own mutation methods (placing food, etc.).
     * Requires {@link #level} to be non-null; callers that might run before the block entity is
     * attached to a level (e.g. NBT deserialization) should not call this.
     */
    protected final void markUpdated() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @SuppressWarnings("unused")
    protected boolean isSlotActive(int slot) {
        return true;
    }

    protected abstract boolean isBlockActive(Level level, BlockState state);

    protected abstract CookResult resolveRecipe(Level level, int slot, ItemStack stack);

    protected void onCookComplete(Level level, BlockPos pos, int slot, ItemStack result) {
    }

    protected void onSlotSeedAssigned(int slot) {
    }

    protected void onSlotsLoaded() {
    }

    protected final void refreshSlotRecipe(int slot) {
        ItemStack stack = items.get(slot);
        boolean wasCooking = cachedOutput[slot] != null;

        if (stack.isEmpty() || level == null) {
            clearSlotCooking(slot, wasCooking);
            return;
        }

        CookResult result = resolveRecipe(level, slot, stack);
        if (result == null) {
            clearSlotCooking(slot, wasCooking);
            return;
        }

        cachedOutput[slot] = result.output();
        cookTime[slot] = result.cookTime();
        if (!wasCooking) {
            cookingSlotCount++;
        }
    }

    private void clearSlotCooking(int slot, boolean wasCooking) {
        if (wasCooking) {
            cachedOutput[slot] = null;
            cookingSlotCount--;
        }
        cookProgress[slot] = 0;
    }

    protected final void refreshAllSlotRecipes() {
        for (int slot = 0; slot < totalSlots; slot++) {
            refreshSlotRecipe(slot);
        }
    }

    protected final void refreshOccupancyCount() {
        int count = 0;
        for (int i = 0; i < totalSlots; i++) {
            if (!items.get(i).isEmpty()) {
                count++;
            }
        }
        nonEmptySlotCount = count;
    }

    @Override
    public int getContainerSize() {
        return totalSlots;
    }

    @Override
    public boolean isEmpty() {
        return nonEmptySlotCount == 0;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) {
            clearSlot(slot);
            setChanged();
        }
        return result;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack result = ContainerHelper.takeItem(items, slot);
        if (!result.isEmpty()) {
            clearSlot(slot);
        }
        return result;
    }

    private void clearSlot(int slot) {
        cookProgress[slot] = 0;
        if (cachedOutput[slot] != null) {
            cachedOutput[slot] = null;
            cookingSlotCount--;
        }
        nonEmptySlotCount--;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        boolean wasEmpty = items.get(slot).isEmpty();
        items.set(slot, stack);
        cookProgress[slot] = 0;
        if (wasEmpty && !stack.isEmpty()) {
            nonEmptySlotCount++;
            this.slotSeeds[slot] = SEED_SOURCE.nextLong();
            onSlotSeedAssigned(slot);
        } else if (!wasEmpty && stack.isEmpty()) {
            nonEmptySlotCount--;
        }
        refreshSlotRecipe(slot);
        setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        if (this.level == null || this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
                this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5
        ) <= STILL_VALID_RADIUS_SQ;
    }

    @Override
    public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
        return getItem(slot).isEmpty();
    }

    @Override
    public void clearContent() {
        Collections.fill(items, ItemStack.EMPTY);
        Arrays.fill(cookProgress, 0);
        Arrays.fill(cookTime, 0);
        Arrays.fill(cachedOutput, null);
        cookingSlotCount = 0;
        nonEmptySlotCount = 0;
        setChanged();
    }

    public int getCookProgress(int slot) {
        return cookProgress[slot];
    }

    public int getCookTime(int slot) {
        return cookTime[slot];
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public void dropContents(Level level, BlockPos pos) {
        NonNullList<ItemStack> merged = mergeStacksForDrop(items);
        Containers.dropContents(level, pos, merged);
        items.clear();
        Arrays.fill(cachedOutput, null);
        cookingSlotCount = 0;
        nonEmptySlotCount = 0;
    }

    private static NonNullList<ItemStack> mergeStacksForDrop(NonNullList<ItemStack> source) {
        NonNullList<ItemStack> merged = NonNullList.create();
        for (ItemStack stack : source) {
            if (stack.isEmpty()) {
                continue;
            }
            ItemStack clean = stack.copy();
            clean.remove(ModDataComponents.COOK_PROGRESS.get());

            boolean addedToExisting = false;
            for (ItemStack existing : merged) {
                if (ItemStack.isSameItemSameComponents(existing, clean)
                        && existing.getCount() + clean.getCount() <= existing.getMaxStackSize()) {
                    existing.grow(clean.getCount());
                    addedToExisting = true;
                    break;
                }
            }
            if (!addedToExisting) {
                merged.add(clean);
            }
        }
        return merged;
    }

    private void readTagInto(CompoundTag tag, HolderLookup.Provider registries, boolean reseedOnMismatch) {
        Collections.fill(items, ItemStack.EMPTY);

        if (tag.contains(ITEMS_KEY, 10)) {
            ContainerHelper.loadAllItems(tag.getCompound(ITEMS_KEY), items, registries);
        }
        int[] progress = tag.getIntArray(PROGRESS_KEY);
        int[] times = tag.getIntArray(TIME_KEY);
        if (progress.length == totalSlots) {
            System.arraycopy(progress, 0, cookProgress, 0, totalSlots);
        }
        if (times.length == totalSlots) {
            System.arraycopy(times, 0, cookTime, 0, totalSlots);
        }
        long[] loadedSeeds = tag.getLongArray("SlotSeeds");
        if (loadedSeeds.length == totalSlots) {
            System.arraycopy(loadedSeeds, 0, slotSeeds, 0, totalSlots);
        } else if (reseedOnMismatch) {
            for (int i = 0; i < totalSlots; i++) {
                this.slotSeeds[i] = SEED_SOURCE.nextLong();
            }
        }

        refreshOccupancyCount();
        onSlotsLoaded();
    }

    private CompoundTag writeTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        tag.put(ITEMS_KEY, ContainerHelper.saveAllItems(new CompoundTag(), items, registries));
        tag.putIntArray(PROGRESS_KEY, cookProgress);
        tag.putIntArray(TIME_KEY, cookTime);
        tag.putLongArray("SlotSeeds", slotSeeds);
        return tag;
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        readTagInto(tag, registries, true);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        refreshAllSlotRecipes();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.merge(writeTag(registries));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return writeTag(registries);
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.handleUpdateTag(tag, registries);
        readTagInto(tag, registries, false);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public record CookResult(ItemStack output, int cookTime) {
    }
}