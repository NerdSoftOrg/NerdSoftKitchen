package com.panzer.mods.dice_and_delish.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.panzer.mods.dice_and_delish.blockentity.GrillTableBlockEntity;
import com.panzer.mods.dice_and_delish.client.sound.GrillLoopSoundManager;
import com.panzer.mods.dice_and_delish.registry.block.ModBlocks;
import com.panzer.mods.dice_and_delish.registry.blockentity.ModBlockEntities;
import com.panzer.mods.dice_and_delish.registry.data.ModCustomStats;
import com.panzer.mods.dice_and_delish.registry.sound.ModSounds;
import com.panzer.mods.dice_and_delish.util.RandomUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//? if >=1.21.2 {
/*import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.properties.EnumProperty;
*///?} else {
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
//?}

public class GrillTableBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    //? if <1.21.2 {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    //?} else {
    /*public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    *///?}

    public static final MapCodec<GrillTableBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                            Codec.BOOL.fieldOf("soul").forGetter(b -> b.soul),
                            propertiesCodec()
                    )
                    .apply(instance, GrillTableBlock::new)
    );

    protected static final VoxelShape SHAPE = Shapes.block();

    private final boolean soul;

    public GrillTableBlock(boolean soul, Properties properties) {
        super(properties);
        this.soul = soul;
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(LIT, true)
                        .setValue(WATERLOGGED, false)
                        .setValue(FACING, Direction.NORTH)
        );
    }

    public boolean isSoul() {
        return soul;
    }

    @Override
    public @NotNull MapCodec<GrillTableBlock> codec() {
        return CODEC;
    }

    @Override
    //? if <1.21.2 {
    protected @NotNull ItemInteractionResult useItemOn(
    //?} else
    //protected @NotNull InteractionResult useItemOn(
            @NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult
    ) {
        if (level.getBlockEntity(pos) instanceof GrillTableBlockEntity grillTableBlockEntity) {
            ItemStack itemstack = player.getItemInHand(hand);

            if (state.getValue(LIT) && itemstack.is(ItemTags.SHOVELS)) {
                extinguishWithTool(state, level, pos, player, hand, itemstack);
                //? if <1.21.2 {
                return ItemInteractionResult.SUCCESS;
                //?} else
                //return InteractionResult.SUCCESS;
            }

            if (canLight(state) && (itemstack.getItem() instanceof FlintAndSteelItem || itemstack.getItem() instanceof FireChargeItem)) {
                igniteWithTool(state, level, pos, player, hand, itemstack);
                //? if <1.21.2 {
                return ItemInteractionResult.SUCCESS;
                //?} else
                //return InteractionResult.SUCCESS;
            }

            if (grillTableBlockEntity.hasCookableRecipe(itemstack)) {
                if (!level.isClientSide && grillTableBlockEntity.placeFood(player, itemstack)) {
                    player.awardStat(ModCustomStats.interactWithGrillTableStat());
                    //? if <1.21.2 {
                    return ItemInteractionResult.SUCCESS;
                    //?} else
                    //return InteractionResult.SUCCESS;
                }

                //? if <1.21.2 {
                return ItemInteractionResult.CONSUME;
                //?} else
                //return InteractionResult.CONSUME;
            }
        }

        //? if <1.21.2 {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        //?} else
        //return InteractionResult.PASS;
    }

    private static boolean canLight(BlockState state) {
        return !state.getValue(LIT) && !state.getValue(WATERLOGGED);
    }

    private void extinguishWithTool(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack tool) {
        if (!level.isClientSide()) {
            level.levelEvent(null, 1009, pos, 0);

            if (soul) {
                swapToUnlit(state, level, pos, level.getFluidState(pos));
            } else {
                level.setBlock(pos, state.setValue(LIT, false), 11);
            }

            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            tool.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
        }
    }

    private void igniteWithTool(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack tool) {
        if (!level.isClientSide()) {
            level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(pos, state.setValue(LIT, true), 11);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);

            if (tool.getItem() instanceof FlintAndSteelItem) {
                tool.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            } else {
                tool.shrink(1);
            }
        }
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        CookingBlockHelpers.applyStepBurn(level, pos, state, entity, LIT, this);
        super.stepOn(level, pos, state, entity);
    }

    //? if <1.21.2 {
    @Override
    protected void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            CookingBlockHelpers.dropContentsOnRemoval(state, level, pos, newState);
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }
    //?} else {
    /*@Override
    protected void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            CookingBlockHelpers.dropContentsOnRemoval(state, level, pos, newState);
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
    *///?}

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        boolean waterlogged = levelaccessor.getFluidState(blockpos).getType() == Fluids.WATER;
        boolean isUnlitVariant = this == ModBlocks.GRILL_TABLE_UNLIT.get() || this == ModBlocks.GRILL_TABLE_SOUL_UNLIT.get();
        return this.defaultBlockState()
                .setValue(WATERLOGGED, waterlogged)
                .setValue(LIT, !waterlogged && !isUnlitVariant)
                .setValue(FACING, context.getHorizontalDirection());
    }

    //? if <1.21.2 {
    @Override
    protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @NotNull BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (!level.isClientSide && neighborPos.equals(pos.below())) {
            if (level.getBlockEntity(pos) instanceof GrillTableBlockEntity grillTableBlockEntity) {
                grillTableBlockEntity.refreshSpeedMultiplier(soul);
            }
        }
    }
    //?} else {
    /*@Override
    protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof GrillTableBlockEntity grillTableBlockEntity) {
                grillTableBlockEntity.refreshSpeedMultiplier(soul);
            }
        }
    }
    *///?}

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof GrillTableBlockEntity grillTableBlockEntity) {
            grillTableBlockEntity.refreshSpeedMultiplier(soul);
        }
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);

        //? if <1.21.2 {
        boolean isCovered = aboveState.getBlock() instanceof SkilletBlock || aboveState.isSolidRender(level, abovePos);
        //?} else {
        /*boolean isCovered = aboveState.getBlock() instanceof SkilletBlock || aboveState.isSolidRender();
         *///?}

        if (!isCovered && random.nextInt(15) == 0) {
            level.addParticle(ParticleTypes.LAVA,
                    pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.4,
                    pos.getY() + 1.0,
                    pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.4,
                    0.0, 0.05, 0.0);
        }

        if (level.getBlockEntity(pos) instanceof GrillTableBlockEntity grillTableBlockEntity && grillTableBlockEntity.isCooking()) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, ModSounds.GRILL_SIZZLE.get(), SoundSource.BLOCKS,
                    RandomUtil.jitter(random, 0.4F, 0.4F), RandomUtil.jitter(random, 0.8F, 0.4F), false);
        }
    }

    @Override
    public boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos pos, BlockState state, @NotNull FluidState fluidState) {
        if (!state.getValue(WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            boolean wasLit = state.getValue(LIT);
            if (wasLit && !level.isClientSide()) {
                level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            if (wasLit && soul) {
                swapToUnlit(state, level, pos, fluidState);
                return true;
            }

            level.setBlock(pos, state.setValue(LIT, false).setValue(WATERLOGGED, true), 3);
            level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            return true;
        }
        return false;
    }

    private void swapToUnlit(BlockState state, LevelAccessor level, BlockPos pos, FluidState fluidState) {
        NonNullList<ItemStack> preservedItems = null;
        if (level.getBlockEntity(pos) instanceof GrillTableBlockEntity oldGrillEntity) {
            preservedItems = NonNullList.withSize(oldGrillEntity.getItems().size(), ItemStack.EMPTY);
            for (int i = 0; i < preservedItems.size(); i++) {
                preservedItems.set(i, oldGrillEntity.getItems().get(i).copy());
            }
            oldGrillEntity.clearContent();
        }

        BlockState unlitState = (this.soul ? ModBlocks.GRILL_TABLE_SOUL_UNLIT.get() : ModBlocks.GRILL_TABLE_UNLIT.get())
                .defaultBlockState()
                .setValue(LIT, false)
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER)
                .setValue(FACING, state.getValue(FACING));

        level.setBlock(pos, unlitState, 3);

        if (preservedItems != null && level.getBlockEntity(pos) instanceof GrillTableBlockEntity newEntity) {
            newEntity.copyItemsFrom(preservedItems);
        }
    }

    @Override
    @SuppressWarnings({"RedundantCast", "RedundantSuppression"})
    protected void onProjectileHit(Level level, @NotNull BlockState state, BlockHitResult hit, @NotNull Projectile projectile) {
        BlockPos blockpos = hit.getBlockPos();
        if (!level.isClientSide
                && projectile.isOnFire()
                && projectile.mayInteract((ServerLevel) level, blockpos)
                && !state.getValue(LIT)
                && !state.getValue(WATERLOGGED)) {
            level.setBlock(blockpos, state.setValue(LIT, true), 11);
        }
    }

    @Override
    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    //? if <1.21.2 {
    @Override
    protected boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return true;
    }
    //?} else {
    /*@Override
    protected boolean propagatesSkylightDown(@NotNull BlockState state) {
        return true;
    }
    *///?}

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, WATERLOGGED, FACING);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new GrillTableBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return createTickerHelper(blockEntityType, ModBlockEntities.GRILL_TABLE.get(),
                    GrillLoopSoundManager::update);
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.GRILL_TABLE.get(), GrillTableBlockEntity::tick);
    }

    @Override
    //? if <1.21.2 {
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType type) {
        return false;
    }
    //?} else {
    /*protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType pathComputationType) {
        return false;
    }
    *///?}
}
