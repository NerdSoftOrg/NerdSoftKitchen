package com.panzer.mods.dice_and_delish.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.panzer.mods.dice_and_delish.crop.TriplePlantHalves;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.jetbrains.annotations.NotNull;

public class TriplePlantFeature extends Feature<TriplePlantFeature.Configuration> {

    public TriplePlantFeature(Codec<Configuration> codec) {
        super(codec);
    }

    @Override
    public boolean place(@NotNull FeaturePlaceContext<Configuration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        Configuration config = context.config();

        BlockState lowerState = config.stateProvider().getState(random, origin);
        if (!(lowerState.getBlock() instanceof TriplePlantHalves triplePlant)) {
            level.setBlock(origin, lowerState, Block.UPDATE_CLIENTS);
            return true;
        }

        BlockPos middlePos = origin.above(1);
        BlockPos upperPos = origin.above(2);

        if (!level.getBlockState(middlePos).canBeReplaced() || !level.getBlockState(upperPos).canBeReplaced()) {
            return false;
        }

        int flags = Block.UPDATE_CLIENTS | Block.UPDATE_INVISIBLE;

        level.setBlock(origin, lowerState, flags);
        level.setBlock(middlePos, triplePlant.middleStateForPlacedBy(level.getLevel(), origin, lowerState), flags);
        level.setBlock(upperPos, triplePlant.upperStateForPlacedBy(level.getLevel(), origin, lowerState), flags);

        return true;
    }

    public record Configuration(BlockStateProvider stateProvider) implements FeatureConfiguration {
        public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockStateProvider.CODEC.fieldOf("state").forGetter(Configuration::stateProvider)
        ).apply(instance, Configuration::new));
    }
}
