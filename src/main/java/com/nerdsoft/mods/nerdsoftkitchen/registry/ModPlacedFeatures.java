package com.nerdsoft.mods.nerdsoftkitchen.registry;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.ModConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public final class ModPlacedFeatures {

    private static final int WILD_STRAWBERRY_RARITY = 24;
    private static final int WILD_TOMATO_RARITY = 28;
    private static final int WILD_LETTUCE_RARITY = 26;
    private static final int WILD_PURPLE_ONION_RARITY = 30;

    public static final ResourceKey<PlacedFeature> WILD_STRAWBERRY = key("wild_strawberry");
    public static final ResourceKey<PlacedFeature> WILD_TOMATO = key("wild_tomato");
    public static final ResourceKey<PlacedFeature> WILD_LETTUCE = key("wild_lettuce");
    public static final ResourceKey<PlacedFeature> WILD_PURPLE_ONION = key("wild_purple_onion");

    private ModPlacedFeatures() {
    }

    private static ResourceKey<PlacedFeature> key(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, name));
    }

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, WILD_STRAWBERRY, configuredFeatures.getOrThrow(ModConfiguredFeatures.WILD_STRAWBERRY), WILD_STRAWBERRY_RARITY);
        register(context, WILD_TOMATO, configuredFeatures.getOrThrow(ModConfiguredFeatures.WILD_TOMATO), WILD_TOMATO_RARITY);
        register(context, WILD_LETTUCE, configuredFeatures.getOrThrow(ModConfiguredFeatures.WILD_LETTUCE), WILD_LETTUCE_RARITY);
        register(context, WILD_PURPLE_ONION, configuredFeatures.getOrThrow(ModConfiguredFeatures.WILD_PURPLE_ONION), WILD_PURPLE_ONION_RARITY);
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> configuredFeature, int rarity) {
        List<PlacementModifier> modifiers = List.of(
                RarityFilter.onAverageOnceEvery(rarity),
                InSquarePlacement.spread(),
                HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                BiomeFilter.biome()
        );
        context.register(key, new PlacedFeature(configuredFeature, modifiers));
    }
}