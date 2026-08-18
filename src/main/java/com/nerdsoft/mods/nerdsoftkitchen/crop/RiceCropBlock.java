package com.nerdsoft.mods.nerdsoftkitchen.crop;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class RiceCropBlock extends ModCropBlock {

    private static final int MAX_AGE = 3;

    private final MapCodec<RiceCropBlock> codec;

    public RiceCropBlock(BlockBehaviour.Properties properties, VoxelShape[] shapes,
                         Supplier<? extends ItemLike> seedSupplier) {
        super(properties, MAX_AGE, shapes, seedSupplier);
        this.codec = simpleCodec(props -> new RiceCropBlock(props, shapes, seedSupplier));
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AGE_3);
    }

    @Override
    protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.is(Blocks.MUD);
    }
}