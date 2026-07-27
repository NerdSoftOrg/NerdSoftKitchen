package com.nerdsoft.mods.nerdsoftkitchen.crop;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public abstract class ModCropBlock extends CropBlock {

    public static final VoxelShape[] SHAPES_AGE_3 = new VoxelShape[]{
            Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0)
    };

    public static final VoxelShape[] SHAPES_AGE_5 = new VoxelShape[]{
            Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0)
    };

    private final int maxAge;
    private final VoxelShape[] shapes;
    private final Supplier<? extends ItemLike> seedSupplier;

    protected ModCropBlock(BlockBehaviour.Properties properties, int maxAge, VoxelShape[] shapes,
                           Supplier<? extends ItemLike> seedSupplier) {
        super(properties);
        this.maxAge = maxAge;
        this.shapes = shapes;
        this.seedSupplier = seedSupplier;

        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0));
    }

    @Override
    public final int getMaxAge() {
        return this.maxAge;
    }

    @SuppressWarnings("unused")
    protected int getEffectiveMaxAge(@NotNull BlockState state) {
        return this.maxAge;
    }

    @Override
    protected final @NotNull ItemLike getBaseSeedId() {
        return this.seedSupplier.get();
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                           @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.shapes[Math.min(this.shapes.length - 1, state.getValue(this.getAgeProperty()))];
    }

    public static final class Ages extends ModCropBlock {
        private static final IntegerProperty[] AGE_PROPERTIES = buildAgeProperties();
        private static int bootstrapMaxAge = -1;

        private final IntegerProperty ageProperty;
        private final MapCodec<Ages> codec;

        public Ages(BlockBehaviour.Properties properties, int maxAge, VoxelShape[] shapes,
                    Supplier<? extends ItemLike> seedSupplier) {
            super(properties, bootstrap(maxAge), shapes, seedSupplier);
            this.ageProperty = AGE_PROPERTIES[maxAge];
            this.codec = simpleCodec(props -> new Ages(props, maxAge, shapes, seedSupplier));
        }

        private static int bootstrap(int maxAge) {
            bootstrapMaxAge = maxAge;
            return maxAge;
        }

        private static IntegerProperty[] buildAgeProperties() {
            IntegerProperty[] properties = new IntegerProperty[8];
            properties[3] = BlockStateProperties.AGE_3;
            properties[4] = BlockStateProperties.AGE_4;
            properties[5] = BlockStateProperties.AGE_5;
            properties[7] = BlockStateProperties.AGE_7;
            return properties;
        }

        @Override
        public @NotNull MapCodec<Ages> codec() {
            return this.codec;
        }

        @Override
        protected @NotNull IntegerProperty getAgeProperty() {
            return this.ageProperty != null ? this.ageProperty : AGE_PROPERTIES[bootstrapMaxAge];
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(this.getAgeProperty());
        }
    }
}