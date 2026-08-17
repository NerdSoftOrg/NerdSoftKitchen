package com.nerdsoft.mods.nerdsoftkitchen.blockentity;

import com.nerdsoft.mods.nerdsoftkitchen.block.SkilletBlock;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cook.CookRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cook.CookRecipeInput;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.mix.MixRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.mix.MixRecipeInput;
import com.nerdsoft.mods.nerdsoftkitchen.registry.blockentity.ModBlockEntities;
import com.nerdsoft.mods.nerdsoftkitchen.registry.recipe.ModRecipeTypes;
import com.nerdsoft.mods.nerdsoftkitchen.registry.sound.ModSounds;
import com.nerdsoft.mods.nerdsoftkitchen.util.RandomUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("CommentedOutCode")
public class SkilletBlockEntity extends AbstractCookingBlockEntity implements WorldlyContainer {

    public static final int PAN_SLOTS_COUNT = 4;
    private static final int[] ALL_SLOTS = {0, 1, 2, 3};
    private static final double PAN_MAX_RANDOM_OFFSET = 0.045;
    private static final double TAU = Math.PI * 2.0;

    private static final String HOT_UNTIL_KEY = "HotUntilTick";
    private static final int HOT_STATE_SECONDS = 20; // "Hot Skillet" retains heat for N seconds after pickup.
    private static final int HOT_STATE_TICKS = HOT_STATE_SECONDS * 20;

    private final RecipeManager.CachedCheck<CookRecipeInput, CookRecipe> cookRecipeCheck =
            RecipeManager.createCheck(ModRecipeTypes.COOK_TYPE.get());
    private final RecipeManager.CachedCheck<MixRecipeInput, MixRecipe> mixRecipeCheck =
            RecipeManager.createCheck(ModRecipeTypes.MIX_TYPE.get());

    private final float[] panRotation = new float[PAN_SLOTS_COUNT];
    private final float[] panOffsetX = new float[PAN_SLOTS_COUNT];
    private final float[] panOffsetZ = new float[PAN_SLOTS_COUNT];
    private final int renderSeedBase;

    private final List<ItemStack> occupiedScratch = new ArrayList<>(PAN_SLOTS_COUNT);
    private long hotUntilTick;
    private boolean mixActive;

    public SkilletBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SKILLET.get(), pos, state, PAN_SLOTS_COUNT);
        this.renderSeedBase = (int) pos.asLong();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SkilletBlockEntity entity) {
        genericTick(level, pos, state, entity);
    }

    private static void playSizzlePlace(Level level, BlockPos pos) {
        float pitch = RandomUtil.jitteredPitch(level.getRandom(), 0.95F, 0.15F);
        level.playSound(null, pos, ModSounds.GRILL_PLACE_FOOD.get(), SoundSource.BLOCKS, 0.6F, pitch);
    }

    public int getRenderSeedBase() {
        return renderSeedBase;
    }

    public float getPanRotation(int slot) {
        return panRotation[slot];
    }

    public float getPanOffsetX(int slot) {
        return panOffsetX[slot];
    }

    public float getPanOffsetZ(int slot) {
        return panOffsetZ[slot];
    }

    @SuppressWarnings("unused")
    public boolean isMixActive() {
        return mixActive;
    }

    private void cachePanTransform(int slot) {
        long baseSeed = slotSeeds[slot];
        RandomSource slotRand = RandomSource.create(baseSeed + slot + 17);

        float sign = ((baseSeed + slot) & 1L) == 0L ? 1.0F : -1.0F;
        panRotation[slot] = (15.0F + slotRand.nextFloat() * 20.0F) * sign;

        double distance = slotRand.nextFloat() * PAN_MAX_RANDOM_OFFSET;
        double angle = slotRand.nextFloat() * TAU;
        panOffsetX[slot] = (float) (Math.cos(angle) * distance);
        panOffsetZ[slot] = (float) (Math.sin(angle) * distance);
    }

    @Override
    protected void onSlotSeedAssigned(int slot) {
        cachePanTransform(slot);
    }

    @Override
    protected void onSlotsLoaded() {
        for (int slot = 0; slot < PAN_SLOTS_COUNT; slot++) {
            cachePanTransform(slot);
        }
        refreshMixState();
    }

    @Override
    protected boolean isBlockActive(Level level, BlockState state) {
        return state.getValue(SkilletBlock.LIT);
    }

    private boolean canCookAt(Level level, ItemStack stack) {
        if (level instanceof ServerLevel serverLevel) {
            return cookRecipeCheck.getRecipeFor(new CookRecipeInput(stack), serverLevel).isPresent();
        }
        return false;
    }

    @Override
    protected CookResult resolveRecipe(Level level, int slot, ItemStack stack) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return null;
        }
        CookRecipeInput cookInput = new CookRecipeInput(stack);
        Optional<RecipeHolder<CookRecipe>> cookRecipe = cookRecipeCheck.getRecipeFor(cookInput, serverLevel);
        if (cookRecipe.isEmpty()) {
            return null;
        }
        CookRecipe recipe = cookRecipe.get().value();
        ItemStack output = recipe.assemble(cookInput, level.registryAccess());
        return new CookResult(output, recipe.cookingTime());
    }

    private List<ItemStack> collectOccupied() {
        occupiedScratch.clear();
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                occupiedScratch.add(stack);
            }
        }
        return occupiedScratch;
    }

    private void refreshMixState() {
        Level level = getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            mixActive = false;
            return;
        }

        List<ItemStack> occupied = collectOccupied();
        if (occupied.size() < 2) {
            mixActive = false;
            return;
        }

        mixActive = mixRecipeCheck.getRecipeFor(new MixRecipeInput(occupied), serverLevel).isPresent();
    }

    public boolean hasCookableRecipe(ItemStack stack) {
        Level level = getLevel();
        if (level == null || !hasFreeSlot()) {
            return false;
        }
        return canCookAt(level, stack) || couldContributeToMix((ServerLevel) level, stack);
    }

    private boolean couldContributeToMix(ServerLevel serverLevel, ItemStack candidate) {
        List<ItemStack> occupied = collectOccupied();
        int hypotheticalSize = occupied.size() + 1;
        if (hypotheticalSize > PAN_SLOTS_COUNT) {
            return false;
        }

        var mixType = ModRecipeTypes.MIX_TYPE.get();

        //? if >=1.21.2{
        /*return serverLevel.recipeAccess().getRecipes().stream()
                .filter(holder -> holder.value().getType() == mixType)
                .map(holder -> (MixRecipe) holder.value())
                .anyMatch(recipe -> recipe.inputs().size() == hypotheticalSize
                        && matchesPartial(recipe.inputs(), occupied, candidate));
        *///?} else {
        return serverLevel.getRecipeManager().getAllRecipesFor(mixType).stream()
                .map(RecipeHolder::value)
                .anyMatch(recipe -> recipe.inputs().size() == hypotheticalSize
                        && matchesPartial(recipe.inputs(), occupied, candidate));
        //?}
    }

    private static boolean matchesPartial(List<Ingredient> ingredients, List<ItemStack> occupied, ItemStack candidate) {
        int size = ingredients.size();
        boolean[] consumed = new boolean[size];

        if (!assignOne(ingredients, consumed, candidate)) {
            return false;
        }
        for (ItemStack stack : occupied) {
            if (!assignOne(ingredients, consumed, stack)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean assignOne(List<Ingredient> ingredients, boolean[] consumed, ItemStack stack) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (!consumed[i] && ingredients.get(i).test(stack)) {
                consumed[i] = true;
                return true;
            }
        }
        return false;
    }

    public boolean isCooking() {
        return nonEmptySlotCount > 0;
    }

    private boolean hasFreeSlot() {
        return nonEmptySlotCount < PAN_SLOTS_COUNT;
    }

    public boolean placeFood(@Nullable LivingEntity entity, ItemStack food) {
        Level lvl = getLevel();
        if (lvl == null) {
            return false;
        }

        int slot = -1;
        for (int i = 0; i < PAN_SLOTS_COUNT; i++) {
            if (items.get(i).isEmpty()) {
                slot = i;
                break;
            }
        }
        if (slot < 0) {
            return false;
        }

        ItemStack inserted = food.copyWithCount(1);
        CookResult result = resolveRecipe(lvl, slot, inserted);

        items.set(slot, food.consumeAndReturn(1, entity));
        nonEmptySlotCount++;
        this.slotSeeds[slot] = lvl.getRandom().nextLong();
        onSlotSeedAssigned(slot);

        refreshMixState();

        if (mixActive) {
            consolidateMixCook(lvl);
        } else if (result != null) {
            cookProgress[slot] = 0;
            cachedOutput[slot] = result.output();
            cookTime[slot] = result.cookTime();
            cookingSlotCount++;
        }

        playSizzlePlace(lvl, getBlockPos());
        lvl.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(entity, getBlockState()));
        markUpdated();
        return true;
    }

    private void consolidateMixCook(Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        List<ItemStack> occupied = collectOccupied();
        int firstOccupied = -1;
        for (int slot = 0; slot < PAN_SLOTS_COUNT; slot++) {
            if (!items.get(slot).isEmpty()) {
                if (firstOccupied < 0) {
                    firstOccupied = slot;
                }
            } else if (cachedOutput[slot] != null) {
                cachedOutput[slot] = null;
                cookingSlotCount--;
            }
        }
        if (firstOccupied < 0) {
            return;
        }

        Optional<RecipeHolder<MixRecipe>> mixRecipe =
                mixRecipeCheck.getRecipeFor(new MixRecipeInput(occupied), serverLevel);
        if (mixRecipe.isEmpty()) {
            return;
        }

        MixRecipe recipe = mixRecipe.get().value();
        ItemStack output = recipe.assemble(new MixRecipeInput(occupied), level.registryAccess());

        for (int slot = 0; slot < PAN_SLOTS_COUNT; slot++) {
            boolean wasCooking = cachedOutput[slot] != null;
            if (slot == firstOccupied) {
                cookProgress[slot] = 0;
                cachedOutput[slot] = output;
                cookTime[slot] = CookRecipe.DEFAULT_COOKING_TIME;
                if (!wasCooking) {
                    cookingSlotCount++;
                }
            } else if (!items.get(slot).isEmpty()) {
                if (wasCooking) {
                    cachedOutput[slot] = null;
                    cookingSlotCount--;
                }
                cookProgress[slot] = 0;
            }
        }
    }

    @Override
    protected void onCookComplete(Level level, BlockPos pos, int slot, ItemStack result) {
        if (!result.isItemEnabled(level.enabledFeatures())) {
            return;
        }
        Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0625, pos.getZ() + 0.5, result);

        if (mixActive) {
            for (int i = 0; i < PAN_SLOTS_COUNT; i++) {
                if (!items.get(i).isEmpty()) {
                    items.set(i, ItemStack.EMPTY);
                    nonEmptySlotCount--;
                }
            }
            mixActive = false;
        }
    }

    public boolean isHotEligible() {
        BlockState state = getBlockState();
        return state.hasProperty(SkilletBlock.LIT) && state.getValue(SkilletBlock.LIT);
    }

    public long computeHotUntilTick(Level level) {
        return level.getGameTime() + HOT_STATE_TICKS;
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction side) {
        return ALL_SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, @NotNull ItemStack stack, @Nullable Direction direction) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, @NotNull ItemStack stack, @NotNull Direction direction) {
        return cookProgress[slot] == 0;
    }

    @Override
    public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
        if (!getItem(slot).isEmpty()) {
            return false;
        }
        Level level = getLevel();
        if (level == null) {
            return false;
        }
        return canCookAt(level, stack) || couldContributeToMix((ServerLevel) level, stack);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        super.setItem(slot, stack);
        refreshMixState();
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack result = super.removeItem(slot, amount);
        refreshMixState();
        return result;
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        hotUntilTick = tag.getLong(HOT_UNTIL_KEY);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong(HOT_UNTIL_KEY, hotUntilTick);
    }
}