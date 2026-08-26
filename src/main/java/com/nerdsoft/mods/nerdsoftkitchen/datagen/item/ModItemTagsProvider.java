package com.nerdsoft.mods.nerdsoftkitchen.datagen.item;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.item.SeedItem;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModTiers;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItemTags;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags,
                               ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, NerdSoftKitchen.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        Item[] foods = ModItems.ITEMS.getEntries().stream()
                .map(DeferredHolder::get)
                .filter(item -> item.components().has(DataComponents.FOOD))
                .toArray(Item[]::new);

        tag(Tags.Items.FOODS).add(foods);

        Item[] seeds = ModItems.ITEMS.getEntries().stream()
                .map(DeferredHolder::get)
                .filter(item -> item instanceof SeedItem)
                .toArray(Item[]::new);

        tag(Tags.Items.SEEDS).add(seeds);

        tag(Tags.Items.CROPS).add(
                ModItems.STRAWBERRY.get(),
                ModItems.TOMATO.get(),
                ModItems.LETTUCE.get(),
                ModItems.PURPLE_ONION.get(),
                ModItems.RICE.get()
        );

        Item[] knivesOrdered = {
                ModItems.STONE_KNIFE.get(),
                ModItems.IRON_KNIFE.get(),
                ModItems.GOLDEN_KNIFE.get(),
                ModItems.DIAMOND_KNIFE.get(),
                ModItems.OBSIDIAN_KNIFE.get(),
                ModItems.NETHERITE_KNIFE.get()
        };

        tag(ModItemTags.KNIFE).add(knivesOrdered);

        tag(ModItemTags.KNIFE_MIN_STONE).addTag(ModItemTags.KNIFE);
        tag(ModItemTags.KNIFE_MIN_IRON).add(Arrays.copyOfRange(knivesOrdered, 1, 6));
        tag(ModItemTags.KNIFE_MIN_GOLD).add(Arrays.copyOfRange(knivesOrdered, 2, 6));
        tag(ModItemTags.KNIFE_MIN_DIAMOND).add(Arrays.copyOfRange(knivesOrdered, 3, 6));
        tag(ModItemTags.KNIFE_MIN_OBSIDIAN).add(Arrays.copyOfRange(knivesOrdered, 4, 6));
        tag(ModItemTags.KNIFE_MIN_NETHERITE).add(Arrays.copyOfRange(knivesOrdered, 5, 6));

        tag(ModItemTags.IRON_CUP).add(ModItems.IRON_CUP.get());
        tag(ModItemTags.FRYING_PAN).add(ModItems.SKILLET.get());
        tag(ModItemTags.TORTILLAS).add(
                ModItems.TORTILLA.get(),
                ModItems.POTATO_TORTILLA.get(),
                ModItems.ONION_TORTILLA.get()
        );

        tag(ModTiers.REPAIRS_OBSIDIAN_KNIFE).add(Items.OBSIDIAN);
    }
}
