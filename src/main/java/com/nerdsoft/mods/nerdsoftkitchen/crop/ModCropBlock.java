package com.nerdsoft.mods.nerdsoftkitchen.crop;

import com.mojang.serialization.MapCodec;
import com.nerdsoft.mods.nerdsoftkitchen.block.FertileFarmlandBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
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
    public int getMaxAge() {
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

    @Override
    protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return super.mayPlaceOn(state, level, pos)
                || state.getBlock() instanceof FertileFarmlandBlock
                || state.is(Blocks.MUD);
    }

    public static final class Ages extends ModCropBlock {

        private static final ThreadLocal<Integer> PENDING_MAX_AGE = new ThreadLocal<>();

        private final IntegerProperty ageProperty;
        private final MapCodec<Ages> codec;

        public Ages(BlockBehaviour.Properties properties, int maxAge, VoxelShape[] shapes,
                    Supplier<? extends ItemLike> seedSupplier) {
            // super() -> createBlockStateDefinition()
            this(properties, maxAge, shapes, seedSupplier, setPendingMaxAge(maxAge));
        }

        private Ages(BlockBehaviour.Properties properties, int maxAge, VoxelShape[] shapes,
                     Supplier<? extends ItemLike> seedSupplier, IntegerProperty ageProperty) {
            super(properties, maxAge, shapes, seedSupplier);
            this.ageProperty = ageProperty;
            this.codec = simpleCodec(props -> new Ages(props, maxAge, shapes, seedSupplier));
            PENDING_MAX_AGE.remove();
        }

        private static IntegerProperty setPendingMaxAge(int maxAge) {
            PENDING_MAX_AGE.set(maxAge);
            return resolveAgeProperty(maxAge);
        }

        private static IntegerProperty resolveAgeProperty(int maxAge) {
            return switch (maxAge) {
                case 3 -> BlockStateProperties.AGE_3;
                case 4 -> BlockStateProperties.AGE_4;
                case 5 -> BlockStateProperties.AGE_5;
                case 7 -> BlockStateProperties.AGE_7;
                default -> throw new IllegalArgumentException("Unsupported crop maxAge: " + maxAge);
            };
        }

        @Override
        public @NotNull MapCodec<Ages> codec() {
            return this.codec;
        }

        @Override
        protected @NotNull IntegerProperty getAgeProperty() {
            if (this.ageProperty != null) {
                return this.ageProperty;
            }

            int ageToResolve = Objects.requireNonNullElseGet(PENDING_MAX_AGE.get(), this::getMaxAge);
            return resolveAgeProperty(ageToResolve);
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(this.getAgeProperty());
        }
    }
}
