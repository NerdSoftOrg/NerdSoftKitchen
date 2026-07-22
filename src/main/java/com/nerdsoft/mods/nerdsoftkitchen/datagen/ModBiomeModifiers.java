package com.nerdsoft.mods.nerdsoftkitchen.datagen;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModPlacedFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModBiomeModifiers {

    private ModBiomeModifiers() {
    }

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        registerForTags(context, biomes, placedFeatures, "wild_strawberry", ModPlacedFeatures.WILD_STRAWBERRY,
                Tags.Biomes.IS_FOREST, Tags.Biomes.IS_PLAINS);

        registerForTags(context, biomes, placedFeatures, "wild_tomato", ModPlacedFeatures.WILD_TOMATO,
                Tags.Biomes.IS_SAVANNA, Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_BADLANDS);

        registerForTags(context, biomes, placedFeatures, "wild_lettuce", ModPlacedFeatures.WILD_LETTUCE,
                Tags.Biomes.IS_FOREST, Tags.Biomes.IS_SWAMP, Tags.Biomes.IS_JUNGLE);

        registerForTags(context, biomes, placedFeatures, "wild_purple_onion", ModPlacedFeatures.WILD_PURPLE_ONION,
                Tags.Biomes.IS_SAVANNA, Tags.Biomes.IS_PLAINS);
    }

    @SafeVarargs
    private static void registerForTags(BootstrapContext<BiomeModifier> context, HolderGetter<Biome> biomes,
                                        HolderGetter<PlacedFeature> placedFeatures, String namePrefix,
                                        ResourceKey<PlacedFeature> placedFeature, TagKey<Biome>... biomeTags) {
        HolderSet<PlacedFeature> feature = HolderSet.direct(placedFeatures.getOrThrow(placedFeature));

        for (TagKey<Biome> biomeTag : biomeTags) {
            String name = namePrefix + "_" + biomeTag.location().getPath();
            context.register(key(name), new BiomeModifiers.AddFeaturesBiomeModifier(
                    biomes.getOrThrow(biomeTag),
                    feature,
                    GenerationStep.Decoration.VEGETAL_DECORATION
            ));
        }
    }

    private static ResourceKey<BiomeModifier> key(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, name));
    }
}