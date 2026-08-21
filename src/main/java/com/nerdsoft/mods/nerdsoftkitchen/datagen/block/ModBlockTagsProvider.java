package com.nerdsoft.mods.nerdsoftkitchen.datagen.block;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlockTags;
import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, NerdSoftKitchen.MOD_ID, existingFileHelper);
    }

    @SuppressWarnings("SameParameterValue")
    private static TagKey<Block> cTag(String name) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(cTag("grill_tables")).add(
                ModBlocks.GRILL_TABLE.get(),
                ModBlocks.GRILL_TABLE_SOUL.get()
        );

        tag(BlockTags.CROPS).add(
                ModBlocks.WILD_STRAWBERRY.get(),
                ModBlocks.WILD_TOMATO.get(),
                ModBlocks.WILD_LETTUCE.get(),
                ModBlocks.WILD_PURPLE_ONION.get(),
                ModBlocks.WILD_RICE.get(),
                ModBlocks.STRAWBERRY_CROP.get(),
                ModBlocks.LETTUCE_CROP.get(),
                ModBlocks.PURPLE_ONION_CROP.get(),
                ModBlocks.RICE_CROP.get(),
                ModBlocks.TOMATO_CROP.get(),
                ModBlocks.TOMATO_CROP_POLE.get()
        );

        tag(cTag("crops")).add(
                ModBlocks.WILD_STRAWBERRY.get(),
                ModBlocks.WILD_TOMATO.get(),
                ModBlocks.WILD_LETTUCE.get(),
                ModBlocks.WILD_PURPLE_ONION.get(),
                ModBlocks.WILD_RICE.get(),
                ModBlocks.STRAWBERRY_CROP.get(),
                ModBlocks.LETTUCE_CROP.get(),
                ModBlocks.PURPLE_ONION_CROP.get(),
                ModBlocks.RICE_CROP.get(),
                ModBlocks.TOMATO_CROP.get(),
                ModBlocks.TOMATO_CROP_POLE.get()
        );

        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModBlocks.ORGANIC_SOIL.get(), ModBlocks.FERTILE_FARMLAND.get());

        tag(cTag("heat_sources")).add(
                Blocks.FIRE,
                Blocks.SOUL_FIRE,
                Blocks.MAGMA_BLOCK,
                Blocks.LAVA,
                Blocks.CAMPFIRE,
                Blocks.SOUL_CAMPFIRE,
                ModBlocks.GRILL_TABLE.get(),
                ModBlocks.GRILL_TABLE_SOUL.get()
        );
    }

    @SuppressWarnings("unused")
    private static TagKey<Block> create(String namespace, String path) {
        return TagKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(namespace, path));
    }
}
