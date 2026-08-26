package com.nerdsoft.mods.nerdsoftkitchen.crop;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
//? if >=1.21.2 {
/*import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ScheduledTickAccess;
*///?}
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public final class RiceCropBlock extends ModCropBlock implements TallPlantHalves {

    private static final int MAX_AGE = 3;

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private final MapCodec<RiceCropBlock> codec;

    public RiceCropBlock(BlockBehaviour.Properties properties, VoxelShape[] shapes,
                         Supplier<? extends ItemLike> seedSupplier) {
        super(properties, MAX_AGE, shapes, seedSupplier);
        this.codec = simpleCodec(props -> new RiceCropBlock(props, shapes, seedSupplier));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(getAgeProperty(), 0)
                .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public @NotNull MapCodec<RiceCropBlock> codec() {
        return this.codec;
    }

    @Override
    protected @NotNull IntegerProperty getAgeProperty() {
        return BlockStateProperties.AGE_3;
    }

    @Override
    public @NotNull EnumProperty<DoubleBlockHalf> halfProperty() {
        return HALF;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AGE_3, HALF);
    }

    @Override
    protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.is(Blocks.MUD);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return tallPlantStateForPlacement(context);
    }

    @Override
    public @NotNull BlockState lowerStateForPlacement(@NotNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public @NotNull BlockState upperStateForPlacedBy(@NotNull Level level, @NotNull BlockPos lowerPos, @NotNull BlockState lowerState) {
        return this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
                            @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        tallPlantSetPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return upperHalfCanSurvive(this, level, pos);
        }
        return super.canSurvive(state, level, pos);
    }

    @Override
    //? if <1.21.2 {
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction,
                                           @NotNull BlockState neighborState, @NotNull LevelAccessor level,
                                           @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        return tallPlantUpdateShape(this, state, direction, level, pos,
                () -> super.updateShape(state, direction, neighborState, level, pos, neighborPos));
    }
     //?} else {
    /*protected @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull LevelReader level,
                                              @NotNull ScheduledTickAccess scheduledTickAccess, @NotNull BlockPos pos,
                                              @NotNull Direction direction, @NotNull BlockPos neighborPos,
                                              @NotNull BlockState neighborState, @NotNull RandomSource random) {
        return tallPlantUpdateShape(this, state, direction, level, pos,
                () -> super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random));
    }
    *///?}

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        tallPlantPlayerWillDestroy(this, level, pos, state, player);
        return super.playerWillDestroy(level, pos, state, player);
    }
}
