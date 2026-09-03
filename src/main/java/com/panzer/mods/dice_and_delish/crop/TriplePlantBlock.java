package com.panzer.mods.dice_and_delish.crop;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class TriplePlantBlock extends BushBlock implements TriplePlantHalves {

    public static final EnumProperty<TripleBlockHalf> HALF = EnumProperty.create("half", TripleBlockHalf.class);

    private final MapCodec<? extends TriplePlantBlock> codec;

    protected TriplePlantBlock(BlockBehaviour.Properties properties, Function<BlockBehaviour.Properties, ? extends TriplePlantBlock> factory) {
        super(properties);
        this.codec = simpleCodec(factory);
        this.registerDefaultState(applyLowerDefaults(this.stateDefinition.any().setValue(HALF, TripleBlockHalf.LOWER)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull MapCodec<TriplePlantBlock> codec() {
        return (MapCodec<TriplePlantBlock>) this.codec;
    }

    @Override
    public final @NotNull EnumProperty<TripleBlockHalf> halfProperty() {
        return HALF;
    }

    protected BlockState applyLowerDefaults(BlockState lowerDefault) {
        return lowerDefault;
    }

    protected abstract boolean lowerHalfCanSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return triplePlantStateForPlacement(context);
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
                            @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        triplePlantSetPlacedBy(level, pos, state, placer, stack);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        TripleBlockHalf half = state.getValue(HALF);
        if (half == TripleBlockHalf.LOWER) {
            return lowerHalfCanSurvive(state, level, pos);
        } else if (half == TripleBlockHalf.MIDDLE) {
            BlockState below = level.getBlockState(pos.below());
            return below.is(this) && below.getValue(HALF) == TripleBlockHalf.LOWER;
        } else {
            BlockState below = level.getBlockState(pos.below());
            return below.is(this) && below.getValue(HALF) == TripleBlockHalf.MIDDLE;
        }
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        triplePlantPlayerWillDestroy(this, level, pos, state, player);

        if (state.hasProperty(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.setBlock(pos, Blocks.WATER.defaultBlockState(), 35);
        }

        return super.playerWillDestroy(level, pos, state, player);
    }
}
