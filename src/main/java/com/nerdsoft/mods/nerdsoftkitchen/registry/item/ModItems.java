package com.nerdsoft.mods.nerdsoftkitchen.registry.item;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.food.ModFoods;
import com.nerdsoft.mods.nerdsoftkitchen.item.CreativeOnlyBlockItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.GrillBlockItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.SeedItem;
import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {

    /// Items Register
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NerdSoftKitchen.MOD_ID);

    /// Machine Items
    public static final DeferredItem<GrillBlockItem> GRILL_TABLE = ITEMS.registerItem("grill_table",
            props -> new GrillBlockItem(ModBlocks.GRILL_TABLE.get(), props));
    public static final DeferredItem<GrillBlockItem> GRILL_TABLE_SOUL = ITEMS.registerItem("grill_table_soul",
            props -> new GrillBlockItem(ModBlocks.GRILL_TABLE_SOUL.get(), props));

    /// Wild Crop Block Items
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_STRAWBERRY =
            ITEMS.register("wild_strawberry", () -> new CreativeOnlyBlockItem(ModBlocks.WILD_STRAWBERRY.get(), new Item.Properties()));
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_TOMATO =
            ITEMS.register("wild_tomato", () -> new CreativeOnlyBlockItem(ModBlocks.WILD_TOMATO.get(), new Item.Properties().food(ModFoods.WILD_TOMATO)));
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_LETTUCE =
            ITEMS.register("wild_lettuce", () -> new CreativeOnlyBlockItem(ModBlocks.WILD_LETTUCE.get(), new Item.Properties()));
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_PURPLE_ONION =
            ITEMS.register("wild_purple_onion", () -> new CreativeOnlyBlockItem(ModBlocks.WILD_PURPLE_ONION.get(), new Item.Properties()));

    /// Crop Seeds
    public static final DeferredItem<SeedItem> STRAWBERRY_SEEDS =
            ITEMS.register("strawberry_seeds", () -> new SeedItem(ModBlocks.STRAWBERRY_CROP.get(), new Item.Properties()));
    public static final DeferredItem<SeedItem> TOMATO_SEEDS =
            ITEMS.register("tomato_seeds", () -> new SeedItem(ModBlocks.TOMATO_CROP.get(), new Item.Properties()));
    public static final DeferredItem<SeedItem> LETTUCE_SEEDS =
            ITEMS.register("lettuce_seeds", () -> new SeedItem(ModBlocks.LETTUCE_CROP.get(), new Item.Properties()));
    public static final DeferredItem<SeedItem> PURPLE_ONION_SEEDS =
            ITEMS.register("purple_onion_seeds", () -> new SeedItem(ModBlocks.PURPLE_ONION_CROP.get(), new Item.Properties()));

    /// Raw Ingredients
    public static final DeferredItem<Item> STRAWBERRY = ITEMS.registerItem("strawberry",
            props -> new Item(props.food(ModFoods.STRAWBERRY)));
    public static final DeferredItem<Item> TOMATO = ITEMS.registerItem("tomato",
            props -> new Item(props.food(ModFoods.TOMATO)));
    public static final DeferredItem<Item> LETTUCE = ITEMS.registerItem("lettuce",
            props -> new Item(props.food(ModFoods.LETTUCE)));
    public static final DeferredItem<Item> PURPLE_ONION = ITEMS.registerItem("purple_onion",
            props -> new Item(props.food(ModFoods.PURPLE_ONION_RAW)));

    /// Tools and Utensils
    public static final DeferredItem<IronCupItem> IRON_CUP = ITEMS.registerItem("iron_cup",
            props -> new IronCupItem(props.stacksTo(16)));

    /// Cooked Foods
    public static final DeferredItem<Item> RAW_CHICKEN_PIECES = ITEMS.registerItem("raw_chicken_pieces",
            props -> new Item(props.food(ModFoods.RAW_CHICKEN_PIECES)));
    public static final DeferredItem<Item> COOKED_CHICKEN_PIECES = ITEMS.registerItem("cooked_chicken_pieces",
            props -> new Item(props.food(ModFoods.COOKED_CHICKEN_PIECES)));
    public static final DeferredItem<Item> FRIED_EGG = ITEMS.registerItem("fried_egg",
            props -> new Item(props.food(ModFoods.FRIED_EGG)));
    public static final DeferredItem<Item> SALAD = ITEMS.registerItem("salad",
            props -> new Item(props.food(ModFoods.SALAD)));

    private ModItems() {
    }
}