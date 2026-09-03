package com.panzer.mods.dice_and_delish.block;

import com.mojang.serialization.MapCodec;
import com.panzer.mods.dice_and_delish.blockentity.CuttingBoardBlockEntity;
import com.panzer.mods.dice_and_delish.recipe.cook.CookRecipe;
import com.panzer.mods.dice_and_delish.recipe.cook.CookRecipeInput;
import com.panzer.mods.dice_and_delish.recipe.cutting.CuttingRecipe;
import com.panzer.mods.dice_and_delish.recipe.cutting.CuttingRecipeInput;
import com.panzer.mods.dice_and_delish.registry.tags.ModItemTags;
import com.panzer.mods.dice_and_delish.registry.recipe.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

//? if <1.21.2 {
import net.minecraft.world.ItemInteractionResult;
//?}

public class CuttingBoardBlock extends BaseEntityBlock {

    public static final MapCodec<CuttingBoardBlock> CODEC = simpleCodec(CuttingBoardBlock::new);
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    protected static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 1.0, 15.0);

    public CuttingBoardBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull MapCodec<CuttingBoardBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
            //? if <1.21.2 {
    protected @NotNull ItemInteractionResult useItemOn(
            //?} else
            //protected @NotNull InteractionResult useItemOn(
            @NotNull ItemStack stack, @NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult
    ) {
        if (!(level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity board)) {
            //? if <1.21.2 {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            //?} else
            //return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (stack.is(ModItemTags.KNIFE) && !board.isEmpty()) {
            if (player.isShiftKeyDown()) {
                return handleItemInteraction(level, board, stack, player, hand);
            }
            return tryCut(level, pos, board, stack, player);
        }

        if (!stack.isEmpty()) {
            return handleItemInteraction(level, board, stack, player, hand);
        }

        //? if <1.21.2 {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        //?} else
        //return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos,
                                                        @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity board && !board.isEmpty()) {
            //? if <1.21.2 {
            return returnItem(level, board, player).result();
            //?} else
            //return returnItem(level, board, player);
        }
        return InteractionResult.PASS;
    }

    //? if <1.21.2 {
    private ItemInteractionResult handleItemInteraction(Level level, CuttingBoardBlockEntity board, ItemStack heldStack, Player player, InteractionHand hand) {
        //?} else
        //private InteractionResult handleItemInteraction(Level level, CuttingBoardBlockEntity board, ItemStack heldStack, Player player, InteractionHand hand) {
        ItemStack stored = board.getStoredItem();

        if (stored.isEmpty()) {
            if (!level.isClientSide) {
                board.setStoredItem(heldStack.copy());
                if (!player.getAbilities().instabuild) {
                    heldStack.setCount(0);
                }
                level.playSound(null, board.getBlockPos(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            //? if <1.21.2 {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
            //?} else
            //return InteractionResult.SUCCESS;
        }

        if (ItemStack.isSameItemSameComponents(stored, heldStack)) {
            int maxStack = stored.getMaxStackSize();
            if (stored.getCount() < maxStack) {
                if (!level.isClientSide) {
                    int space = maxStack - stored.getCount();
                    int toAdd = Math.min(space, heldStack.getCount());

                    stored.grow(toAdd);
                    board.setStoredItem(stored);

                    if (!player.getAbilities().instabuild) {
                        heldStack.shrink(toAdd);
                    }
                    level.playSound(null, board.getBlockPos(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                //? if <1.21.2 {
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
                //?} else
                //return InteractionResult.SUCCESS;
            }
        } else {
            if (!level.isClientSide) {
                ItemStack newHeld = stored.copy();
                board.setStoredItem(heldStack.copy());

                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, newHeld);
                }
                level.playSound(null, board.getBlockPos(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            //? if <1.21.2 {
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
            //?} else
            //return InteractionResult.SUCCESS;
        }

        //? if <1.21.2 {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        //?} else
        //return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Nullable
    private RecipeManager getRecipeManager(Level level) {
        if (level == null) return null;

        //? if <1.21.2 {
        if (level.isClientSide()) {
            return net.minecraft.client.Minecraft.getInstance().getConnection() != null
                    ? net.minecraft.client.Minecraft.getInstance().getConnection().getRecipeManager()
                    : null;
        }
        return level.getServer() != null ? level.getServer().getRecipeManager() : null;
        //?} else {
        /*return (RecipeManager) level.recipeAccess();
         *///?}
    }

    //? if <1.21.2 {
    private ItemInteractionResult returnItem(Level level, CuttingBoardBlockEntity board, Player player) {
        //?} else
        //private InteractionResult returnItem(Level level, CuttingBoardBlockEntity board, Player player) {
        if (!level.isClientSide) {
            ItemStack removed = board.clearStoredItem();
            if (!player.getInventory().add(removed)) {
                player.drop(removed, false);
            }
        }
        //? if <1.21.2 {
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
        //?} else
        //return InteractionResult.SUCCESS;
    }

    //? if <1.21.2 {
    private ItemInteractionResult tryCut(Level level, BlockPos pos, CuttingBoardBlockEntity board, ItemStack knife, Player player) {
        //?} else
        //private InteractionResult tryCut(Level level, BlockPos pos, CuttingBoardBlockEntity board, ItemStack knife, Player player) {
        RecipeManager recipeManager = getRecipeManager(level);
        if (recipeManager == null) {
            //? if <1.21.2 {
            return ItemInteractionResult.FAIL;
            //?} else
            //return InteractionResult.FAIL;
        }

        Optional<RecipeHolder<CuttingRecipe>> match = recipeManager
                .getRecipeFor(ModRecipeTypes.CUT_TYPE.get(), new CuttingRecipeInput(board.getStoredItem(), knife), level);

        if (match.isEmpty()) {
            //? if <1.21.2 {
            return ItemInteractionResult.FAIL;
            //?} else
            //return InteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            ItemStack result = match.get().value().assemble(new CuttingRecipeInput(board.getStoredItem(), knife), level.registryAccess());
            board.clearStoredItem();

            if (hasFireAspect(knife, level)) {
                result = applyFireAspectCooking(result, level);
            }

            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, result));

            if (!player.getAbilities().instabuild && level instanceof ServerLevel serverLevel) {
                //? if <1.21.2 {
                knife.hurtAndBreak(1, serverLevel, player instanceof ServerPlayer serverPlayer ? serverPlayer : null,
                        item -> player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                //?} else {
                /*knife.hurtAndBreak(1, serverLevel, player, item -> player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                 *///?}
            }

            level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 0.6F, 1.6F);
        }

        //? if <1.21.2 {
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
        //?} else
        //return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("deprecation")
    private boolean hasFireAspect(ItemStack knife, Level level) {
        Holder<Enchantment> fireAspect = level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.FIRE_ASPECT);
        return EnchantmentHelper.getItemEnchantmentLevel(fireAspect, knife) > 0;
    }

    private ItemStack applyFireAspectCooking(ItemStack result, Level level) {
        RecipeManager recipeManager = getRecipeManager(level);
        if (recipeManager == null) {
            return result;
        }

        Optional<RecipeHolder<CookRecipe>> cookMatch = recipeManager
                .getRecipeFor(ModRecipeTypes.COOK_TYPE.get(), new CookRecipeInput(result), level);

        if (cookMatch.isEmpty()) {
            return result;
        }

        ItemStack cooked = cookMatch.get().value().assemble(new CookRecipeInput(result), level.registryAccess());
        cooked.setCount(result.getCount());
        return cooked;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity board && !board.isEmpty()) {
                level.addFreshEntity(new ItemEntity(level,
                        pos.getX() + 0.5, pos.getY() + 0.2, pos.getZ() + 0.5, board.getStoredItem()));
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CuttingBoardBlockEntity(pos, state);
    }
}
