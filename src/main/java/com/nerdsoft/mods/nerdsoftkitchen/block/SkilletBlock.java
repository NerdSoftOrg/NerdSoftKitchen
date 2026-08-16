package com.nerdsoft.mods.nerdsoftkitchen.block;

import com.mojang.serialization.MapCodec;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.SkilletBlockEntity;
import com.nerdsoft.mods.nerdsoftkitchen.client.sound.SkilletLoopSoundManager;
import com.nerdsoft.mods.nerdsoftkitchen.item.SkilletBlockItem;
import com.nerdsoft.mods.nerdsoftkitchen.registry.blockentity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
//? if >=1.21.2 {
 import net.minecraft.world.InteractionResult;
//?}
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//? if >=1.21.2 {
import net.minecraft.world.level.redstone.Orientation;
//?}
//? if <1.21.2 {

/*import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
 *///?} else {
import net.minecraft.world.level.block.state.properties.EnumProperty;
//?}


@SuppressWarnings("CommentedOutCode")
public class SkilletBlock extends BaseEntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    //? if <1.21.2 {
    /*public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
     *///?} else {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    //?}

    public static final MapCodec<SkilletBlock> CODEC = simpleCodec(SkilletBlock::new);

    protected static final VoxelShape SHAPE = Shapes.box(0.0625, 0.0, 0.0625, 0.9375, 0.125, 0.9375);

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

    public SkilletBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(LIT, false)
                        .setValue(FACING, Direction.NORTH)
        );
    }


    public static boolean isHeatSourceBelow(LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);
        Block belowBlock = belowState.getBlock();

        if (belowBlock instanceof GrillTableBlock) {
            return belowState.hasProperty(GrillTableBlock.LIT) && belowState.getValue(GrillTableBlock.LIT);
        }
        if (belowBlock instanceof CampfireBlock) {
            return belowState.hasProperty(BlockStateProperties.LIT) && belowState.getValue(BlockStateProperties.LIT);
        }
        return belowBlock instanceof FireBlock || belowBlock == Blocks.MAGMA_BLOCK || belowBlock == Blocks.LAVA;
    }

    @Override
    public @NotNull MapCodec<SkilletBlock> codec() {
        return CODEC;
    }

    @Override
    //? if <1.21.2 {
    /*protected @NotNull ItemInteractionResult useItemOn(
     *///?} else
    protected @NotNull InteractionResult useItemOn(
            @NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult
    ) {
        if (level.getBlockEntity(pos) instanceof SkilletBlockEntity skilletEntity) {
            ItemStack itemstack = player.getItemInHand(hand);
            if (skilletEntity.hasCookableRecipe(itemstack)) {
                if (!level.isClientSide && skilletEntity.placeFood(player, itemstack)) {
                    player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    //? if <1.21.2 {
                    /*return ItemInteractionResult.SUCCESS;
                     *///?} else
                    return InteractionResult.SUCCESS;
                }

                //? if <1.21.2 {
                /*return ItemInteractionResult.CONSUME;
                 *///?} else
                return InteractionResult.CONSUME;
            }
        }

        //? if <1.21.2 {
        /*return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
         *///?} else
        return InteractionResult.PASS;
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!level.isClientSide && state.getValue(LIT) && entity instanceof LivingEntity && !entity.fireImmune()) {
            entity.igniteForSeconds(1);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        return this.defaultBlockState()
                .setValue(LIT, isHeatSourceBelow(levelaccessor, blockpos))
                .setValue(FACING, context.getHorizontalDirection());
    }

    //? if <1.21.2 {
    /*@Override
    protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @NotNull BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        if (!level.isClientSide && neighborPos.equals(pos.below())) {
            refreshLitState(level, pos, state);
        }
    }
    *///?} else {
    @Override
    protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        if (!level.isClientSide) {
            refreshLitState(level, pos, state);
        }
    }
    //?}

    private void refreshLitState(Level level, BlockPos pos, BlockState state) {
        boolean shouldBeLit = isHeatSourceBelow(level, pos);
        if (state.getValue(LIT) != shouldBeLit) {
            level.setBlock(pos, state.setValue(LIT, shouldBeLit), 3);
        }
    }

    //? if <1.21.2 {
    /*@Override
    protected void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof SkilletBlockEntity skilletEntity) {
                skilletEntity.dropContents(level, pos);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }
    *///?} else {
    @Override
    protected void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof SkilletBlockEntity skilletEntity) {
                skilletEntity.dropContents(level, pos);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
    //?}

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos,
                               @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
        if (!level.isClientSide
                && !player.isCreative()
                && blockEntity instanceof SkilletBlockEntity skilletEntity
                && skilletEntity.isHotEligible()) {
            ItemStack hotStack = new ItemStack(this);
            SkilletBlockItem.pickupHotState(hotStack, skilletEntity, level);
            popResource(level, pos, hotStack);
            player.awardStat(Stats.BLOCK_MINED.get(this));
            player.causeFoodExhaustion(0.005F);
            return;
        }
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
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
        builder.add(LIT, FACING);
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
    /*protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType type) {
        return false;
    }
    *///?} else {
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType pathComputationType) {
        return false;
    }
    //?}
}