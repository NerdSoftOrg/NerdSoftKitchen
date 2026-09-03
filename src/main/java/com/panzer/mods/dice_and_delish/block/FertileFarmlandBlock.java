package com.panzer.mods.dice_and_delish.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.NotNull;

public class FertileFarmlandBlock extends FarmBlock {

    public static final int MAX_FERTILITY = 3;
    public static final IntegerProperty FERTILITY = IntegerProperty.create("fertility", 0, MAX_FERTILITY);

    private static final int[] GROWTH_ATTEMPTS_PER_LEVEL = {0, 1, 2, 3};

    public FertileFarmlandBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(BlockStateProperties.MOISTURE, 0)
                        .setValue(FERTILITY, MAX_FERTILITY)
        );
    }

    @Override
    public @NotNull MapCodec<FarmBlock> codec() {
        return simpleCodec(FertileFarmlandBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FERTILITY);
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        super.randomTick(state, level, pos, random);

        int fertility = state.getValue(FERTILITY);
        if (fertility <= 0) {
            return;
        }

        BlockState cropState = level.getBlockState(pos.above());
        if (!(cropState.getBlock() instanceof BonemealableBlock crop)) {
            return;
        }
        if (!crop.isValidBonemealTarget(level, pos.above(), cropState) || !crop.isBonemealSuccess(level, random, pos.above(), cropState)) {
            return;
        }

        int attempts = GROWTH_ATTEMPTS_PER_LEVEL[Math.min(fertility, GROWTH_ATTEMPTS_PER_LEVEL.length - 1)];
        for (int i = 0; i < attempts; i++) {
            if (random.nextInt(3) != 0) {
                continue;
            }
            crop.performBonemeal(level, random, pos.above(), level.getBlockState(pos.above()));
        }
    }

    public void notifyHarvested(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(this)) {
            return;
        }
        int fertility = state.getValue(FERTILITY);
        if (fertility <= 1) {
            level.setBlock(pos, Blocks.FARMLAND.defaultBlockState().setValue(BlockStateProperties.MOISTURE, state.getValue(BlockStateProperties.MOISTURE)), 3);
            return;
        }
        level.setBlock(pos, state.setValue(FERTILITY, fertility - 1), 3);
    }

    @SuppressWarnings("NonStrictComparisonCanBeEquality")
    public static int bonusDropsFor(BlockGetter level, BlockPos farmlandPos) {
        BlockState state = level.getBlockState(farmlandPos);
        if (!(state.getBlock() instanceof FertileFarmlandBlock)) {
            return 0;
        }
        int fertility = state.getValue(FERTILITY);
        if (fertility >= MAX_FERTILITY) {
            return 2;
        }
        if (fertility >= MAX_FERTILITY - 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public void fallOn(Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float fallDistance) {
        if (!level.isClientSide) {
            //? if >=1.21.3 {
            /*boolean trampling = CommonHooks.onFarmlandTrample((ServerLevel) level, pos, Blocks.DIRT.defaultBlockState(), fallDistance, entity);
            *///?} else {
            boolean trampling = CommonHooks.onFarmlandTrample(level, pos, Blocks.DIRT.defaultBlockState(), fallDistance, entity);
             //?}

            if (trampling) {
                degradeFertility(level, pos, state);
            }
        }

        entity.causeFallDamage(fallDistance, 1.0F, level.damageSources().fall());
    }

    private void degradeFertility(Level level, BlockPos pos, BlockState state) {
        int fertility = state.getValue(FERTILITY);
        if (fertility <= 1) {
            level.setBlock(pos, Blocks.FARMLAND.defaultBlockState()
                    .setValue(BlockStateProperties.MOISTURE, state.getValue(BlockStateProperties.MOISTURE)), 3);
        } else {
            level.setBlock(pos, state.setValue(FERTILITY, fertility - 1), 3);
        }
    }
}
