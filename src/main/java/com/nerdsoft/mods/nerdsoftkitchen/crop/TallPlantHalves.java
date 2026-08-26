package com.nerdsoft.mods.nerdsoftkitchen.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
                                                     @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Supplier<BlockState> fallbackSuper) {
        if (direction != Direction.DOWN || state.canSurvive(level, pos)) {
            return fallbackSuper.get();
        }

        if (level instanceof Level activeLevel && !activeLevel.isClientSide()) {
            DoubleBlockHalf half = state.getValue(halfProperty());
            activeLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
            activeLevel.levelEvent(2001, pos, Block.getId(state));

            BlockPos otherPos = half == DoubleBlockHalf.UPPER ? pos.below() : pos.above();
            BlockState otherState = activeLevel.getBlockState(otherPos);
            if (otherState.is(self) && otherState.getValue(halfProperty()) != half) {
                activeLevel.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
                activeLevel.levelEvent(2001, otherPos, Block.getId(otherState));
            }
        }
        return state;
    }

    default void tallPlantPlayerWillDestroy(@NotNull Block self, @NotNull Level level, @NotNull BlockPos pos,
                                            @NotNull BlockState state, @NotNull Player player) {
        if (level.isClientSide()) {
            return;
        }

        DoubleBlockHalf half = state.getValue(halfProperty());
        BlockPos otherPos = half == DoubleBlockHalf.UPPER ? pos.below() : pos.above();
        BlockState otherState = level.getBlockState(otherPos);

        if (otherState.is(self) && otherState.getValue(halfProperty()) != half) {
            level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
            level.levelEvent(player, 2001, otherPos, Block.getId(otherState));
        }
    }
}
