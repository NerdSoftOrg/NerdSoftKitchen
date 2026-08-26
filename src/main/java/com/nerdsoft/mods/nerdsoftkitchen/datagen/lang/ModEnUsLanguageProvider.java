package com.nerdsoft.mods.nerdsoftkitchen.datagen.lang;

import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import com.nerdsoft.mods.nerdsoftkitchen.registry.sound.ModSounds;
import net.minecraft.data.PackOutput;

public class ModEnUsLanguageProvider extends ModLanguageProvider {

    public ModEnUsLanguageProvider(PackOutput output) {
        super(output, "en_us");
    }

    @Override
    protected void addTranslations() {
        // Tab Group & Datapack
        add("itemGroup.nerdsoftkitchen.kitchen_tab", "NerdSoft Kitchen");
        add("datapack.nerdsoftkitchen.description", "NerdSoft Kitchen Resources");

        // Blocks
        add(ModBlocks.GRILL_TABLE, "Grill");
        add(ModBlocks.GRILL_TABLE_SOUL, "Soul Grill");
        add(ModBlocks.GRILL_TABLE_UNLIT, "Unlit Grill");
        add(ModBlocks.GRILL_TABLE_SOUL_UNLIT, "Unlit Soul Grill");
        add(ModBlocks.TOMATO_CROP_POLE, "Staked Tomato Vine");
        add(ModBlocks.FERTILE_FARMLAND, "Fertile Farmland");
        add(ModBlocks.ORGANIC_SOIL, "Organic Soil");
        add(ModBlocks.CUTTING_BOARD, "Cutting Board");
        add(ModBlocks.SKILLET, "Skillet");

        // Crops & Knives Patterns
        addWildCropPrefix("Wild %s");
        addCropSeedsPrefix("%s Seeds");
        addCrop(ModBlocks.WILD_STRAWBERRY, ModItems.STRAWBERRY_SEEDS, ModItems.STRAWBERRY, "Strawberry");
        addCrop(ModBlocks.WILD_TOMATO, ModItems.TOMATO_SEEDS, ModItems.TOMATO, "Tomato");
        addCrop(ModBlocks.WILD_LETTUCE, ModItems.LETTUCE_SEEDS, ModItems.LETTUCE, "Lettuce");
        addCrop(ModBlocks.WILD_PURPLE_ONION, ModItems.PURPLE_ONION_SEEDS, ModItems.PURPLE_ONION, "Onion");
        addCrop(ModBlocks.WILD_RICE, ModItems.RICE_SEEDS, ModItems.RICE, "Rice");

        addKnifePattern("%s Knife");
        addKnife(ModItems.STONE_KNIFE, "Stone");
        addKnife(ModItems.IRON_KNIFE, "Iron");
        addKnife(ModItems.GOLDEN_KNIFE, "Gold");
        addKnife(ModItems.DIAMOND_KNIFE, "Diamond");
        addKnife(ModItems.OBSIDIAN_KNIFE, "Obsidian");
        addKnife(ModItems.NETHERITE_KNIFE, "Netherite");

        // Simple Items
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

        // Cup System
        addFilledNamePrefix("Iron Cup of ");
        addContainsPrefix("Contains: ");
        addCupContent("milk", "Milk");
        addCupContent("yogurt", "Yogurt");
        addCupContent("strawberry_yogurt", "Strawberry Yogurt");
        addCupContent("liquid_egg", "Egg Liquid");
        addCupTooltip("milk", "Right-click a Cow to fill with Milk");
        addCupTooltip("liquid_egg", "Right-click a Skillet with an egg to fill with Liquid Egg");

        // Subtitles
        addSubtitle(ModSounds.GRILL_PLACE_FOOD, "Grilling food");
        addSubtitle(ModSounds.SKILLET_SIZZLE_LOOP, "Skillet sizzling");
        addSubtitle(ModSounds.SKILLET_CLANG, "Skillet clangs");

        // Jade Integration
        addJadeTooltip("grill_table.slot_remaining", "%ss");
        addJadeTooltip("skillet.slot_remaining", "%ss");
        addJadeTooltip("skillet.egg_quantity", "Eggs: %s");
        addJadeConfig("grill_table_progress", "Grill Progress");
        addJadeConfig("skillet_progress", "Skillet Progress");

        // Advancements
        addAdvancement("root", "NerdSoft Kitchen", "Craft a Grill");
        addAdvancement("iron_cup", "Mini-Bucket", "Craft an Iron Cup");
        addAdvancement("cutting_board", "Chef's Prep Station", "Craft a Cutting Board");
        addAdvancement("grill_soul", "Spooky Barbecue", "Craft a Soul Grill");
        addAdvancement("master_knife", "Bladesmith", "Craft a Diamond, Obsidian, or Netherite Knife");
        addAdvancement("milk_cup", "Got Milk?", "Fill an Iron Cup with milk");
        addAdvancement("yogurt", "Cultured", "Make plain Yogurt");
        addAdvancement("strawberry_yogurt", "Sweet Treat", "Make Strawberry Yogurt");
        addAdvancement("grow_strawberry", "Berry Patch", "Harvest a Strawberry");
        addAdvancement("grow_lettuce", "Leafy Greens", "Harvest Lettuce");
        addAdvancement("grow_purple_onion", "Tearjerker", "Harvest a Purple Onion");
        addAdvancement("grow_tomato", "Vine Ripened", "Harvest a Tomato");
        addAdvancement("trellis_master", "Up the Trellis", "Grow a Tomato Vine on its pole");
        addAdvancement("harvest_all", "Garden Variety", "Harvest every crop at least once");
        addAdvancement("make_salad", "Fresh & Crisp", "Make a Salad");
        addAdvancement("cook_chicken_pieces", "Diced and Grilled", "Cook Chicken Pieces on the Grill");
        addAdvancement("fry_egg", "Sunny Side Up", "Fry an Egg on the Grill");
        addAdvancement("grilled_cheese", "Melty Goodness", "Make a Grilled Cheese sandwich");
        addAdvancement("potato_tortilla", "Fold It Up", "Cook a Potato Tortilla in the Skillet");
        addAdvancement("gourmet", "Gourmet Chef", "Master every kitchen discipline: farming, cutting, grilling, and dairy");

        // JEI
        addJeiCategory("grill_cooking", "Grill Cooking");
        addJeiInfo("grill_table", "Cook food directly on the grill grate, or use the campfire slots underneath like a regular campfire. Place a Hay Block beneath it to cook 25% faster!");
        addJeiInfo("grill_table_soul", "Built with a Soul Campfire instead of a regular one. Works like the regular Grill, but cooks 10% faster!");
        addJeiInfo("grill_table_unlit", "Cannot be crafted directly: put out a lit Grill with water or a Shovel (costs 1 durability) to get this. Relight it in place with Flint and Steel, a Fire Charge, or a flaming arrow, or craft it with Blaze Powder (add Soul Sand/Soil too to relight it as a Soul Grill instead).");
        addJeiInfo("grill_table_soul_unlit", "Cannot be crafted directly: put out a lit Soul Grill with water or a Shovel (costs 1 durability) to get this. Relight it in place with Flint and Steel, a Fire Charge, or a flaming arrow, or craft it with Blaze Powder.");
        addJeiInfo("iron_cup", "Right-click a cow to fill with milk. Combine a filled cup with sugar on a crafting grid to make yogurt.");
        addJeiInfo("iron_cup_milk", "Cannot be crafted: right-click an empty Iron Cup on a cow (not a Mooshroom) to fill it with milk.");
        addJeiInfo("iron_cup_yogurt", "Craft a Milk-filled Iron Cup with Sugar to make plain Yogurt.");
        addJeiInfo("iron_cup_strawberry_yogurt", "Craft a Milk-filled Iron Cup with Sugar and a Strawberry, or a plain Yogurt Cup with a Strawberry.");
        addJeiInfo("iron_cup_liquid_egg", "Cannot be crafted: right-click an empty Iron Cup on a Skillet holding raw Egg liquid to draw it off. Right-click a filled Egg Cup on an empty Skillet to pour it back in.");
        addJeiInfo("cutting_board", "Right-click with a cuttable ingredient to place it, then right-click with any knife to cut it. Cutting damages the knife by 1 durability.");
        addJeiInfo("skillet", "Place on top of a lit Grill, Campfire, Fire, or Magma Block. Cooks the same simple recipes as the Grill, plus tortillas when Egg is combined with Cut Potato or Cut Purple Onion. Doubles as a heavy melee weapon - picking it up while hot lets it set enemies alight.");
        addJeiInfo("tortilla", "Cook a single Egg alone in the Skillet. Add Cut Potato or Cut Purple Onion while it cooks to turn it into a Potato or Onion Tortilla instead.");

        // Death Messages (Damage Types)
        addDeathMessage("cookware_burn", "%1$s stepped on a hot %2$s");
        addDeathMessagePlayer("cookware_burn", "%1$s stepped on a hot %2$s while escaping %3$s");
    }
}
