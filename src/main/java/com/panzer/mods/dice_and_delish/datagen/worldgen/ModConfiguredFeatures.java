package com.panzer.mods.dice_and_delish.datagen.worldgen;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.crop.TripleBlockHalf;
import com.panzer.mods.dice_and_delish.crop.WildRiceBlock;
import com.panzer.mods.dice_and_delish.registry.block.ModBlocks;
import com.panzer.mods.dice_and_delish.registry.world.worldgen.ModFeatures;
import com.panzer.mods.dice_and_delish.worldgen.feature.TriplePlantFeature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.material.Fluids;

public final class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_STRAWBERRY = key("wild_strawberry");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_TOMATO = key("wild_tomato");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_LETTUCE = key("wild_lettuce");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_PURPLE_ONION = key("wild_purple_onion");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_RICE = key("wild_rice");

    private static final int DEFAULT_PATCH_TRIES = 32;
    private static final int DEFAULT_PATCH_XZ_SPREAD = 6;
    private static final int DEFAULT_PATCH_Y_SPREAD = 2;

    private static final int WILD_RICE_PATCH_TRIES = 512;
    private static final int WILD_RICE_PATCH_XZ_SPREAD = 32;

    private ModConfiguredFeatures() {
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, name));
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        registerPatch(context, WILD_STRAWBERRY, ModBlocks.WILD_STRAWBERRY.get());
        registerPatch(context, WILD_TOMATO, ModBlocks.WILD_TOMATO.get());
        registerPatch(context, WILD_LETTUCE, ModBlocks.WILD_LETTUCE.get());
        registerPatch(context, WILD_PURPLE_ONION, ModBlocks.WILD_PURPLE_ONION.get());

        BlockState wildRiceLowerState = ModBlocks.WILD_RICE.get().defaultBlockState()
                .setValue(WildRiceBlock.HALF, TripleBlockHalf.LOWER)
                .setValue(WildRiceBlock.WATERLOGGED, true);

        TriplePlantFeature.Configuration triplePlantConfig = new TriplePlantFeature.Configuration(
                BlockStateProvider.simple(wildRiceLowerState)
        );

        register(context, WILD_RICE, Feature.RANDOM_PATCH,
                createTriplePlantWaterPatch(
                        triplePlantConfig,
                        WILD_RICE_PATCH_TRIES,
                        WILD_RICE_PATCH_XZ_SPREAD,
                        DEFAULT_PATCH_Y_SPREAD
                )
        );
    }

    private static void registerPatch(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, Block block) {
        RandomPatchConfiguration config = new RandomPatchConfiguration(
                DEFAULT_PATCH_TRIES,
                DEFAULT_PATCH_XZ_SPREAD,
                DEFAULT_PATCH_Y_SPREAD,
                PlacementUtils.filtered(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(block)),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE
                )
        );
        register(context, key, Feature.RANDOM_PATCH, config);
    }

    @SuppressWarnings("SameParameterValue")
    private static RandomPatchConfiguration createTriplePlantWaterPatch(TriplePlantFeature.Configuration triplePlantConfig, int tries, int xzSpread, int ySpread) {
        return new RandomPatchConfiguration(tries, xzSpread, ySpread,
                PlacementUtils.filtered(
                        ModFeatures.TRIPLE_PLANT.get(),
                        triplePlantConfig,
                        BlockPredicate.allOf(
                                BlockPredicate.matchesFluids(Fluids.WATER), // LOWER
                                BlockPredicate.matchesBlocks(new BlockPos(0, 1, 0), Blocks.AIR), // MIDDLE
                                BlockPredicate.matchesBlocks(new BlockPos(0, 2, 0), Blocks.AIR), // UPPER
                                BlockPredicate.matchesTag(new BlockPos(0, -1, 0), BlockTags.DIRT) // FLOOR
                        )
                )
        );
    }

    @SuppressWarnings("SameParameterValue")
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            F feature,
            FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }
}
