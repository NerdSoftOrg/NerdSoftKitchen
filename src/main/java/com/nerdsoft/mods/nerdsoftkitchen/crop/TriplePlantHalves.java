package com.nerdsoft.mods.nerdsoftkitchen.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    default void triplePlantSetPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
                                       @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        level.setBlock(pos.above(1), middleStateForPlacedBy(level, pos, state), 3);
        level.setBlock(pos.above(2), upperStateForPlacedBy(level, pos, state), 3);
    }
}
