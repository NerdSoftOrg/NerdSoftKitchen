package com.nerdsoft.mods.nerdsoftkitchen.blockentity;

import com.nerdsoft.mods.nerdsoftkitchen.block.SkilletBlock;
import com.nerdsoft.mods.nerdsoftkitchen.perf.StateMask;
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

    private final int[] perItemCookTime = new int[PAN_SLOTS_COUNT];
    private final ItemStack[] perItemOutput = new ItemStack[PAN_SLOTS_COUNT];

    private final ItemStack[] vacatedIngredient = new ItemStack[PAN_SLOTS_COUNT];
    private final int[] vacatedProgress = new int[PAN_SLOTS_COUNT];

    private long hotUntilTick;
    private int damage = 0;

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

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
        setChanged();
    }

    @SuppressWarnings("unused")
    public boolean isMixActive() {
        return StateMask.isHeated(tickState());
    }

    private void setMixActive(boolean value) {
        setTickState(StateMask.setHeated(tickState(), value));
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
    public void onLoad() {
        int[] progressSnapshot = cookProgress.clone();
        super.onLoad();
        // The base's post-load refresh (refreshAllSlotRecipes) calls resolveRecipe, which only
        // resolves single-ingredient CookRecipes and knows nothing about mixes or batch scaling;
        // for any slot it couldn't resolve (mix ingredients, or a CookRecipe slot) it zeroes both
        // cookProgress and cachedOutput. Reconstruct both cases here from the persisted items.
        if (isMixActive()) {
            System.arraycopy(progressSnapshot, 0, cookProgress, 0, PAN_SLOTS_COUNT);
            for (int slot = 0; slot < PAN_SLOTS_COUNT; slot++) {
                clearBatchTracking(slot);
            }
            reconsolidateMixAfterLoad();
            return;
        }
        for (int slot = 0; slot < PAN_SLOTS_COUNT; slot++) {
            ItemStack stack = items.get(slot);
            if (stack.isEmpty() || cachedOutput[slot] == null) {
                clearBatchTracking(slot);
                continue;
            }
            int count = stack.getCount();
            perItemOutput[slot] = cachedOutput[slot].copyWithCount(1);
            perItemCookTime[slot] = Math.max(1, cookTime[slot]);
            cachedOutput[slot] = scaleOutput(perItemOutput[slot], count);
            cookTime[slot] = perItemCookTime[slot] * count;
            cookProgress[slot] = progressSnapshot[slot];
        }
    }

    /** Rebuilds cachedOutput/cookTime for the loaded mix batch without disturbing cookProgress. */
    private void reconsolidateMixAfterLoad() {
        Level level = getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        List<ItemStack> occupied = collectOccupied();
        int firstOccupied = -1;
        for (int slot = 0; slot < PAN_SLOTS_COUNT; slot++) {
            if (!items.get(slot).isEmpty() && firstOccupied < 0) {
                firstOccupied = slot;
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
        MixRecipeInput mixInput = new MixRecipeInput(occupied);
        ItemStack output = recipe.assemble(mixInput, level.registryAccess());
        int batchSize = recipe.batchSize(mixInput);

        cachedOutput[firstOccupied] = output;
        cookTime[firstOccupied] = CookRecipe.DEFAULT_COOKING_TIME * Math.max(1, batchSize);
        cookingSlotCount = 1;
    }

    @Override
    protected boolean isBlockActive(Level level, BlockState state) {
        return state.getValue(SkilletBlock.LIT);
    }

    private boolean canCookAt(ServerLevel serverLevel, ItemStack stack) {
        return cookRecipeCheck.getRecipeFor(new CookRecipeInput(stack), serverLevel).isPresent();
    }

    @Override
    protected CookResult resolveRecipe(Level level, int slot, ItemStack stack) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return null;
        }
        CookRecipeInput cookInput = new CookRecipeInput(stack.copyWithCount(1));
        Optional<RecipeHolder<CookRecipe>> cookRecipe = cookRecipeCheck.getRecipeFor(cookInput, serverLevel);
        if (cookRecipe.isEmpty()) {
            return null;
        }
        CookRecipe recipe = cookRecipe.get().value();
        // Per-unit output/time; the caller scales both by the slot's live item count.
        ItemStack perItemOutput = recipe.assemble(cookInput, level.registryAccess());
        return new CookResult(perItemOutput, recipe.cookingTime());
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
            setMixActive(false);
            return;
        }

        List<ItemStack> occupied = collectOccupied();
        if (occupied.isEmpty()) {
            setMixActive(false);
            return;
        }

        boolean active = mixRecipeCheck.getRecipeFor(new MixRecipeInput(occupied), serverLevel).isPresent();
        setMixActive(active);
    }

    public boolean hasCookableRecipe(ItemStack stack) {
        Level level = getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }
        if (isMixActive() && findMixTopUpSlot(stack) >= 0) {
            return true;
        }
        if (findTopUpSlot(stack) >= 0) {
            return true;
        }
        if (!hasFreeSlot()) {
            return false;
        }
        return canCookAt(serverLevel, stack) || couldContributeToMix(serverLevel, stack);
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

        int mixTopUpSlot = isMixActive() ? findMixTopUpSlot(food) : -1;
        if (mixTopUpSlot >= 0) {
            return topUpMixFood(lvl, entity, mixTopUpSlot, food);
        }

        int topUpSlot = findTopUpSlot(food);
        if (topUpSlot >= 0) {
            return topUpFood(lvl, entity, topUpSlot, food);
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

        int amount = Math.min(food.getCount(), getMaxStackSize());
        CookResult result = resolveRecipe(lvl, slot, food);

        ItemStack placed = food.consumeAndReturn(amount, entity);
        items.set(slot, placed);
        adjustNonEmptySlotCount(1);
        this.slotSeeds[slot] = lvl.getRandom().nextLong();
        onSlotSeedAssigned(slot);

        refreshMixState();

        if (isMixActive()) {
            clearBatchTracking(slot);
            consolidateMixCook(lvl);
        } else if (result != null) {
            perItemCookTime[slot] = result.cookTime();
            perItemOutput[slot] = result.output();

            int resumedProgress = resumeVacatedProgress(slot, placed);
            cookProgress[slot] = resumedProgress;
            cookTime[slot] = perItemCookTime[slot] * placed.getCount();
            cachedOutput[slot] = scaleOutput(perItemOutput[slot], placed.getCount());
            cookingSlotCount++;
        } else {
            clearBatchTracking(slot);
        }

        playSizzlePlace(lvl, getBlockPos());
        lvl.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(entity, getBlockState()));
        markUpdated();
        return true;
    }

    /**
     * Finds an occupied slot, currently part of the active mix batch, that {@code food} matches
     * and that has room to grow before {@link #getMaxStackSize()}.
     */
    private int findMixTopUpSlot(ItemStack food) {
        for (int i = 0; i < PAN_SLOTS_COUNT; i++) {
            ItemStack existing = items.get(i);
            if (existing.isEmpty() || existing.getCount() >= getMaxStackSize()) {
                continue;
            }
            if (ItemStack.isSameItemSameComponents(existing, food)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds {@code food} into a slot that's already part of the active mix, then rescales the
     * whole mix's cook time/output for the new batch size via {@link #consolidateMixCook}.
     */
    private boolean topUpMixFood(Level lvl, @Nullable LivingEntity entity, int slot, ItemStack food) {
        ItemStack existing = items.get(slot);
        int room = getMaxStackSize() - existing.getCount();
        int amount = Math.min(food.getCount(), room);
        if (amount <= 0) {
            return false;
        }

        food.consume(amount, entity);
        existing.grow(amount);
        consolidateMixCook(lvl);

        playSizzlePlace(lvl, getBlockPos());
        lvl.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(entity, getBlockState()));
        markUpdated();
        return true;
    }

    /**
     * Finds an occupied slot that {@code food} can merge into: same item/components, on the
     * single-ingredient cook path (not an active mix), with room left before {@link #getMaxStackSize()}.
     */
    private int findTopUpSlot(ItemStack food) {
        for (int i = 0; i < PAN_SLOTS_COUNT; i++) {
            ItemStack existing = items.get(i);
            if (existing.isEmpty() || perItemOutput[i] == null) {
                continue;
            }
            if (existing.getCount() >= getMaxStackSize()) {
                continue;
            }
            if (ItemStack.isSameItemSameComponents(existing, food)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Adds {@code food} into an already-cooking slot of the same ingredient. Elapsed progress
     * ({@link #cookProgress}) is untouched; only the required time and output grow to match the
     * new item count, so a longer batch simply takes proportionally longer from where it stood.
     */
    private boolean topUpFood(Level lvl, @Nullable LivingEntity entity, int slot, ItemStack food) {
        ItemStack existing = items.get(slot);
        int room = getMaxStackSize() - existing.getCount();
        int amount = Math.min(food.getCount(), room);
        if (amount <= 0) {
            return false;
        }

        food.consume(amount, entity);
        existing.grow(amount);
        cookTime[slot] += perItemCookTime[slot] * amount;
        cachedOutput[slot] = scaleOutput(perItemOutput[slot], existing.getCount());
        clearVacatedMemory(slot);

        playSizzlePlace(lvl, getBlockPos());
        lvl.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(entity, getBlockState()));
        markUpdated();
        return true;
    }

    private static ItemStack scaleOutput(ItemStack perItemOutput, int count) {
        ItemStack scaled = perItemOutput.copy();
        scaled.setCount(perItemOutput.getCount() * Math.max(1, count));
        return scaled;
    }

    /** Recalls elapsed progress if {@code slot} was vacated with the same ingredient still pending. */
    private int resumeVacatedProgress(int slot, ItemStack placed) {
        ItemStack vacated = vacatedIngredient[slot];
        int resumed = (vacated != null && ItemStack.isSameItemSameComponents(vacated, placed)) ? vacatedProgress[slot] : 0;
        clearVacatedMemory(slot);
        return resumed;
    }

    private void clearVacatedMemory(int slot) {
        vacatedIngredient[slot] = null;
        vacatedProgress[slot] = 0;
    }

    private void clearBatchTracking(int slot) {
        perItemCookTime[slot] = 0;
        perItemOutput[slot] = null;
        clearVacatedMemory(slot);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
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
        MixRecipeInput mixInput = new MixRecipeInput(occupied);
        ItemStack output = recipe.assemble(mixInput, level.registryAccess());
        int batchSize = recipe.batchSize(mixInput);
        int mixCookTime = CookRecipe.DEFAULT_COOKING_TIME * Math.max(1, batchSize);

        for (int slot = 0; slot < PAN_SLOTS_COUNT; slot++) {
            boolean wasCooking = cachedOutput[slot] != null;
            if (slot == firstOccupied) {
                cookProgress[slot] = 0;
                cachedOutput[slot] = output;
                cookTime[slot] = mixCookTime;
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

        if (isMixActive()) {
            for (int i = 0; i < PAN_SLOTS_COUNT; i++) {
                if (!items.get(i).isEmpty()) {
                    items.set(i, ItemStack.EMPTY);
                    adjustNonEmptySlotCount(-1);
                }
            }
            setMixActive(false);
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
        ServerLevel level = (ServerLevel) getLevel();
        if (level == null) {
            return false;
        }
        return canCookAt(level, stack) || couldContributeToMix(level, stack);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        super.setItem(slot, stack);
        refreshMixState();
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int amount) {
        ItemStack before = items.get(slot);
        boolean wasBatchTracked = perItemOutput[slot] != null;
        boolean wasMixTracked = !wasBatchTracked && isMixActive() && cachedOutput[slot] != null && !before.isEmpty();
        int countBefore = before.getCount();
        ItemStack ingredientBefore = before.isEmpty() ? null : before.copyWithCount(1);

        if (wasBatchTracked && !before.isEmpty() && amount > 0 && amount < countBefore) {
            // Partial extraction: shrink the batch instead of wiping progress via the base's clearSlot.
            int taken = Math.min(amount, countBefore);
            ItemStack extracted = before.split(taken);
            int remaining = before.getCount();

            cookTime[slot] = perItemCookTime[slot] * remaining;
            cachedOutput[slot] = scaleOutput(perItemOutput[slot], remaining);
            // cookProgress[slot] intentionally left untouched here; the tick loop drains any
            // now-excess progress gradually (see decayExcessProgress).
            setChanged();
            refreshMixState();
            return extracted;
        }

        if (wasMixTracked && amount > 0 && amount < countBefore) {
            // Partial extraction from a slot that's part of the active mix: shrink that slot's
            // stack, then let the mix rescale (batch size follows the smallest matched count).
            Level lvl = getLevel();
            ItemStack extracted = before.split(Math.min(amount, countBefore));
            if (lvl != null) {
                consolidateMixCook(lvl);
            }
            setChanged();
            refreshMixState();
            return extracted;
        }

        boolean willVacate = wasBatchTracked && !before.isEmpty() && amount >= countBefore;
        int progressBeforeRemoval = cookProgress[slot];

        ItemStack result = super.removeItem(slot, amount);

        if (willVacate && !result.isEmpty()) {
            clearBatchTracking(slot);
            vacatedIngredient[slot] = ingredientBefore;
            vacatedProgress[slot] = progressBeforeRemoval;
        }

        refreshMixState();
        return result;
    }

    /**
     * Drains progress that now exceeds the slot's (shrunken) required time, one item's worth of
     * time per tick rather than one tick at a time, so a reduced batch settles back down "by
     * item time" instead of snapping to the new ceiling instantly. While draining, the slot does
     * not also advance forward that same tick. Covers both single-ingredient cook batches and
     * active mix batches (using the mix's flat per-unit time as the drain rate).
     */
    @Override
    protected boolean isSlotActive(int slot) {
        if (items.get(slot).isEmpty() || cachedOutput[slot] == null) {
            return true;
        }
        boolean tracked = perItemOutput[slot] != null || isMixActive();
        if (!tracked) {
            return true;
        }
        int excess = cookProgress[slot] - cookTime[slot];
        if (excess <= 0) {
            return true;
        }
        int rate = perItemOutput[slot] != null ? Math.max(1, perItemCookTime[slot]) : CookRecipe.DEFAULT_COOKING_TIME;
        cookProgress[slot] = Math.max(cookTime[slot], cookProgress[slot] - rate);
        return false;
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        hotUntilTick = tag.getLong(HOT_UNTIL_KEY);
        this.damage = tag.getInt("Damage");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong(HOT_UNTIL_KEY, hotUntilTick);
        tag.putInt("Damage", this.damage);
    }
}
