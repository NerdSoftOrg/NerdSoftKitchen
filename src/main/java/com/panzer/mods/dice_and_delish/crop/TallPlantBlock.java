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
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public abstract class TallPlantBlock extends BushBlock implements TallPlantHalves {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private final MapCodec<? extends TallPlantBlock> codec;

    protected TallPlantBlock(BlockBehaviour.Properties properties, Function<BlockBehaviour.Properties, ? extends TallPlantBlock> factory) {
        super(properties);
        this.codec = simpleCodec(factory::apply);
        this.registerDefaultState(applyLowerDefaults(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull MapCodec<TallPlantBlock> codec() {
        return (MapCodec<TallPlantBlock>) this.codec;
    }

    @Override
    public final @NotNull EnumProperty<DoubleBlockHalf> halfProperty() {
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
        return tallPlantStateForPlacement(context);
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
        return lowerHalfCanSurvive(state, level, pos);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        tallPlantPlayerWillDestroy(this, level, pos, state, player);
        return super.playerWillDestroy(level, pos, state, player);
    }
}
