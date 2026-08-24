package com.nerdsoft.mods.nerdsoftkitchen.blockentity;

import com.nerdsoft.mods.nerdsoftkitchen.block.SkilletBlock;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
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
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("CommentedOutCode")
public class SkilletBlockEntity extends AbstractCookingBlockEntity {

    public static final int PAN_SLOTS_COUNT = 2;
    public static final int EGG_SLOT = 0;
    public static final int INGREDIENT_SLOT = 1;
    private static final int[] ALL_SLOTS = {EGG_SLOT, INGREDIENT_SLOT};

    private static final double PAN_MAX_RANDOM_OFFSET = 0.045;
    private static final double TAU = Math.PI * 2.0;
    private static final double SYNC_TRACKING_RANGE = 64.0;

    private static final String HOT_UNTIL_KEY = "HotUntilTick";
    private static final int HOT_STATE_SECONDS = 20;
    private static final int HOT_STATE_TICKS = HOT_STATE_SECONDS * 20;

    private final RecipeManager.CachedCheck<CookRecipeInput, CookRecipe> cookRecipeCheck =
            RecipeManager.createCheck(ModRecipeTypes.COOK_TYPE.get());
    private final RecipeManager.CachedCheck<MixRecipeInput, MixRecipe> mixRecipeCheck =
            RecipeManager.createCheck(ModRecipeTypes.MIX_TYPE.get());

    private final float[] panRotation = new float[PAN_SLOTS_COUNT];
    private final float[] panOffsetX = new float[PAN_SLOTS_COUNT];
    private final float[] panOffsetZ = new float[PAN_SLOTS_COUNT];

    private long hotUntilTick;
    private int damage = 0;
    private int eggAloneProgress;
    private int eggAloneCookTime;

    public SkilletBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SKILLET.get(), pos, state, PAN_SLOTS_COUNT);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SkilletBlockEntity entity) {
        if (!level.isClientSide) {
            boolean currentlyLit = state.getValue(SkilletBlock.LIT);
            boolean shouldBeLit = !state.getValue(SkilletBlock.WATERLOGGED) && SkilletBlock.isHeatSourceBelow(level, pos);

            if (currentlyLit != shouldBeLit) {
                level.setBlock(pos, state.setValue(SkilletBlock.LIT, shouldBeLit), 3);
                state = level.getBlockState(pos);
            }
        }
        genericTick(level, pos, state, entity);
        entity.tickEggAlone(level, pos, state);
    }

    private static void playSizzlePlace(Level level, BlockPos pos) {
        float pitch = RandomUtil.jitteredPitch(level.getRandom(), 0.95F, 0.15F);
        level.playSound(null, pos, ModSounds.GRILL_PLACE_FOOD.get(), SoundSource.BLOCKS, 0.6F, pitch);
    }

    private static ItemStack eggStack() {
        return new ItemStack(Items.EGG);
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
    }

    @Override
    protected boolean isBlockActive(Level level, BlockState state) {
        return state.getValue(SkilletBlock.LIT);
    }

    public boolean hasEggLiquid() {
        return items.get(EGG_SLOT).is(Items.EGG);
    }

    public boolean hasIngredient() {
        return !items.get(INGREDIENT_SLOT).isEmpty();
    }

    private boolean canCookAt(ServerLevel serverLevel, ItemStack stack) {
        return cookRecipeCheck.getRecipeFor(new CookRecipeInput(stack), serverLevel).isPresent();
    }

    @Override
    protected CookResult resolveRecipe(Level level, int slot, ItemStack stack) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return null;
        }
        if (slot == EGG_SLOT) {
            return null;
        }

        if (hasEggLiquid()) {
            return resolveMix(serverLevel, level, stack);
        }

        CookRecipeInput cookInput = new CookRecipeInput(stack.copyWithCount(1));
        Optional<RecipeHolder<CookRecipe>> cookRecipe = cookRecipeCheck.getRecipeFor(cookInput, serverLevel);
        if (cookRecipe.isEmpty()) {
            return null;
        }
        CookRecipe recipe = cookRecipe.get().value();
        ItemStack output = recipe.assemble(cookInput, level.registryAccess());
        return new CookResult(output, recipe.cookingTime());
    }

    private CookResult resolveMix(ServerLevel serverLevel, Level level, ItemStack ingredient) {
        List<ItemStack> inputs = ingredient.isEmpty()
                ? List.of(eggStack())
                : List.of(eggStack(), ingredient.copyWithCount(1));

        Optional<RecipeHolder<MixRecipe>> mixRecipe =
                mixRecipeCheck.getRecipeFor(new MixRecipeInput(inputs), serverLevel);
        if (mixRecipe.isEmpty()) {
            return null;
        }
        MixRecipe recipe = mixRecipe.get().value();
        MixRecipeInput mixInput = new MixRecipeInput(inputs);
        ItemStack output = recipe.assemble(mixInput, level.registryAccess());
        return new CookResult(output, CookRecipe.DEFAULT_COOKING_TIME);
    }

    public boolean hasCookableRecipe(ItemStack stack) {
        Level level = getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return false;
        }

        if (stack.is(Items.EGG)) {
            return !hasEggLiquid();
        }

        if (hasEggLiquid()) {
            if (hasIngredient()) {
                return false;
            }
            return resolveMix(serverLevel, level, stack) != null;
        }

        if (hasIngredient()) {
            return false;
        }
        return canCookAt(serverLevel, stack);
    }

    public boolean isCooking() {
        return nonEmptySlotCount > 0;
    }

    public boolean pourEgg(@Nullable LivingEntity entity, ItemStack eggStack) {
        Level lvl = getLevel();
        if (lvl == null || hasEggLiquid()) {
            return false;
        }
        eggStack.consume(1, entity);
        setEggSlotDirect(new ItemStack(Items.EGG));
        eggAloneProgress = 0;
        eggAloneCookTime = CookRecipe.DEFAULT_COOKING_TIME;
        playSizzlePlace(lvl, getBlockPos());
        lvl.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(entity, getBlockState()));
        markUpdated();
        return true;
    }

    private void tickEggAlone(Level level, BlockPos pos, BlockState state) {
        if (!hasEggLiquid() || hasIngredient() || !isBlockActive(level, state)) {
            return;
        }

        eggAloneProgress++;

        if (eggAloneProgress >= eggAloneCookTime) {
            if (level instanceof ServerLevel serverLevel) {
                Optional<RecipeHolder<MixRecipe>> mixRecipe =
                        mixRecipeCheck.getRecipeFor(new MixRecipeInput(List.of(eggStack())), serverLevel);
                mixRecipe.ifPresent(holder -> {
                    ItemStack output = holder.value().assemble(new MixRecipeInput(List.of(eggStack())), level.registryAccess());
                    onCookComplete(level, pos, EGG_SLOT, output);
                });
            }
            eggAloneProgress = 0;
            eggAloneCookTime = 0;
            markUpdated();
        } else if (eggAloneProgress % 5 == 0 && hasNearbyPlayer(level, pos)) {
            markUpdated();
        }
    }

    private static boolean hasNearbyPlayer(Level level, BlockPos pos) {
        return level.hasNearbyAlivePlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SYNC_TRACKING_RANGE);
    }

    public boolean placeFood(@Nullable LivingEntity entity, ItemStack food) {
        Level lvl = getLevel();
        if (lvl == null) {
            return false;
        }
        if (food.is(Items.EGG)) {
            return pourEgg(entity, food);
        }
        if (hasIngredient()) {
            return false;
        }
        if (!hasCookableRecipe(food)) {
            return false;
        }

        ItemStack placed = food.consumeAndReturn(1, entity);
        setItem(INGREDIENT_SLOT, placed);
        eggAloneProgress = 0;
        eggAloneCookTime = hasEggLiquid() ? CookRecipe.DEFAULT_COOKING_TIME : 0;

        playSizzlePlace(lvl, getBlockPos());
        lvl.gameEvent(GameEvent.BLOCK_CHANGE, getBlockPos(), GameEvent.Context.of(entity, getBlockState()));
        markUpdated();
        return true;
    }

    private void setEggSlotDirect(ItemStack stack) {
        items.set(EGG_SLOT, stack);
        if (stack.isEmpty()) {
            adjustNonEmptySlotCount(-1);
        } else {
            adjustNonEmptySlotCount(1);
        }
        refreshSlotRecipeIfIngredientPresent();
        markUpdated();
    }

    private void refreshSlotRecipeIfIngredientPresent() {
        ItemStack ingredient = items.get(INGREDIENT_SLOT);
        if (!ingredient.isEmpty()) {
            setItem(INGREDIENT_SLOT, ingredient);
        }
    }

    public ItemStack takeContents() {
        if (hasIngredient()) {
            ItemStack out = items.get(INGREDIENT_SLOT).copy();
            setItem(INGREDIENT_SLOT, ItemStack.EMPTY);
            if (hasEggLiquid()) {
                eggAloneProgress = 0;
                eggAloneCookTime = CookRecipe.DEFAULT_COOKING_TIME;
            }
            markUpdated();
            return out;
        }
        return ItemStack.EMPTY;
    }

    public boolean hasHandRecoverableContents() {
        return hasIngredient();
    }

    public boolean canExtractEggToCup() {
        return hasEggLiquid() && !hasIngredient();
    }

    public ItemStack extractEggToCup(ItemStack cupItemStack) {
        setEggSlotDirect(ItemStack.EMPTY);
        eggAloneProgress = 0;
        eggAloneCookTime = 0;
        return IronCupItem.filled(cupItemStack.getItem(), IronCupContent.LIQUID_EGG);
    }

    public boolean canPourEggFromCup() {
        return !hasEggLiquid() && !hasIngredient();
    }

    public void pourEggFromCup() {
        setEggSlotDirect(new ItemStack(Items.EGG));
        eggAloneProgress = 0;
        eggAloneCookTime = CookRecipe.DEFAULT_COOKING_TIME;
    }

    @Override
    protected void onCookComplete(Level level, BlockPos pos, int slot, ItemStack result) {
        if (!result.isItemEnabled(level.enabledFeatures())) {
            return;
        }
        Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 1.0625, pos.getZ() + 0.5, result);

        if (hasEggLiquid()) {
            setEggSlotDirect(ItemStack.EMPTY);
        }
        eggAloneProgress = 0;
        eggAloneCookTime = 0;
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
    public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
        if (!getItem(slot).isEmpty()) {
            return false;
        }
        if (!(getLevel() instanceof ServerLevel)) {
            return false;
        }
        if (slot == EGG_SLOT) {
            return stack.is(Items.EGG);
        }
        return hasCookableRecipe(stack);
    }

    public float getCookingProgress() {
        if (hasIngredient()) {
            int cookTime = getCookTime(INGREDIENT_SLOT);
            if (cookTime <= 0) return 0.0f;
            return Math.min(1.0f, (float) getCookProgress(INGREDIENT_SLOT) / (float) cookTime);
        }

        if (hasEggLiquid()) {
            if (eggAloneCookTime <= 0) return 0.0f;
            return Math.min(1.0f, (float) eggAloneProgress / (float) eggAloneCookTime);
        }

        return 0.0f;
    }

    public ItemStack getCookingResult() {
        Level lvl = getLevel();
        if (!(lvl instanceof ServerLevel serverLevel)) {
            return ItemStack.EMPTY;
        }

        if (hasIngredient()) {
            ItemStack ingredient = getItem(INGREDIENT_SLOT);
            if (hasEggLiquid()) {
                List<ItemStack> inputs = List.of(eggStack(), ingredient.copyWithCount(1));
                return mixRecipeCheck.getRecipeFor(new MixRecipeInput(inputs), serverLevel)
                        .map(holder -> holder.value().assemble(new MixRecipeInput(inputs), lvl.registryAccess()))
                        .orElse(ItemStack.EMPTY);
            } else {
                CookRecipeInput input = new CookRecipeInput(ingredient.copyWithCount(1));
                return cookRecipeCheck.getRecipeFor(input, serverLevel)
                        .map(holder -> holder.value().assemble(input, lvl.registryAccess()))
                        .orElse(ItemStack.EMPTY);
            }
        }

        if (hasEggLiquid()) {
            List<ItemStack> inputs = List.of(eggStack());
            return mixRecipeCheck.getRecipeFor(new MixRecipeInput(inputs), serverLevel)
                    .map(holder -> holder.value().assemble(new MixRecipeInput(inputs), lvl.registryAccess()))
                    .orElse(ItemStack.EMPTY);
        }

        return ItemStack.EMPTY;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(@NotNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        hotUntilTick = tag.getLong(HOT_UNTIL_KEY);
        this.damage = tag.getInt("Damage");
        this.eggAloneProgress = tag.getInt("EggAloneProgress");
        this.eggAloneCookTime = tag.getInt("EggAloneCookTime");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putLong(HOT_UNTIL_KEY, hotUntilTick);
        tag.putInt("Damage", this.damage);
        tag.putInt("EggAloneProgress", this.eggAloneProgress);
        tag.putInt("EggAloneCookTime", this.eggAloneCookTime);
    }
}
