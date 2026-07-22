package com.nerdsoft.mods.nerdsoftkitchen.datagen;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public final class ModConfiguredFeatures {

    private static final int PATCH_TRIES = 32;
    private static final int PATCH_XZ_SPREAD = 6;
    private static final int PATCH_Y_SPREAD = 2;
    private static final BlockPos BELOW = new BlockPos(0, -1, 0);

    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_STRAWBERRY = key("wild_strawberry");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_TOMATO = key("wild_tomato");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_LETTUCE = key("wild_lettuce");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILD_PURPLE_ONION = key("wild_purple_onion");

    private ModConfiguredFeatures() {
    }

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, name));
    }

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, WILD_STRAWBERRY, ModBlocks.WILD_STRAWBERRY.get());
        register(context, WILD_TOMATO, ModBlocks.WILD_TOMATO.get());
        register(context, WILD_LETTUCE, ModBlocks.WILD_LETTUCE.get());
        register(context, WILD_PURPLE_ONION, ModBlocks.WILD_PURPLE_ONION.get());
    }

    private static void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, Block block) {
        BlockPredicate plantedOnValidGround = BlockPredicate.allOf(
                BlockPredicate.ONLY_IN_AIR_PREDICATE,
                BlockPredicate.matchesTag(BELOW, BlockTags.DIRT)
        );

        context.register(key, new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(
                PATCH_TRIES, PATCH_XZ_SPREAD, PATCH_Y_SPREAD,
                PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)), plantedOnValidGround)
        )));
    }
}