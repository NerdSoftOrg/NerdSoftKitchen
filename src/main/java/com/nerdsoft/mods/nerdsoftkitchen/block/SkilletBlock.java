package com.nerdsoft.mods.nerdsoftkitchen.block;

import com.mojang.serialization.MapCodec;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.SkilletBlockEntity;
import com.nerdsoft.mods.nerdsoftkitchen.client.sound.SkilletLoopSoundManager;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.SkilletBlockItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlockTags;
import com.nerdsoft.mods.nerdsoftkitchen.registry.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//? if >=1.21.2 {
/*import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerLevel;
*///?} else {
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
//?}


@SuppressWarnings("CommentedOutCode")
public class SkilletBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    //? if <1.21.2 {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    //?} else {
    /*public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
     *///?}

    public static final MapCodec<SkilletBlock> CODEC = simpleCodec(SkilletBlock::new);

    protected static final VoxelShape SHAPE_BASE = Block.box(2.0, 0.0, 2.0, 14.0, 2.0, 14.0);
    protected static final VoxelShape RIM_NORTH = Block.box(7.25, 0.25, -6.5, 8.75, 1.75, 2.0);

    protected static final VoxelShape[] RIM_BY_DIRECTION = new VoxelShape[4];

    private static final int SMOKE_CHANCE_COOKING = 3;
    private static final int SMOKE_CHANCE_IDLE = 8;
    private static final int OIL_SPATTER_CHANCE = 4;
    private static final double SMOKE_Y_OFFSET = 0.15;
    private static final double SMOKE_XZ_JITTER = 0.5;
    private static final double SMOKE_RISE_SPEED = 0.015;
    private static final double OIL_Y_OFFSET = 0.13;
    private static final double OIL_XZ_SPREAD = 0.6;
    private static final double OIL_XZ_MARGIN = 0.2;
    private static final double OIL_RISE_SPEED = 0.01;

    static {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            RIM_BY_DIRECTION[dir.get2DDataValue()] = rotateShape(Direction.NORTH, dir, RIM_NORTH);
        }
    }

    public SkilletBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(LIT, false)
                        .setValue(WATERLOGGED, false)
                        .setValue(FACING, Direction.NORTH)
        );
    }

    public static boolean isHeatSourceBelow(LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (belowState.is(ModBlockTags.HEAT_SOURCES)) {

            if (belowState.hasProperty(BlockStateProperties.LIT)) {
                return belowState.getValue(BlockStateProperties.LIT);
            }

            return true;
        }

        return level.getFluidState(belowPos).is(FluidTags.LAVA);
    }

    @Override
    public @NotNull MapCodec<SkilletBlock> codec() {
        return CODEC;
    }

    @Override
            //? if <1.21.2 {
    protected @NotNull ItemInteractionResult useItemOn(
            //?} else
            //protected @NotNull InteractionResult useItemOn(
            @NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult
    ) {
        if (level.getBlockEntity(pos) instanceof SkilletBlockEntity skilletEntity) {
            ItemStack itemstack = player.getItemInHand(hand);

            if (itemstack.getItem() instanceof IronCupItem) {
                return handleCupInteraction(skilletEntity, level, player, hand, itemstack);
            }

            if (skilletEntity.hasCookableRecipe(itemstack)) {
                if (!level.isClientSide && skilletEntity.placeFood(player, itemstack)) {
                    player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
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

            if (itemstack.is(Items.EGG)) {
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

    //? if <1.21.2 {
    private ItemInteractionResult handleCupInteraction(SkilletBlockEntity skilletEntity, Level level, Player player,
                                                        InteractionHand hand, ItemStack cupStack) {
    //?} else {
    /*private InteractionResult handleCupInteraction(SkilletBlockEntity skilletEntity, Level level, Player player,
                                                    InteractionHand hand, ItemStack cupStack) {
    *///?}
        boolean cupEmpty = IronCupItem.isEmpty(cupStack);

        if (cupEmpty && skilletEntity.canExtractEggToCup()) {
            if (!level.isClientSide) {
                ItemStack filledCup = skilletEntity.extractEggToCup(cupStack);
                if (player.getAbilities().instabuild) {
                    player.setItemInHand(hand, filledCup);
                } else {
                    cupStack.shrink(1);
                    if (cupStack.isEmpty()) {
                        player.setItemInHand(hand, filledCup);
                    } else if (!player.getInventory().add(filledCup)) {
                        player.drop(filledCup, false);
                    }
                }
                level.playSound(null, skilletEntity.getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            //? if <1.21.2 {
            return ItemInteractionResult.SUCCESS;
            //?} else
            //return InteractionResult.SUCCESS;
        }

        IronCupContent content = IronCupItem.contentOf(cupStack);
        boolean cupHasEgg = content == IronCupContent.LIQUID_EGG;

        if (cupHasEgg && skilletEntity.canPourEggFromCup()) {
            if (!level.isClientSide) {
                skilletEntity.pourEggFromCup();
                ItemStack emptyCup = new ItemStack(cupStack.getItem());
                if (player.getAbilities().instabuild) {
                    player.setItemInHand(hand, emptyCup);
                } else {
                    cupStack.shrink(1);
                    if (cupStack.isEmpty()) {
                        player.setItemInHand(hand, emptyCup);
                    } else if (!player.getInventory().add(emptyCup)) {
                        player.drop(emptyCup, false);
                    }
                }
                level.playSound(null, skilletEntity.getBlockPos(), SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            //? if <1.21.2 {
            return ItemInteractionResult.SUCCESS;
            //?} else
            //return InteractionResult.SUCCESS;
        }

        //? if <1.21.2 {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        //?} else
        //return InteractionResult.PASS;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(
            @NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult
    ) {
        if (level.getBlockEntity(pos) instanceof SkilletBlockEntity skilletEntity && skilletEntity.hasHandRecoverableContents()) {
            if (!level.isClientSide) {
                ItemStack taken = skilletEntity.takeContents();
                if (!taken.isEmpty()) {
                    if (!player.getInventory().add(taken)) {
                        player.drop(taken, false);
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.6F, 1.0F);
                }
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        CookingBlockHelpers.applyStepBurn(level, pos, state, entity, LIT, this);
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        boolean waterlogged = level.getFluidState(pos).is(FluidTags.WATER);

        boolean isHeated = !waterlogged && isHeatSourceBelow(level, pos);

        BlockState state = this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(WATERLOGGED, waterlogged)
                .setValue(LIT, isHeated);

        if (canSurvive(state, level, pos)) {
            return state;
        }

        for (Direction altFacing : Direction.Plane.HORIZONTAL) {
            BlockState altState = state.setValue(FACING, altFacing);
            if (canSurvive(altState, level, pos)) {
                return altState;
            }
        }

        return null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos neighborPos = pos.relative(facing);
        BlockState neighborState = level.getBlockState(neighborPos);

        if (neighborState.is(this)) {
            Direction neighborFacing = neighborState.getValue(FACING);
            if (neighborFacing == facing.getOpposite()) {
                return false;
            }
        }

        return super.canSurvive(state, level, pos);
    }

    //? if <1.21.2 {
    @Override
    protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @NotNull BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        //?} else {
        /*@Override
        protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
            super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        *///?}

        if (!level.isClientSide) {
            refreshLitState(level, pos, state);
        }
    }

    private void refreshLitState(Level level, BlockPos pos, BlockState state) {
        boolean shouldBeLit = !state.getValue(WATERLOGGED) && isHeatSourceBelow(level, pos);
        if (state.getValue(LIT) != shouldBeLit) {
            level.setBlock(pos, state.setValue(LIT, shouldBeLit), 3);
        }
    }

    @Override
    public boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos pos, BlockState state, @NotNull FluidState fluidState) {
        if (!state.getValue(WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            if (state.getValue(LIT) && !level.isClientSide()) {
                level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            level.setBlock(pos, state.setValue(LIT, false).setValue(WATERLOGGED, true), 3);
            level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            return true;
        }
        return false;
    }

    @Override
    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
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

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos,
                              @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
        if (!level.isClientSide && !player.isCreative() && blockEntity instanceof SkilletBlockEntity skilletEntity) {

            ItemStack stack = new ItemStack(this);

            if (skilletEntity.getDamage() > 0) {
                stack.setDamageValue(skilletEntity.getDamage());
            }

            if (skilletEntity.isHotEligible()) {
                SkilletBlockItem.pickupHotState(stack, skilletEntity, level);
            }

            popResource(level, pos, stack);
            player.awardStat(Stats.BLOCK_MINED.get(this));
            player.causeFoodExhaustion(0.005F);
            return;
        }
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction facing = state.getValue(FACING);
        BlockPos neighborPos = pos.relative(facing);

        //? if <1.21.2 {
        if (level.getBlockState(neighborPos).isSolidRender(level, neighborPos)) {
        //?} else {
        /*if (level.getBlockState(neighborPos).isSolidRender()) {
        *///?}
            return SHAPE_BASE;
        }

        return Shapes.or(SHAPE_BASE, RIM_BY_DIRECTION[facing.get2DDataValue()]);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction facing = state.getValue(FACING);
        int index = facing.get2DDataValue();
        VoxelShape rim = RIM_BY_DIRECTION[index];

        if (context instanceof EntityCollisionContext entityContext) {
            Entity entity = entityContext.getEntity();
            if (entity != null) {
                VoxelShape entityShape = Shapes.create(entity.getBoundingBox().move(-pos.getX(), -pos.getY(), -pos.getZ()));

                if (Shapes.joinIsNotEmpty(rim, entityShape, BooleanOp.AND)) {
                    return SHAPE_BASE;
                }
            }
        }

        return Shapes.or(SHAPE_BASE, rim);
    }

    //? if <1.21.2 {
    @Override
    public @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos neighborPos) {
    //?} else {
    /*@Override
    protected @NotNull BlockState updateShape(BlockState state, @NotNull LevelReader level, @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos currentPos, @NotNull Direction facing, @NotNull BlockPos neighborPos, @NotNull BlockState neighborState, @NotNull RandomSource random) {
        *///?}
        if (facing == state.getValue(FACING) && !state.canSurvive(level, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (state.getValue(WATERLOGGED)) {
            //? if <1.21.2 {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            //?} else {
            /*scheduledTickAccess.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            *///?}
        }
        //? if <1.21.2 {
        return super.updateShape(state, facing, neighborState, level, currentPos, neighborPos);
        //?} else {
        /*return super.updateShape(state, level, scheduledTickAccess, currentPos, facing, neighborPos, neighborState, random);
        *///?}
    }

    @Override
    public void animateTick(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        boolean cooking = level.getBlockEntity(pos) instanceof SkilletBlockEntity skilletEntity && skilletEntity.isCooking();

        if (random.nextInt(cooking ? SMOKE_CHANCE_COOKING : SMOKE_CHANCE_IDLE) == 0) {
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    pos.getX() + 0.5 + (random.nextDouble() - 0.5) * SMOKE_XZ_JITTER,
                    pos.getY() + SMOKE_Y_OFFSET,
                    pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * SMOKE_XZ_JITTER,
                    0.0, SMOKE_RISE_SPEED, 0.0);
        }

        if (cooking && random.nextInt(OIL_SPATTER_CHANCE) == 0) {
            level.addParticle(ParticleTypes.LAVA,
                    pos.getX() + OIL_XZ_MARGIN + random.nextDouble() * OIL_XZ_SPREAD,
                    pos.getY() + OIL_Y_OFFSET,
                    pos.getZ() + OIL_XZ_MARGIN + random.nextDouble() * OIL_XZ_SPREAD,
                    0.0, OIL_RISE_SPEED, 0.0);
        }
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

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, WATERLOGGED, FACING);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SkilletBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (level.isClientSide) {
            return createTickerHelper(blockEntityType, ModBlockEntities.SKILLET.get(),
                    SkilletLoopSoundManager::update);
        }

        return createTickerHelper(blockEntityType, ModBlockEntities.SKILLET.get(), SkilletBlockEntity::tick);
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

    @SuppressWarnings("SameParameterValue")
    private static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};
        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;

        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                    buffer[1] = Shapes.or(buffer[1], Shapes.create(1.0 - maxZ, minY, minX, 1.0 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }
        return buffer[0];
    }
}
