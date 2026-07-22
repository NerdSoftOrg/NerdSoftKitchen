package com.nerdsoft.mods.nerdsoftkitchen.datagen;

import com.nerdsoft.mods.nerdsoftkitchen.registry.ModBlocks;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModItems;
import net.minecraft.data.PackOutput;

public class ModEnUsLanguageProvider extends ModLanguageProvider {

    public ModEnUsLanguageProvider(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModBlocks.GRILL_TABLE.get(), "Grill");
        add(ModBlocks.GRILL_TABLE_SOUL.get(), "Soul Grill");
        add(ModBlocks.WILD_STRAWBERRY.get(), "Wild Strawberry");
        add(ModBlocks.WILD_TOMATO.get(), "Wild Tomato");
        add(ModBlocks.WILD_LETTUCE.get(), "Wild Lettuce");
        add(ModBlocks.WILD_PURPLE_ONION.get(), "Wild Onion");
        add(ModBlocks.TOMATO_CROP_POLE.get(), "Staked Tomato Vine");

        add(ModItems.STRAWBERRY_SEEDS.get(), "Strawberry Seeds");
        add(ModItems.TOMATO_SEEDS.get(), "Tomato Seeds");
        add(ModItems.LETTUCE_SEEDS.get(), "Lettuce Seeds");
        add(ModItems.PURPLE_ONION_SEEDS.get(), "Onion Seeds");

        add(ModItems.STRAWBERRY.get(), "Strawberry");
        add(ModItems.TOMATO.get(), "Tomato");
        add(ModItems.LETTUCE.get(), "Lettuce");
        add(ModItems.PURPLE_ONION.get(), "Onion");
        add(ModItems.IRON_CUP.get(), "Iron Cup");
        add(ModItems.RAW_CHICKEN_PIECES.get(), "Raw Chicken Pieces");
        add(ModItems.COOKED_CHICKEN_PIECES.get(), "Cooked Chicken Pieces");
        add(ModItems.FRIED_EGG.get(), "Fried Egg");
        add(ModItems.SALAD.get(), "Salad");

        addFilledNamePrefix("Iron Cup of ");
        addContainsPrefix("Contains: ");
        addCupContent("milk", "Milk");
        addCupContent("yogurt", "Yogurt");
        addCupContent("strawberry_yogurt", "Strawberry Yogurt");

        add("itemGroup.nerdsoftkitchen.kitchen_tab", "NerdSoft Kitchen");
        add("subtitles.block.nerdsoftkitchen.grill.sizzle", "Oil sizzles");

        add("jade.nerdsoftkitchen.grill_table.slot_remaining", "%ss");
        add("config.jade.plugin_nerdsoftkitchen.grill_table_progress", "Cooking Time");

        addAdvancement("root", "NerdSoft Kitchen", "Craft a Grill");
        addAdvancement("iron_cup", "MiniBucket", "Craft an Iron Cup");
        addAdvancement("milk_cup", "Got Milk?", "Fill an Iron Cup with milk");
        addAdvancement("strawberry_yogurt", "Sweet Treat", "Make Strawberry Yogurt");
        addAdvancement("harvest_all", "Garden Variety", "Harvest every wild crop at least once");

        add("datapack.nerdsoftkitchen.description", "NerdSoft Kitchen Resources");

        add("jei.category.nerdsoftkitchen.grill_cooking", "Grill Cooking");
    }
}