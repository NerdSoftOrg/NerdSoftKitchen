package com.panzer.mods.dice_and_delish.crop;

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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface TriplePlantHalves {

    EnumProperty<TripleBlockHalf> halfProperty();

    @NotNull BlockState lowerStateForPlacement(@NotNull BlockPlaceContext context);

    @NotNull BlockState middleStateForPlacedBy(@NotNull Level level, @NotNull BlockPos lowerPos, @NotNull BlockState lowerState);

    @NotNull BlockState upperStateForPlacedBy(@NotNull Level level, @NotNull BlockPos lowerPos, @NotNull BlockState lowerState);

    @Nullable
    default BlockState triplePlantStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        //? if <1.21.2 {
        if (pos.getY() >= level.getMaxBuildHeight() - 2 ||
                !level.getBlockState(pos.above(1)).canBeReplaced(context) ||
                !level.getBlockState(pos.above(2)).canBeReplaced(context)) {
            //?} else {
        /*if (pos.getY() >= level.getMaxY() - 2 ||
                !level.getBlockState(pos.above(1)).canBeReplaced(context) ||
                !level.getBlockState(pos.above(2)).canBeReplaced(context)) {
            *///?}
            return null;
        }
        return lowerStateForPlacement(context);
    }

    @SuppressWarnings("unused")
    default void triplePlantSetPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
                                        @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        level.setBlock(pos.above(1), middleStateForPlacedBy(level, pos, state), 3);
        level.setBlock(pos.above(2), upperStateForPlacedBy(level, pos, state), 3);
    }

    default @NotNull BlockState triplePlantUpdateShape(@NotNull Block self, @NotNull BlockState state, @NotNull Direction direction,
                                                       @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Supplier<BlockState> fallbackSuper) {
        if (direction != Direction.DOWN || state.canSurvive(level, pos)) {
            return fallbackSuper.get();
        }

        if (level instanceof Level activeLevel && !activeLevel.isClientSide()) {
            TripleBlockHalf half = state.getValue(halfProperty());
            removeBlockPreservingWater(activeLevel, pos, state);
            activeLevel.levelEvent(2001, pos, Block.getId(state));

            if (half != TripleBlockHalf.UPPER) {
                BlockPos abovePos = pos.above();
                BlockState aboveState = activeLevel.getBlockState(abovePos);
                while (aboveState.is(self) && aboveState.getValue(halfProperty()) != TripleBlockHalf.LOWER) {
                    removeBlockPreservingWater(activeLevel, abovePos, aboveState);
                    activeLevel.levelEvent(2001, abovePos, Block.getId(aboveState));
                    abovePos = abovePos.above();
                    aboveState = activeLevel.getBlockState(abovePos);
                }
            }
        }
        return state;
    }

    default void triplePlantPlayerWillDestroy(@NotNull Block self, @NotNull Level level, @NotNull BlockPos pos,
                                              @NotNull BlockState state, @NotNull Player player) {
        if (level.isClientSide()) {
            return;
        }

        TripleBlockHalf half = state.getValue(halfProperty());

        if (half != TripleBlockHalf.LOWER) {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            while (belowState.is(self) && belowState.getValue(halfProperty()) != TripleBlockHalf.UPPER) {
                removeBlockPreservingWater(level, belowPos, belowState);
                level.levelEvent(player, 2001, belowPos, Block.getId(belowState));
                belowPos = belowPos.below();
                belowState = level.getBlockState(belowPos);
            }
        }

        if (half != TripleBlockHalf.UPPER) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            while (aboveState.is(self) && aboveState.getValue(halfProperty()) != TripleBlockHalf.LOWER) {
                removeBlockPreservingWater(level, abovePos, aboveState);
                level.levelEvent(player, 2001, abovePos, Block.getId(aboveState));
                abovePos = abovePos.above();
                aboveState = level.getBlockState(abovePos);
            }
        }
    }

    private static void removeBlockPreservingWater(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
        boolean isWaterlogged = state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED);
        BlockState replacement = isWaterlogged ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
        level.setBlock(pos, replacement, 35);
    }
}
