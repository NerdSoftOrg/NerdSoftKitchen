package com.nerdsoft.mods.nerdsoftkitchen.datagen.lang;

import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
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
        add(ModItems.DIAMOND_KNIFE.get(), "Diamond Knife");
        add(ModBlocks.CUTTING_BOARD.get(), "Cutting Board");
        add(ModItems.CHEESE.get(), "Cheese");
        add(ModItems.CHEESE_SLICE.get(), "Cheese Slice");
        add(ModItems.CHEESE_SANDWICH.get(), "Cheese Sandwich");
        add(ModItems.GRILLED_CHEESE.get(), "Grilled Cheese");

        addFilledNamePrefix("Iron Cup of ");
        addContainsPrefix("Contains: ");
        addCupContent("milk", "Milk");
        addCupContent("yogurt", "Yogurt");
        addCupContent("strawberry_yogurt", "Strawberry Yogurt");

        add("itemGroup.nerdsoftkitchen.kitchen_tab", "NerdSoft Kitchen");
        add("subtitles.block.nerdsoftkitchen.grill.place_food", "Grilling food");

        add("jade.nerdsoftkitchen.grill_table.slot_remaining", "%ss");
        add("config.jade.plugin_nerdsoftkitchen.grill_table_progress", "Cooking Time");

        addAdvancement("root", "NerdSoft Kitchen", "Craft a Grill");
        addAdvancement("iron_cup", "MiniBucket", "Craft an Iron Cup");
        addAdvancement("milk_cup", "Got Milk?", "Fill an Iron Cup with milk");
        addAdvancement("strawberry_yogurt", "Sweet Treat", "Make Strawberry Yogurt");
        addAdvancement("harvest_all", "Garden Variety", "Harvest every wild crop at least once");

        add("datapack.nerdsoftkitchen.description", "NerdSoft Kitchen Resources");

        add("jei.category.nerdsoftkitchen.grill_cooking", "Grill Cooking");

        add("nerdsoftkitchen.jei.info.grill_table",
                "Cook food directly on the grill grate, or use the campfire slots underneath like a regular campfire.");
        add("nerdsoftkitchen.jei.info.grill_table_soul",
                "Built with a Soul Campfire instead of a regular one. Crafts and cooks exactly like the regular Grill.");
        add("nerdsoftkitchen.jei.info.iron_cup",
                "Right-click a cow to fill with milk. Combine a filled cup with sugar on a crafting grid for yogurt.");
        add("nerdsoftkitchen.jei.info.cutting_board",
                "Right-click with a cuttable ingredient to place it, then right-click with any knife (tagged #c:tools/knife) to cut it. Cutting damages the knife by 1 durability.");
    }
}