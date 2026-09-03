package com.panzer.mods.dice_and_delish.datagen.worldgen;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.registry.world.worldgen.ModPlacedFeatures;
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
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModBiomeModifiers {

    private ModBiomeModifiers() {}

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        registerFeature(context, biomes, placedFeatures, "wild_strawberry",
                ModPlacedFeatures.WILD_STRAWBERRY,
                createTag("has_wild_strawberry"));

        registerFeature(context, biomes, placedFeatures, "wild_tomato",
                ModPlacedFeatures.WILD_TOMATO,
                createTag("has_wild_tomato"));

        registerFeature(context, biomes, placedFeatures, "wild_lettuce",
                ModPlacedFeatures.WILD_LETTUCE,
                createTag("has_wild_lettuce"));

        registerFeature(context, biomes, placedFeatures, "wild_purple_onion",
                ModPlacedFeatures.WILD_PURPLE_ONION,
                createTag("has_wild_purple_onion"));

        registerFeature(context, biomes, placedFeatures, "wild_rice",
                ModPlacedFeatures.WILD_RICE,
                createTag("has_wild_rice"));
    }

    private static void registerFeature(BootstrapContext<BiomeModifier> context,
                                        HolderGetter<Biome> biomes,
                                        HolderGetter<PlacedFeature> placedFeatures,
                                        String baseName,
                                        ResourceKey<PlacedFeature> placedFeatureKey,
                                        TagKey<Biome> biomeTag) {
        HolderSet<PlacedFeature> featureHolderSet = HolderSet.direct(placedFeatures.getOrThrow(placedFeatureKey));

        context.register(
                key("add_" + baseName),
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(biomeTag),
                        featureHolderSet,
                        GenerationStep.Decoration.VEGETAL_DECORATION
                )
        );
    }

    private static TagKey<Biome> createTag(String name) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, name));
    }

    private static ResourceKey<BiomeModifier> key(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, name));
    }
}
