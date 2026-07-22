package com.nerdsoft.mods.nerdsoftkitchen.crop;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class TomatoCropPoleBlock extends HarvestableCropBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private static final VoxelShape HITBOX = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);
    private static final VoxelShape COLLISION = Block.box(7.5, 0.0, 7.5, 8.5, 16.0, 8.5);

    private static final int MAX_AGE = 5;
    private static final int UPPER_AGE_THRESHOLD = 2;
    private static final int HARVEST_RESET_AGE = 2;
    private static final int NORMAL_TOMATO_DROP_COUNT = 4;
    private static final float HARVEST_YIELD_MULTIPLIER = 1.75F;
    private static final int HARVEST_COUNT = Math.round(NORMAL_TOMATO_DROP_COUNT * HARVEST_YIELD_MULTIPLIER);

    private final MapCodec<TomatoCropPoleBlock> codec;

    public TomatoCropPoleBlock(BlockBehaviour.Properties properties, VoxelShape[] shapes,
                               Supplier<? extends ItemLike> seedSupplier, Supplier<? extends ItemLike> harvestItemSupplier) {
        super(properties, MAX_AGE, shapes, seedSupplier, harvestItemSupplier, HARVEST_RESET_AGE, HARVEST_COUNT);
        this.codec = simpleCodec(props -> new TomatoCropPoleBlock(props, shapes, seedSupplier, harvestItemSupplier));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(this.getAgeProperty(), 0)
                .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public @NotNull MapCodec<TomatoCropPoleBlock> codec() {
        return this.codec;
    }

    @Override
    protected @NotNull IntegerProperty getAgeProperty() {
        return BlockStateProperties.AGE_5;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AGE_5, HALF);
    }

    public static BlockState createLower(TomatoCropPoleBlock block, int age) {
        return block.defaultBlockState()
                .setValue(BlockStateProperties.AGE_5, age)
                .setValue(HALF, DoubleBlockHalf.LOWER);
    }

    public static BlockState createUpper(TomatoCropPoleBlock block, int age) {
        return block.defaultBlockState()
                .setValue(BlockStateProperties.AGE_5, age)
                .setValue(HALF, DoubleBlockHalf.UPPER);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                           @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return HITBOX;
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                    @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (!(level instanceof LevelReader)) {
            return Shapes.empty();
        }
        return COLLISION;
    }

    @Override
    @Nullable
    protected BlockPos resolveHarvestOriginPos(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockPos lowerPos = pos.below();
            return level.getBlockState(lowerPos).is(this) ? lowerPos : null;
        }
        return pos;
    }

    @Override
    protected void afterHarvest(@NotNull ServerLevel level, @NotNull BlockPos originPos, @NotNull IntegerProperty ageProperty) {
        tryGrowOrSyncUpper(this, level, originPos, ageProperty, HARVEST_RESET_AGE);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        super.playerWillDestroy(level, pos, state, player);

        if (level.isClientSide()) {
            return state;
        }

        DoubleBlockHalf half = state.getValue(HALF);
        BlockPos otherPos = half == DoubleBlockHalf.UPPER ? pos.below() : pos.above();
        BlockState otherState = level.getBlockState(otherPos);

        if (otherState.is(this) && otherState.getValue(HALF) != half) {
            level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
            level.levelEvent(player, 2001, otherPos, Block.getId(otherState));
        }
        return state;
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState belowState = level.getBlockState(pos.below());
            return belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
        return super.canSurvive(state, level, pos);
    }

    @Override
    protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                              @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                              @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (direction.getAxis() == Direction.Axis.Y && state.getValue(HALF) == DoubleBlockHalf.UPPER && direction == Direction.DOWN) {
            if (!neighborState.is(this) || neighborState.getValue(HALF) != DoubleBlockHalf.LOWER) {
                return Blocks.AIR.defaultBlockState();
            }
            return state;
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos,
                              @NotNull RandomSource random) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return;
        }

        IntegerProperty ageProperty = this.getAgeProperty();
        int currentAge = state.getValue(ageProperty);
        if (currentAge >= MAX_AGE) {
            return;
        }
        if (random.nextInt(MAX_AGE) != 0) {
            return;
        }

        float growthSpeed = getGrowthSpeed(state, level, pos);
        if (random.nextInt((int) (25.0F / growthSpeed) + 1) != 0) {
            return;
        }

        int nextAge = currentAge + 1;
        if (!tryGrowOrSyncUpper(this, level, pos, ageProperty, nextAge)) {
            return;
        }

        level.setBlock(pos, state.setValue(ageProperty, nextAge), 2);
    }

    @Override
    public void growCrops(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            pos = pos.below();
            state = level.getBlockState(pos);
        }

        IntegerProperty ageProperty = this.getAgeProperty();
        int nextAge = Math.min(state.getValue(ageProperty) + 1, MAX_AGE);

        if (!tryGrowOrSyncUpper(this, level, pos, ageProperty, nextAge)) {
            return;
        }

        level.setBlock(pos, state.setValue(ageProperty, nextAge), 2);
    }

    static boolean tryGrowOrSyncUpper(@NotNull TomatoCropPoleBlock poleBlock, @NotNull Level level, @NotNull BlockPos lowerPos,
                                      @NotNull IntegerProperty ageProperty, int nextAge) {
        if (nextAge < UPPER_AGE_THRESHOLD) {
            return true;
        }

        BlockPos upperPos = lowerPos.above();
        BlockState upperState = level.getBlockState(upperPos);

        if (upperState.is(poleBlock) && upperState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            level.setBlock(upperPos, upperState.setValue(ageProperty, nextAge), 2);
            return true;
        }

        if (!upperState.canBeReplaced()) {
            return false;
        }

        level.setBlock(upperPos, TomatoCropPoleBlock.createUpper(poleBlock, nextAge), 3);
        return true;
    }

    @Override
    protected void spawnAfterBreak(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos,
                                   @NotNull ItemStack tool, boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, tool, dropExperience);

        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return;
        }

        popResource(level, pos, new ItemStack(Items.STICK));
    }
}