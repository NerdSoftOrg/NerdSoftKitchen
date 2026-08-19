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
        // Blocks
        add(ModBlocks.GRILL_TABLE, "Grill");
        add(ModBlocks.GRILL_TABLE_SOUL, "Soul Grill");
        add(ModBlocks.TOMATO_CROP_POLE, "Staked Tomato Vine");
        add(ModBlocks.FERTILE_FARMLAND, "Fertile Farmland");
        add(ModBlocks.ORGANIC_SOIL, "Organic Soil");
        add(ModBlocks.CUTTING_BOARD, "Cutting Board");
        add(ModBlocks.SKILLET, "Skillet");

        // Crops & Seeds
        addWildCropPrefix("Wild %s");
        addCropSeedsPrefix("%s Seeds");

        addCrop(
                ModBlocks.WILD_STRAWBERRY,
                ModItems.STRAWBERRY_SEEDS,
                ModItems.STRAWBERRY,
                "Strawberry"
        );
        addCrop(
                ModBlocks.WILD_TOMATO,
                ModItems.TOMATO_SEEDS,
                ModItems.TOMATO,
                "Tomato"
        );
        addCrop(
                ModBlocks.WILD_LETTUCE,
                ModItems.LETTUCE_SEEDS,
                ModItems.LETTUCE,
                "Lettuce"
        );
        addCrop(
                ModBlocks.WILD_PURPLE_ONION,
                ModItems.PURPLE_ONION_SEEDS,
                ModItems.PURPLE_ONION,
                "Onion"
        );
        addCrop(
                ModBlocks.WILD_RICE,
                ModItems.RICE_SEEDS,
                ModItems.RICE,
                "Rice"
        );

        // Knifes
        addKnifePattern("%s Knife");

        addKnife(ModItems.STONE_KNIFE, "Stone");
        addKnife(ModItems.IRON_KNIFE, "Iron");
        addKnife(ModItems.GOLD_KNIFE, "Gold");
        addKnife(ModItems.DIAMOND_KNIFE, "Diamond");
        addKnife(ModItems.OBSIDIAN_KNIFE, "Obsidian");
        addKnife(ModItems.NETHERITE_KNIFE, "Netherite");

        // Items & Foods
        add(ModItems.IRON_CUP, "Iron Cup");
        add(ModItems.RAW_CHICKEN_PIECES, "Raw Chicken Pieces");
        add(ModItems.COOKED_CHICKEN_PIECES, "Cooked Chicken Pieces");
        add(ModItems.FRIED_EGG, "Fried Egg");
        add(ModItems.RAW_SANDWICH_BREAD, "Sandwich Bread");
        add(ModItems.TOASTED_SANDWICH_BREAD, "Toasted Sandwich Bread");
        add(ModItems.SALAD, "Salad");
        add(ModItems.CHEESE, "Cheese");
        add(ModItems.CHEESE_SLICE, "Cheese Slice");
        add(ModItems.CHEESE_RAW_SANDWICH, "Cheese Sandwich");
        add(ModItems.CHEESE_TOASTED_SANDWICH, "Toasted Cheese Sandwich");
        add(ModItems.GRILLED_CHEESE, "Grilled Cheese");
        add(ModItems.TORTILLA, "Tortilla");
        add(ModItems.POTATO_TORTILLA, "Potato Tortilla");
        add(ModItems.ONION_TORTILLA, "Onion Tortilla");
        add(ModItems.CUT_POTATO, "Cut Potato");
        add(ModItems.CUT_PURPLE_ONION, "Cut Purple Onion");
        add(ModItems.COOKED_RICE, "Cooked Rice");
        add(ModItems.RICE_BOWL, "Rice Bowl");
        add(ModItems.ORGANIC_MIXTURE, "Organic Mixture");

        // Iron Cups
        addFilledNamePrefix("Iron Cup of ");
        addContainsPrefix("Contains: ");
        addCupContent("milk", "Milk");
        addCupContent("yogurt", "Yogurt");
        addCupContent("strawberry_yogurt", "Strawberry Yogurt");
        add("nerdsoftkitchen.iron_cup.tooltip.empty", "Right-click a cow to fill with milk");

        // Tooltips
        add("itemGroup.nerdsoftkitchen.kitchen_tab", "NerdSoft Kitchen");
        add("subtitles.block.nerdsoftkitchen.grill.place_food", "Grilling food");
        add("subtitles.block.nerdsoftkitchen.skillet.sizzle_loop", "Skillet sizzling");
        add("subtitles.item.nerdsoftkitchen.skillet.clang", "Skillet clangs");
        add("jade.nerdsoftkitchen.grill_table.slot_remaining", "%ss");
        add("jade.nerdsoftkitchen.skillet.slot_remaining", "%ss");
        add("jade.nerdsoftkitchen.skillet.egg_quantity", "Eggs: %s");
        add("config.jade.plugin_nerdsoftkitchen.grill_table_progress", "Cooking Time");

        // Advancements
        addAdvancement(
                "root",
                "NerdSoft Kitchen",
                "Craft a Grill"
        );
        addAdvancement(
                "iron_cup",
                "Mini-Bucket",
                "Craft an Iron Cup"
        );
        addAdvancement(
                "cutting_board",
                "Chef's Prep Station",
                "Craft a Cutting Board"
        );
        addAdvancement(
                "grill_soul",
                "Spooky Barbecue",
                "Craft a Soul Grill"
        );
        addAdvancement(
                "master_knife",
                "Bladesmith",
                "Craft a Diamond, Obsidian, or Netherite Knife"
        );
        addAdvancement(
                "milk_cup",
                "Got Milk?",
                "Fill an Iron Cup with milk"
        );
        addAdvancement(
                "yogurt",
                "Cultured",
                "Make plain Yogurt"
        );
        addAdvancement(
                "strawberry_yogurt",
                "Sweet Treat",
                "Make Strawberry Yogurt"
        );
        addAdvancement(
                "grow_strawberry",
                "Berry Patch",
                "Harvest a Strawberry"
        );
        addAdvancement(
                "grow_lettuce",
                "Leafy Greens",
                "Harvest Lettuce"
        );
        addAdvancement(
                "grow_purple_onion",
                "Tearjerker",
                "Harvest a Purple Onion"
        );
        addAdvancement(
                "grow_tomato",
                "Vine Ripened",
                "Harvest a Tomato"
        );
        addAdvancement(
                "trellis_master",
                "Up the Trellis",
                "Grow a Tomato Vine on its pole"
        );
        addAdvancement(
                "harvest_all",
                "Garden Variety",
                "Harvest every crop at least once"
        );
        addAdvancement(
                "make_salad",
                "Fresh & Crisp",
                "Make a Salad"
        );
        addAdvancement(
                "cook_chicken_pieces",
                "Diced and Grilled",
                "Cook Chicken Pieces on the Grill"
        );
        addAdvancement(
                "fry_egg",
                "Sunny Side Up",
                "Fry an Egg on the Grill"
        );
        addAdvancement(
                "grilled_cheese",
                "Melty Goodness",
                "Make a Grilled Cheese sandwich"
        );
        addAdvancement(
                "potato_tortilla",
                "Fold It Up",
                "Cook a Potato Tortilla in the Skillet"
        );
        addAdvancement(
                "gourmet",
                "Gourmet Chef",
                "Master every kitchen discipline: farming, cutting, grilling, and dairy"
        );

        // JEI Info
        add("jei.category.nerdsoftkitchen.grill_cooking", "Grill Cooking");
        addJeiInfo(
                "grill_table",
                "Cook food directly on the grill grate, or use the campfire slots underneath like a regular campfire. Place a Hay Block beneath it to cook 25% faster!"
        );
        addJeiInfo(
                "grill_table_soul",
                "Built with a Soul Campfire instead of a regular one. Works like the regular Grill, but cooks 10% faster!"
        );
        addJeiInfo(
                "iron_cup",
                "Right-click a cow to fill with milk. Combine a filled cup with sugar on a crafting grid to make yogurt."
        );
        addJeiInfo(
                "iron_cup_milk",
                "Cannot be crafted: right-click an empty Iron Cup on a cow (not a Mooshroom) to fill it with milk."
        );
        addJeiInfo(
                "iron_cup_yogurt",
                "Craft a Milk-filled Iron Cup with Sugar to make plain Yogurt."
        );
        addJeiInfo(
                "iron_cup_strawberry_yogurt",
                "Craft a Milk-filled Iron Cup with Sugar and a Strawberry, or a plain Yogurt Cup with a Strawberry."
        );
        addJeiInfo(
                "cutting_board",
                "Right-click with a cuttable ingredient to place it, then right-click with any knife (tagged #c:tools/knife) to cut it. Cutting damages the knife by 1 durability."
        );
        addJeiInfo(
                "skillet",
                "Place on top of a lit Grill, Campfire, Fire, or Magma Block. Cooks the same simple recipes as the Grill, plus tortillas when Egg is combined with Cut Potato or Cut Purple Onion. Doubles as a heavy melee weapon - picking it up while hot lets it set enemies alight."
        );
        addJeiInfo(
                "tortilla",
                "Cook a single Egg alone in the Skillet. Add Cut Potato or Cut Purple Onion while it cooks to turn it into a Potato or Onion Tortilla instead."
        );

        // Datapack
        add("datapack.nerdsoftkitchen.description", "NerdSoft Kitchen Resources");
    }
}
