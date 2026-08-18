package com.nerdsoft.mods.nerdsoftkitchen.registry.item;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.food.ModFoods;
import com.nerdsoft.mods.nerdsoftkitchen.item.CreativeOnlyBlockItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.GrillBlockItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.KnifeItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.OrganicMixtureItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.SeedItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.SkilletBlockItem;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModTiers;
import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

//? if <1.21.2 {
import net.minecraft.world.item.Tiers;
//?} else {
/*import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.ToolMaterial;

import java.util.List;
*///?}

@SuppressWarnings("CommentedOutCode")
public final class ModItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(NerdSoftKitchen.MOD_ID);

    private static final float STONE_KNIFE_DAMAGE = 0.5F; // 2.5
    @SuppressWarnings("InvariantValue")
    //? if > 1.21.8 {
    /*private static final float COPPER_KNIFE_DAMAGE = 0.75F; // 2.75 - 1.0 (copper tier)
    *///?}
    private static final float IRON_KNIFE_DAMAGE = 0.0F; // 3.0
    private static final float GOLD_KNIFE_DAMAGE = 1.0F; // 2.0
    private static final float DIAMOND_KNIFE_DAMAGE = -0.5F; // 3.5
    private static final float OBSIDIAN_KNIFE_DAMAGE = -1.25F; // 3.75
    private static final float NETHERITE_KNIFE_DAMAGE = -1.0F; // 4.0

    private static final float DEFAULT_KNIFE_SPEED = -1.4F; // 2.6

    private static final int SKILLET_DURABILITY = 250;

    //? if <1.21.2 {
    /// Machine Items
    public static final DeferredItem<GrillBlockItem> GRILL_TABLE = ITEMS.registerItem("grill_table",
            props -> new GrillBlockItem(ModBlocks.GRILL_TABLE.get(), props));
    public static final DeferredItem<GrillBlockItem> GRILL_TABLE_SOUL = ITEMS.registerItem("grill_table_soul",
            props -> new GrillBlockItem(ModBlocks.GRILL_TABLE_SOUL.get(), props));
    public static final DeferredItem<BlockItem> CUTTING_BOARD = ITEMS.registerSimpleBlockItem(ModBlocks.CUTTING_BOARD);
    public static final DeferredItem<BlockItem> FERTILE_FARMLAND = ITEMS.registerSimpleBlockItem(ModBlocks.FERTILE_FARMLAND);
    public static final DeferredItem<BlockItem> ORGANIC_SOIL = ITEMS.registerSimpleBlockItem(ModBlocks.ORGANIC_SOIL);
    public static final DeferredItem<SkilletBlockItem> SKILLET = ITEMS.registerItem("skillet",
            props -> new SkilletBlockItem(ModBlocks.SKILLET.get(), props.stacksTo(1).durability(SKILLET_DURABILITY)));

    /// Wild Crop Block Items
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_STRAWBERRY =
            ITEMS.register("wild_strawberry", () -> new CreativeOnlyBlockItem(ModBlocks.WILD_STRAWBERRY.get(), new Item.Properties()));
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_TOMATO =
            ITEMS.register("wild_tomato", () -> new CreativeOnlyBlockItem(ModBlocks.WILD_TOMATO.get(), new Item.Properties().food(ModFoods.WILD_TOMATO)));
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_LETTUCE =
            ITEMS.register("wild_lettuce", () -> new CreativeOnlyBlockItem(ModBlocks.WILD_LETTUCE.get(), new Item.Properties()));
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_PURPLE_ONION =
            ITEMS.register("wild_purple_onion", () -> new CreativeOnlyBlockItem(ModBlocks.WILD_PURPLE_ONION.get(), new Item.Properties()));
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_RICE =
            ITEMS.register("wild_rice", () -> new CreativeOnlyBlockItem(ModBlocks.WILD_RICE.get(), new Item.Properties()));

    /// Crop Seeds
    public static final DeferredItem<SeedItem> STRAWBERRY_SEEDS =
            ITEMS.register("strawberry_seeds", () -> new SeedItem(ModBlocks.STRAWBERRY_CROP.get(), new Item.Properties()));
    public static final DeferredItem<SeedItem> TOMATO_SEEDS =
            ITEMS.register("tomato_seeds", () -> new SeedItem(ModBlocks.TOMATO_CROP.get(), new Item.Properties()));
    public static final DeferredItem<SeedItem> LETTUCE_SEEDS =
            ITEMS.register("lettuce_seeds", () -> new SeedItem(ModBlocks.LETTUCE_CROP.get(), new Item.Properties()));
    public static final DeferredItem<SeedItem> PURPLE_ONION_SEEDS =
            ITEMS.register("purple_onion_seeds", () -> new SeedItem(ModBlocks.PURPLE_ONION_CROP.get(), new Item.Properties()));
    public static final DeferredItem<SeedItem> RICE_SEEDS =
            ITEMS.register("rice_seeds", () -> new SeedItem(ModBlocks.RICE_CROP.get(), new Item.Properties()));

    /// Knives
    public static final DeferredItem<KnifeItem> STONE_KNIFE = ITEMS.registerItem("stone_knife",
            props -> new KnifeItem(Tiers.STONE, STONE_KNIFE_DAMAGE, DEFAULT_KNIFE_SPEED, props));
    public static final DeferredItem<KnifeItem> IRON_KNIFE = ITEMS.registerItem("iron_knife",
            props -> new KnifeItem(Tiers.IRON, IRON_KNIFE_DAMAGE, DEFAULT_KNIFE_SPEED, props));
    public static final DeferredItem<KnifeItem> GOLD_KNIFE = ITEMS.registerItem("gold_knife",
            props -> new KnifeItem(Tiers.GOLD, GOLD_KNIFE_DAMAGE, DEFAULT_KNIFE_SPEED, props));
    public static final DeferredItem<KnifeItem> DIAMOND_KNIFE = ITEMS.registerItem("diamond_knife",
            props -> new KnifeItem(Tiers.DIAMOND, DIAMOND_KNIFE_DAMAGE, DEFAULT_KNIFE_SPEED, props));
    public static final DeferredItem<KnifeItem> OBSIDIAN_KNIFE = ITEMS.registerItem("obsidian_knife",
            props -> new KnifeItem(ModTiers.OBSIDIAN, OBSIDIAN_KNIFE_DAMAGE, DEFAULT_KNIFE_SPEED, props));
    public static final DeferredItem<KnifeItem> NETHERITE_KNIFE = ITEMS.registerItem("netherite_knife",
            props -> new KnifeItem(Tiers.NETHERITE, NETHERITE_KNIFE_DAMAGE, DEFAULT_KNIFE_SPEED, props));
    //?} else {
    /*/// Machine Items
    public static final DeferredItem<GrillBlockItem> GRILL_TABLE = ITEMS.registerItem(
            "grill_table", props -> new GrillBlockItem(ModBlocks.GRILL_TABLE.get(), props));
    public static final DeferredItem<GrillBlockItem> GRILL_TABLE_SOUL = ITEMS.registerItem(
            "grill_table_soul", props -> new GrillBlockItem(ModBlocks.GRILL_TABLE_SOUL.get(), props));
    public static final DeferredItem<BlockItem> CUTTING_BOARD = ITEMS.registerSimpleBlockItem(ModBlocks.CUTTING_BOARD);
    public static final DeferredItem<BlockItem> FERTILE_FARMLAND = ITEMS.registerSimpleBlockItem(ModBlocks.FERTILE_FARMLAND);
    public static final DeferredItem<BlockItem> ORGANIC_SOIL = ITEMS.registerSimpleBlockItem(ModBlocks.ORGANIC_SOIL);
    public static final DeferredItem<SkilletBlockItem> SKILLET = ITEMS.registerItem("skillet",
            props -> new SkilletBlockItem(ModBlocks.SKILLET.get(), props.stacksTo(1).durability(SKILLET_DURABILITY)));

    /// Wild Crop Block Items
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_STRAWBERRY = ITEMS.registerItem(
            "wild_strawberry", props -> new CreativeOnlyBlockItem(ModBlocks.WILD_STRAWBERRY.get(), props));
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_TOMATO = ITEMS.registerItem(
            "wild_tomato", props -> new CreativeOnlyBlockItem(ModBlocks.WILD_TOMATO.get(), props.food(ModFoods.WILD_TOMATO)));
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_LETTUCE = ITEMS.registerItem(
            "wild_lettuce", props -> new CreativeOnlyBlockItem(ModBlocks.WILD_LETTUCE.get(), props));
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_PURPLE_ONION = ITEMS.registerItem(
            "wild_purple_onion", props -> new CreativeOnlyBlockItem(ModBlocks.WILD_PURPLE_ONION.get(), props));
    public static final DeferredItem<CreativeOnlyBlockItem> WILD_RICE = ITEMS.registerItem(
            "wild_rice", props -> new CreativeOnlyBlockItem(ModBlocks.WILD_RICE.get(), props));

    /// Crop Seeds
    public static final DeferredItem<SeedItem> STRAWBERRY_SEEDS = ITEMS.registerItem(
            "strawberry_seeds", props -> new SeedItem(ModBlocks.STRAWBERRY_CROP.get(), props));
    public static final DeferredItem<SeedItem> TOMATO_SEEDS = ITEMS.registerItem(
            "tomato_seeds", props -> new SeedItem(ModBlocks.TOMATO_CROP.get(), props));
    public static final DeferredItem<SeedItem> LETTUCE_SEEDS = ITEMS.registerItem(
            "lettuce_seeds", props -> new SeedItem(ModBlocks.LETTUCE_CROP.get(), props));
    public static final DeferredItem<SeedItem> PURPLE_ONION_SEEDS = ITEMS.registerItem(
            "purple_onion_seeds", props -> new SeedItem(ModBlocks.PURPLE_ONION_CROP.get(), props));
    public static final DeferredItem<SeedItem> RICE_SEEDS = ITEMS.registerItem(
            "rice_seeds", props -> new SeedItem(ModBlocks.RICE_CROP.get(), props));

    /// Knives
    public static final DeferredItem<KnifeItem> STONE_KNIFE = ITEMS.registerItem("stone_knife",
            props -> new KnifeItem(ToolMaterial.STONE, STONE_KNIFE_DAMAGE, DEFAULT_KNIFE_SPEED, props));
    public static final DeferredItem<KnifeItem> IRON_KNIFE = ITEMS.registerItem("iron_knife",
            props -> new KnifeItem(ToolMaterial.IRON, IRON_KNIFE_DAMAGE, DEFAULT_KNIFE_SPEED, props));
    public static final DeferredItem<KnifeItem> GOLD_KNIFE = ITEMS.registerItem("gold_knife",
            props -> new KnifeItem(ToolMaterial.GOLD, GOLD_KNIFE_DAMAGE, DEFAULT_KNIFE_SPEED, props));
    public static final DeferredItem<KnifeItem> DIAMOND_KNIFE = ITEMS.registerItem("diamond_knife",
            props -> new KnifeItem(ToolMaterial.DIAMOND, DIAMOND_KNIFE_DAMAGE, DEFAULT_KNIFE_SPEED, props));
    public static final DeferredItem<KnifeItem> OBSIDIAN_KNIFE = ITEMS.registerItem("obsidian_knife",
            props -> new KnifeItem(ModTiers.OBSIDIAN, OBSIDIAN_KNIFE_DAMAGE, DEFAULT_KNIFE_SPEED, props));
    public static final DeferredItem<KnifeItem> NETHERITE_KNIFE = ITEMS.registerItem("netherite_knife",
            props -> new KnifeItem(ToolMaterial.NETHERITE, NETHERITE_KNIFE_DAMAGE, DEFAULT_KNIFE_SPEED, props));
    *///?}

    @SuppressWarnings("InvariantValue")
    //? if > 1.21.8 {
    /*public static final DeferredItem<KnifeItem> COPPER_KNIFE = ITEMS.registerItem("copper_knife",
           props -> new KnifeItem(ToolMaterial.IRON, COPPER_KNIFE_DAMAGE, COPPER_KNIFE_SPEED, props));
    *///?}

    /// Raw Ingredients
    public static final DeferredItem<Item> STRAWBERRY = ITEMS.registerItem("strawberry",
            props -> new Item(props.food(ModFoods.STRAWBERRY)));
    public static final DeferredItem<Item> TOMATO = ITEMS.registerItem("tomato",
            props -> new Item(props.food(ModFoods.TOMATO)));
    public static final DeferredItem<Item> LETTUCE = ITEMS.registerItem("lettuce",
            props -> new Item(props.food(ModFoods.LETTUCE)));
    public static final DeferredItem<Item> PURPLE_ONION = ITEMS.registerItem("purple_onion",
            props -> new Item(props.food(ModFoods.PURPLE_ONION_RAW)));
    public static final DeferredItem<Item> RAW_SANDWICH_BREAD = ITEMS.registerItem("raw_sandwich_bread",
            props -> new Item(props.food(ModFoods.RAW_SANDWICH_BREAD)));
    public static final DeferredItem<Item> RICE = ITEMS.registerItem("rice",
            props -> new Item(props.food(ModFoods.RICE)));

    public static final DeferredItem<OrganicMixtureItem> ORGANIC_MIXTURE = ITEMS.registerItem("organic_mixture",
            props -> new OrganicMixtureItem(props.stacksTo(64)));

    /// Tools and Utensils
    public static final DeferredItem<IronCupItem> IRON_CUP = ITEMS.registerItem("iron_cup",
            props -> new IronCupItem(props.stacksTo(16)));

    /// Dairy
    public static final DeferredItem<Item> CHEESE = ITEMS.registerItem("cheese",
            props -> new Item(props.food(ModFoods.CHEESE)));
    public static final DeferredItem<Item> CHEESE_SLICE = ITEMS.registerItem("cheese_slice",
            props -> new Item(props.food(ModFoods.CHEESE_SLICE)));

    /// Cooked Foods
    //? if <1.21.2 {
    public static final DeferredItem<Item> RAW_CHICKEN_PIECES = ITEMS.registerItem("raw_chicken_pieces",
            props -> new Item(props.food(ModFoods.RAW_CHICKEN_PIECES)));
    //?} else {
    /*public static final DeferredItem<Item> RAW_CHICKEN_PIECES = ITEMS.registerItem("raw_chicken_pieces",
            props -> new Item(props
                    .food(ModFoods.RAW_CHICKEN_PIECES)
                    .component(
                            DataComponents.CONSUMABLE,
                            Consumables.defaultFood()
                                    .onConsume(new ApplyStatusEffectsConsumeEffect(
                                            List.of(new MobEffectInstance(MobEffects.HUNGER, 300, 0)),
                                            0.15F
                                    ))
                                    .build()
                    )
            ));
    *///?}
    public static final DeferredItem<Item> COOKED_CHICKEN_PIECES = ITEMS.registerItem("cooked_chicken_pieces",
            props -> new Item(props.food(ModFoods.COOKED_CHICKEN_PIECES)));
    public static final DeferredItem<Item> FRIED_EGG = ITEMS.registerItem("fried_egg",
            props -> new Item(props.food(ModFoods.FRIED_EGG)));
    public static final DeferredItem<Item> SALAD = ITEMS.registerItem("salad",
            props -> new Item(props.food(ModFoods.SALAD)));
    public static final DeferredItem<Item> TOASTED_SANDWICH_BREAD = ITEMS.registerItem("toasted_sandwich_bread",
            props -> new Item(props.food(ModFoods.TOASTED_SANDWICH_BREAD)));
    public static final DeferredItem<Item> CHEESE_RAW_SANDWICH = ITEMS.registerItem("cheese_raw_sandwich",
            props -> new Item(props.food(ModFoods.CHEESE_RAW_SANDWICH)));
    public static final DeferredItem<Item> CHEESE_TOASTED_SANDWICH = ITEMS.registerItem("cheese_toasted_sandwich",
            props -> new Item(props.food(ModFoods.CHEESE_TOASTED_SANDWICH)));
    public static final DeferredItem<Item> GRILLED_CHEESE = ITEMS.registerItem("grilled_cheese",
            props -> new Item(props.food(ModFoods.GRILLED_CHEESE)));
    public static final DeferredItem<Item> SCRAMBLED_EGGS = ITEMS.registerItem("scrambled_eggs",
            props -> new Item(props.food(ModFoods.SCRAMBLED_EGGS)));
    public static final DeferredItem<Item> COOKED_RICE = ITEMS.registerItem("cooked_rice",
            props -> new Item(props.food(ModFoods.COOKED_RICE)));
    public static final DeferredItem<Item> RICE_BOWL = ITEMS.registerItem("rice_bowl",
            props -> new Item(props.food(ModFoods.RICE_BOWL)));

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        NerdSoftKitchenLogger.info("Items registered successfully.");
    }
}