package com.panzer.mods.dice_and_delish.datagen.tags;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends TagsProvider<Biome> {

    public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.BIOME, lookupProvider, DiceAndDelish.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(create("has_wild_strawberry"))
            .addTag(BiomeTags.IS_FOREST)
            .addTag(BiomeTags.IS_TAIGA)
            .addTag(Tags.Biomes.IS_PLAINS)
            .addTag(Tags.Biomes.IS_MOUNTAIN);

        tag(create("has_wild_tomato"))
            .addTag(BiomeTags.IS_SAVANNA)
            .addTag(Tags.Biomes.IS_PLAINS)
            .addTag(Tags.Biomes.IS_BADLANDS)
            .addTag(Tags.Biomes.IS_BEACH);

        tag(create("has_wild_lettuce"))
            .addTag(BiomeTags.IS_FOREST)
            .addTag(Tags.Biomes.IS_SWAMP)
            .addTag(Tags.Biomes.IS_JUNGLE)
            .addTag(Tags.Biomes.IS_PLAINS);

        tag(create("has_wild_purple_onion"))
            .addTag(BiomeTags.IS_SAVANNA)
            .addTag(Tags.Biomes.IS_PLAINS)
            .addTag(Tags.Biomes.IS_BIRCH_FOREST)
            .addTag(Tags.Biomes.IS_TAIGA);

        tag(create("has_wild_rice"))
            .addTag(Tags.Biomes.IS_SWAMP)
            .addTag(Tags.Biomes.IS_RIVER);
    }

    private static TagKey<Biome> create(String name) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, name));
    }
}
