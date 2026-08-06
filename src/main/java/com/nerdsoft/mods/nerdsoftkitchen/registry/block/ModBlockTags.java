package com.nerdsoft.mods.nerdsoftkitchen.registry.block;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> INCORRECT_FOR_OBSIDIAN_TOOL = create("incorrect_for_obsidian_tool");

    private ModBlockTags() {
    }

    @SuppressWarnings("SameParameterValue")
    private static TagKey<Block> create(String name) {
        return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, name));
    }

    public static TagKey<Block> create(ResourceLocation name) {
        return TagKey.create(Registries.BLOCK, name);
    }
}
