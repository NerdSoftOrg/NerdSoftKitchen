package com.panzer.mods.dice_and_delish.crop;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class WildCropBlock extends BushBlock {
    public static final VoxelShape DEFAULT_WILD_SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0);

    private final MapCodec<? extends WildCropBlock> codec;
    private final VoxelShape shape;

    public WildCropBlock(BlockBehaviour.Properties properties, VoxelShape shape) {
        super(properties);
        this.shape = shape;
        this.codec = simpleCodec(props -> new WildCropBlock(props, shape));
    }

    @Override
    public @NotNull MapCodec<? extends WildCropBlock> codec() {
        return this.codec;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                           @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.shape;
    }
}
