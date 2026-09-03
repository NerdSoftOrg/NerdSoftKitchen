package com.panzer.mods.dice_and_delish.registry.world.worldgen;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.datagen.worldgen.ModConfiguredFeatures;
import com.panzer.mods.dice_and_delish.util.ModLogger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.neoforged.bus.api.IEventBus;

import java.util.List;

public final class ModPlacedFeatures {

    private static final int WILD_STRAWBERRY_RARITY = 24;
    private static final int WILD_TOMATO_RARITY = 28;
    private static final int WILD_LETTUCE_RARITY = 26;
    private static final int WILD_PURPLE_ONION_RARITY = 30;
    private static final int WILD_RICE_RARITY = 48;

    public static final ResourceKey<PlacedFeature> WILD_STRAWBERRY = key("wild_strawberry");
    public static final ResourceKey<PlacedFeature> WILD_TOMATO = key("wild_tomato");
    public static final ResourceKey<PlacedFeature> WILD_LETTUCE = key("wild_lettuce");
    public static final ResourceKey<PlacedFeature> WILD_PURPLE_ONION = key("wild_purple_onion");
    public static final ResourceKey<PlacedFeature> WILD_RICE = key("wild_rice");

    private ModPlacedFeatures() {
    }

    @SuppressWarnings("unused")
    public static void register(IEventBus eventBus) {
        ModLogger.info("Placed Features registered successfully.");
    }

    private static ResourceKey<PlacedFeature> key(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, name));
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, WILD_STRAWBERRY, configuredFeatures.getOrThrow(
                ModConfiguredFeatures.WILD_STRAWBERRY),
                WILD_STRAWBERRY_RARITY,
                Heightmap.Types.MOTION_BLOCKING
        );
        register(context, WILD_TOMATO, configuredFeatures.getOrThrow(
                ModConfiguredFeatures.WILD_TOMATO),
                WILD_TOMATO_RARITY,
                Heightmap.Types.MOTION_BLOCKING
        );
        register(context, WILD_LETTUCE, configuredFeatures.getOrThrow(
                ModConfiguredFeatures.WILD_LETTUCE),
                WILD_LETTUCE_RARITY,
                Heightmap.Types.MOTION_BLOCKING
        );
        register(context, WILD_PURPLE_ONION, configuredFeatures.getOrThrow(
                ModConfiguredFeatures.WILD_PURPLE_ONION),
                WILD_PURPLE_ONION_RARITY,
                Heightmap.Types.MOTION_BLOCKING
        );
        register(context, WILD_RICE, configuredFeatures.getOrThrow(
                ModConfiguredFeatures.WILD_RICE),
                WILD_RICE_RARITY,
                Heightmap.Types.WORLD_SURFACE_WG
        );
    }

    private static void register(BootstrapContext<PlacedFeature> context,
                                 ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> configuredFeature,
                                 int rarity,
                                 Heightmap.Types heightmapType) {
        List<PlacementModifier> modifiers = List.of(
                RarityFilter.onAverageOnceEvery(rarity),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(heightmapType)
        );
        context.register(key, new PlacedFeature(configuredFeature, modifiers));
    }
}
