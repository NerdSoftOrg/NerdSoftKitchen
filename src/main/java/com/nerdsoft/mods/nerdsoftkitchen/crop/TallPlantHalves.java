package com.nerdsoft.mods.nerdsoftkitchen.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;


public interface TallPlantHalves {

    EnumProperty<DoubleBlockHalf> halfProperty();

    @NotNull BlockState lowerStateForPlacement(@NotNull BlockPlaceContext context);

    @NotNull BlockState upperStateForPlacedBy(@NotNull Level level, @NotNull BlockPos lowerPos, @NotNull BlockState lowerState);

    @Nullable
    default BlockState tallPlantStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        //? if <1.21.2 {
        if (pos.getY() >= level.getMaxBuildHeight() - 1 || !level.getBlockState(pos.above()).canBeReplaced(context)) {
        //?} else {
        /*if (pos.getY() >= level.getMaxY() - 1 || !level.getBlockState(pos.above()).canBeReplaced(context)) {
        *///?}
            return null;
        }
        return lowerStateForPlacement(context);
    }

    default void tallPlantSetPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
                                      @SuppressWarnings("unused") @Nullable LivingEntity placer, @SuppressWarnings("unused") @NotNull ItemStack stack) {
        BlockPos abovePos = pos.above();
        level.setBlock(abovePos, upperStateForPlacedBy(level, pos, state), 3);
    }

    default boolean upperHalfCanSurvive(@NotNull Block self, @NotNull LevelReader level, @NotNull BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(self) && below.getValue(halfProperty()) == DoubleBlockHalf.LOWER;
    }

    default @NotNull BlockState tallPlantUpdateShape(@NotNull Block self, @NotNull BlockState state, @NotNull Direction direction,
                                                     @NotNull BlockState neighborState, @SuppressWarnings("unused") @NotNull BlockPos pos,
                                                     @NotNull Supplier<Boolean> lowerCanSurviveHere, @NotNull Supplier<BlockState> fallbackSuper) {
        DoubleBlockHalf half = state.getValue(halfProperty());
        if (direction.getAxis() != Direction.Axis.Y
                || (half == DoubleBlockHalf.LOWER) != (direction == Direction.UP)
                || (neighborState.is(self) && neighborState.getValue(halfProperty()) != half)) {
            return half == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !lowerCanSurviveHere.get()
                    ? Blocks.AIR.defaultBlockState()
                    : fallbackSuper.get();
        }
        return Blocks.AIR.defaultBlockState();
    }
}
