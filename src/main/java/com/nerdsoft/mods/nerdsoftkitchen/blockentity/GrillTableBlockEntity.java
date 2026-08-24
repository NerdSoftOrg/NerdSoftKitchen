package com.nerdsoft.mods.nerdsoftkitchen.blockentity;

import com.nerdsoft.mods.nerdsoftkitchen.block.GrillTableBlock;
import com.nerdsoft.mods.nerdsoftkitchen.perf.StateMask;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cook.CookRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cook.CookRecipeInput;
import com.nerdsoft.mods.nerdsoftkitchen.registry.blockentity.ModBlockEntities;
import com.nerdsoft.mods.nerdsoftkitchen.registry.recipe.ModRecipeTypes;
import com.nerdsoft.mods.nerdsoftkitchen.registry.sound.ModSounds;
import com.nerdsoft.mods.nerdsoftkitchen.util.RandomUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class GrillTableBlockEntity extends AbstractCookingBlockEntity {

    public static final int GRILL_SLOTS_START = 0;
    public static final int GRILL_SLOTS_COUNT = 4;
    public static final int CAMPFIRE_SLOTS_START = 4;
    public static final int CAMPFIRE_SLOTS_COUNT = 4;
    public static final int TOTAL_SLOTS = GRILL_SLOTS_COUNT + CAMPFIRE_SLOTS_COUNT;

    private static final int[] GRILL_SLOTS = {0, 1, 2, 3};
    private static final int[] CAMPFIRE_SLOTS = {4, 5, 6, 7};
    private static final int[] ALL_SLOTS = {0, 1, 2, 3, 4, 5, 6, 7};

    private static final double GRILL_MAX_RANDOM_OFFSET = 0.03;

    private static final float HAY_BALE_MULTIPLIER = 1.25F;
    private static final float SOUL_MULTIPLIER = 1.10F;
    private static final float BASE_MULTIPLIER = 1.0F;
    private final RecipeManager.CachedCheck<CookRecipeInput, CookRecipe> grillRecipeCheck = RecipeManager.createCheck(ModRecipeTypes.COOK_TYPE.get());
    private final RecipeManager.CachedCheck<SingleRecipeInput, CampfireCookingRecipe> campfireRecipeCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
    private final float[] grillRotation = new float[GRILL_SLOTS_COUNT];
    private final float[] grillOffsetX = new float[GRILL_SLOTS_COUNT];
    private final float[] grillOffsetZ = new float[GRILL_SLOTS_COUNT];
    private float speedMultiplier = BASE_MULTIPLIER;

    public GrillTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRILL_TABLE.get(), pos, state, TOTAL_SLOTS);
    }

    private static boolean isGrillSlot(int slot) {
        return slot >= GRILL_SLOTS_START && slot < GRILL_SLOTS_START + GRILL_SLOTS_COUNT;
    }

    private static void playPlaceFood(Level level, BlockPos pos) {
        float pitch = RandomUtil.jitteredPitch(level.getRandom(), 0.9F, 0.2F);
        level.playSound(null, pos, ModSounds.GRILL_PLACE_FOOD.get(), SoundSource.BLOCKS, 0.7F, pitch);
    }

    private static float computeShuffledRotation(long baseSeed, int localSlot) {
        RandomSource rand = RandomSource.create(baseSeed);

        int[] ranges = {0, 1, 2, 3};
        RandomUtil.shuffle(ranges, rand);

        int[] signs = {1, -1, 1, -1};
        RandomUtil.shuffle(signs, rand);

        int rangeIndex = ranges[localSlot % 4];
        int sign = signs[localSlot % 4];

        RandomSource slotRand = RandomSource.create(baseSeed + localSlot + 99);
        float minAngle = 30.0F + rangeIndex * 3.75F;
        float angle = RandomUtil.jitter(slotRand, minAngle, 3.75F);

        return angle * sign;
    }

    @SuppressWarnings("SameParameterValue")
    private static double[] computeShuffledOffset(long baseSeed, int localSlot, double maxRandomOffset) {
        RandomSource rand = RandomSource.create(baseSeed ^ 0x5a5a5a5a5a5a5a5aL);

        int[] quadrants = {0, 1, 2, 3};
        RandomUtil.shuffle(quadrants, rand);

        int quadrant = quadrants[localSlot % 4];

        RandomSource slotRand = RandomSource.create((baseSeed ^ 0x5a5a5a5a5a5a5a5aL) + localSlot + 99);
        double distance = (0.4 + slotRand.nextFloat() * 0.6) * maxRandomOffset;
        double baseAngle = quadrant * (Math.PI / 2.0);
        double angle = baseAngle + slotRand.nextFloat() * (Math.PI / 2.0);

        double offsetX = Math.cos(angle) * distance;
        double offsetZ = Math.sin(angle) * distance;
        return new double[]{offsetX, offsetZ};
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GrillTableBlockEntity entity) {
        genericTick(level, pos, state, entity);
    }

    @SuppressWarnings("unused")
    public boolean isHayBelowCached() {
        return StateMask.isHotResidual(tickState());
    }

    private void setHayBelowCached(boolean value) {
        setTickState(StateMask.setHotResidual(tickState(), value));
    }

    private void cacheGrillTransform(int slot) {
        int localSlot = slot - GRILL_SLOTS_START;
        long baseSeed = slotSeeds[slot];
        grillRotation[localSlot] = computeShuffledRotation(baseSeed, localSlot);
        double[] offset = computeShuffledOffset(baseSeed, localSlot, GRILL_MAX_RANDOM_OFFSET);
        grillOffsetX[localSlot] = (float) offset[0];
        grillOffsetZ[localSlot] = (float) offset[1];
    }

    @Override
    protected void onSlotSeedAssigned(int slot) {
        if (isGrillSlot(slot)) {
            cacheGrillTransform(slot);
        }
    }

    @Override
    protected void onSlotsLoaded() {
        for (int slot = GRILL_SLOTS_START; slot < GRILL_SLOTS_START + GRILL_SLOTS_COUNT; slot++) {
            cacheGrillTransform(slot);
        }
    }

    public float getGrillRotation(int localSlot) {
        return grillRotation[localSlot];
    }

    public float getGrillOffsetX(int localSlot) {
        return grillOffsetX[localSlot];
    }

    public float getGrillOffsetZ(int localSlot) {
        return grillOffsetZ[localSlot];
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getBlockState().getBlock() instanceof GrillTableBlock grillTableBlock) {
            speedMultiplier = resolveSpeedMultiplier(grillTableBlock.isSoul());
        }
    }

    private float resolveSpeedMultiplier(boolean soul) {
        boolean hayBelow = level != null && level.getBlockState(worldPosition.below()).is(Blocks.HAY_BLOCK);
        setHayBelowCached(hayBelow);
        float multiplier = BASE_MULTIPLIER;
        if (soul) multiplier *= SOUL_MULTIPLIER;
        if (hayBelow) multiplier *= HAY_BALE_MULTIPLIER;
        return multiplier;
    }

    public void refreshSpeedMultiplier(boolean soul) {
        if (level == null) {
            return;
        }
        float multiplier = resolveSpeedMultiplier(soul);

        if (multiplier != speedMultiplier) {
            speedMultiplier = multiplier;
            refreshAllSlotRecipes();
            setChanged();
        }
    }

    private int scaledCookTime(int baseCookTime) {
        if (speedMultiplier == BASE_MULTIPLIER) {
            return baseCookTime;
        }
        int scaled = Math.round(baseCookTime / speedMultiplier);
        return Math.max(1, scaled);
    }

    @Override
    protected boolean isBlockActive(Level level, BlockState state) {
        return state.getValue(GrillTableBlock.LIT);
    }

    //? if <1.21.2 {
    private boolean canCookAt(Level level, ItemStack stack) {
        if (grillRecipeCheck.getRecipeFor(new CookRecipeInput(stack), level).isPresent()) {
            return true;
        }
        return campfireRecipeCheck.getRecipeFor(new SingleRecipeInput(stack), level).isPresent();
    }
    //?} else {
    /*private boolean canCookAt(Level level, ItemStack stack) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return false;
        }
        if (grillRecipeCheck.getRecipeFor(new CookRecipeInput(stack), serverLevel).isPresent()) {
            return true;
        }
        return campfireRecipeCheck.getRecipeFor(new SingleRecipeInput(stack), serverLevel).isPresent();
    }
    *///?}

    //? if <1.21.2 {
    @Override
    protected CookResult resolveRecipe(Level level, int slot, ItemStack stack) {
        CookRecipeInput cookInput = new CookRecipeInput(stack);
        Optional<RecipeHolder<CookRecipe>> cookRecipe = grillRecipeCheck.getRecipeFor(cookInput, level);
        if (cookRecipe.isPresent()) {
            CookRecipe recipe = cookRecipe.get().value();
            ItemStack output = recipe.assemble(cookInput, level.registryAccess());
            return new CookResult(output, scaledCookTime(recipe.cookingTime()));
        }
        SingleRecipeInput campfireInput = new SingleRecipeInput(stack);
        Optional<RecipeHolder<CampfireCookingRecipe>> campfireRecipe = campfireRecipeCheck.getRecipeFor(campfireInput, level);
        if (campfireRecipe.isPresent()) {
            CampfireCookingRecipe recipe = campfireRecipe.get().value();
            ItemStack output = recipe.assemble(campfireInput, level.registryAccess());
            return new CookResult(output, scaledCookTime(recipe.getCookingTime()));
        }
        return null;
    }
    //?} else {
    /*@Override
    protected CookResult resolveRecipe(Level level, int slot, ItemStack stack) {
        if (!(level instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return null;
        }
        CookRecipeInput cookInput = new CookRecipeInput(stack);
        Optional<RecipeHolder<CookRecipe>> cookRecipe = grillRecipeCheck.getRecipeFor(cookInput, serverLevel);
        if (cookRecipe.isPresent()) {
            CookRecipe recipe = cookRecipe.get().value();
            ItemStack output = recipe.assemble(cookInput, level.registryAccess());
            return new CookResult(output, scaledCookTime(recipe.cookingTime()));
        }
        SingleRecipeInput campfireInput = new SingleRecipeInput(stack);
        Optional<RecipeHolder<CampfireCookingRecipe>> campfireRecipe = campfireRecipeCheck.getRecipeFor(campfireInput, serverLevel);
        if (campfireRecipe.isPresent()) {
            CampfireCookingRecipe recipe = campfireRecipe.get().value();
            ItemStack output = recipe.assemble(campfireInput, level.registryAccess());
            return new CookResult(output, scaledCookTime(recipe.cookingTime()));
        }
        return null;
    }
    *///?}

    @Override
    protected void onCookComplete(Level level, BlockPos pos, int slot, ItemStack result) {
        dropCookedItem(level, pos, slot, result);
    }

    private void dropCookedItem(Level level, BlockPos pos, int slot, ItemStack result) {
        if (!result.isItemEnabled(level.enabledFeatures())) {
            return;
        }
        if (isGrillSlot(slot)) {
            Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, result);
        } else {
            Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, result);
        }
    }

    public boolean hasCookableRecipe(ItemStack stack) {
        Level level = getLevel();
        if (level == null || !canCookAt(level, stack)) {
            return false;
        }
        return hasFreeSlot(GRILL_SLOTS_START, GRILL_SLOTS_COUNT) || hasFreeSlot(CAMPFIRE_SLOTS_START, CAMPFIRE_SLOTS_COUNT);
    }

    public boolean isCooking() {
        return nonEmptySlotCount > 0;
    }

    private boolean hasFreeSlot(int start, int count) {
        for (int slot = start; slot < start + count; slot++) {
            if (items.get(slot).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean placeFood(@Nullable LivingEntity entity, ItemStack food) {
        if (placeFoodInRange(entity, food, GRILL_SLOTS_START, GRILL_SLOTS_COUNT)) {
            return true;
        }
        return placeFoodInRange(entity, food, CAMPFIRE_SLOTS_START, CAMPFIRE_SLOTS_COUNT);
    }

    private boolean placeFoodInRange(@Nullable LivingEntity entity, ItemStack food, int start, int count) {
        Level lvl = getLevel();
        if (lvl == null) {
            return false;
        }
        for (int slot = start; slot < start + count; slot++) {
            if (!items.get(slot).isEmpty()) {
                continue;
            }

            ItemStack inserted = food.copyWithCount(1);
            CookResult result = resolveRecipe(lvl, slot, inserted);
            if (result == null) {
                return false;
            }

            cookProgress[slot] = 0;
            cachedOutput[slot] = result.output();
            cookTime[slot] = result.cookTime();
            items.set(slot, food.consumeAndReturn(1, entity));
            cookingSlotCount++;
            adjustNonEmptySlotCount(1);
            this.slotSeeds[slot] = lvl.getRandom().nextLong();
            onSlotSeedAssigned(slot);
            playPlaceFood(lvl, getBlockPos());
            lvl.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(entity, getBlockState()));
            markUpdated();
            return true;
        }
        return false;
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction side) {
        if (side == Direction.UP) {
            return GRILL_SLOTS;
        }
        if (side == Direction.DOWN) {
            return CAMPFIRE_SLOTS;
        }
        return ALL_SLOTS;
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
        return canCookAt(level, stack);
    }

    public void copyItemsFrom(NonNullList<ItemStack> source) {
        int count = Math.min(source.size(), items.size());
        for (int i = 0; i < count; i++) {
            items.set(i, source.get(i));
        }
        refreshOccupancyCount();
        refreshAllSlotRecipes();
        setChanged();
    }
}
