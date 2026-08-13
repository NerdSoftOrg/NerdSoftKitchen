package com.nerdsoft.mods.nerdsoftkitchen.datagen.item;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModTiers;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItemTags;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags,
                               ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, NerdSoftKitchen.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(Tags.Items.CROPS).add(
                ModItems.STRAWBERRY.get(),
                ModItems.TOMATO.get(),
                ModItems.LETTUCE.get(),
                ModItems.PURPLE_ONION.get()
        );

        tag(Tags.Items.SEEDS).add(
                ModItems.STRAWBERRY_SEEDS.get(),
                ModItems.TOMATO_SEEDS.get(),
                ModItems.LETTUCE_SEEDS.get(),
                ModItems.PURPLE_ONION_SEEDS.get()
        );

        tag(Tags.Items.FOODS).add(
                ModItems.STRAWBERRY.get(),
                ModItems.TOMATO.get(),
                ModItems.LETTUCE.get(),
                ModItems.PURPLE_ONION.get(),
                ModItems.WILD_TOMATO.get(),
                ModItems.SALAD.get(),
                ModItems.RAW_CHICKEN_PIECES.get(),
                ModItems.COOKED_CHICKEN_PIECES.get(),
                ModItems.FRIED_EGG.get(),
                ModItems.CHEESE.get(),
                ModItems.CHEESE_SLICE.get(),
                ModItems.CHEESE_SANDWICH.get(),
                ModItems.GRILLED_CHEESE.get()
        );

        tag(ModItemTags.IRON_CUP).add(ModItems.IRON_CUP.get());

        tag(ModItemTags.KNIFE).add(
                ModItems.STONE_KNIFE.get(),
                ModItems.IRON_KNIFE.get(),
                ModItems.GOLD_KNIFE.get(),
                ModItems.DIAMOND_KNIFE.get(),
                ModItems.OBSIDIAN_KNIFE.get(),
                ModItems.NETHERITE_KNIFE.get()
        );

        tag(ModTiers.REPAIRS_OBSIDIAN_KNIFE).add(Items.OBSIDIAN);
    }
}