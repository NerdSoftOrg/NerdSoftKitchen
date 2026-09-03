package com.panzer.mods.dice_and_delish.registry.tags;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class ModBlockTags {

    private ModBlockTags() {}

    public static final TagKey<Block> HEAT_SOURCES = mod("heat_sources");
    public static final TagKey<Block> CROPS = mod("crops");
    public static final TagKey<Block> WILD_CROPS = mod("wild_crops");
    public static final TagKey<Block> TALL_PLANTS = mod("tall_plants");
    public static final TagKey<Block> TRIPLE_PLANTS = mod("triple_plants");
    public static final TagKey<Block> HARVESTABLE_CROPS = mod("harvestable_crops");

    public static final TagKey<Block> C_HEAT_SOURCES = c("heat_sources");
    public static final TagKey<Block> C_CROPS = c("crops");
    public static final TagKey<Block> C_WILD_CROPS = c("wild_crops");
    public static final TagKey<Block> C_GRILL_TABLES = c("grill_tables");
    public static final TagKey<Block> C_TALL_PLANTS = c("tall_plants");
    public static final TagKey<Block> C_TRIPLE_PLANTS = c("triple_plants");
    public static final TagKey<Block> C_HARVESTABLE_CROPS = c("harvestable_crops");

    public static TagKey<Block> mod(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, path));
    }

    public static TagKey<Block> c(String path) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", path));
    }

    public static TagKey<Block> create(ResourceLocation id) {
        return TagKey.create(Registries.BLOCK, id);
    }
}
