package com.nerdsoft.mods.nerdsoftkitchen.datagen.advancement;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static net.minecraft.advancements.Advancement.Builder;

@SuppressWarnings("CommentedOutCode")
public class ModAdvancementProvider extends AdvancementProvider {

    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries,
                                  ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(ModAdvancementProvider::generate));
    }

    private static void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver,
                                 ExistingFileHelper existingFileHelper) {

        AdvancementHolder root = Builder.advancement()
                .display(
                        ModItems.GRILL_TABLE.get(),
                        Component.translatable("advancements.nerdsoftkitchen.root.title"),
                        Component.translatable("advancements.nerdsoftkitchen.root.description"),
                        ResourceLocation.withDefaultNamespace("textures/block/farmland.png"),
                        AdvancementType.TASK,
                        false, false, false
                )
                .addCriterion("has_grill_table",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GRILL_TABLE.get()))
                .save(saver, id("root"));

        // --- Utensils branch ---

        AdvancementHolder ironCup = Builder.advancement()
                .parent(root)
                .display(
                        ModItems.IRON_CUP.get(),
                        Component.translatable("advancements.nerdsoftkitchen.iron_cup.title"),
                        Component.translatable("advancements.nerdsoftkitchen.iron_cup.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_iron_cup",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.IRON_CUP.get()))
                .save(saver, id("craft_iron_cup"));

        AdvancementHolder cuttingBoard = Builder.advancement()
                .parent(root)
                .display(
                        ModItems.CUTTING_BOARD.get(),
                        Component.translatable("advancements.nerdsoftkitchen.cutting_board.title"),
                        Component.translatable("advancements.nerdsoftkitchen.cutting_board.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_cutting_board",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CUTTING_BOARD.get()))
                .save(saver, id("craft_cutting_board"));

        AdvancementHolder grillSoul = Builder.advancement()
                .parent(root)
                .display(
                        ModItems.GRILL_TABLE_SOUL.get(),
                        Component.translatable("advancements.nerdsoftkitchen.grill_soul.title"),
                        Component.translatable("advancements.nerdsoftkitchen.grill_soul.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_grill_table_soul",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GRILL_TABLE_SOUL.get()))
                .save(saver, id("craft_grill_table_soul"));

        AdvancementHolder masterKnife = Builder.advancement()
                .parent(cuttingBoard)
                .display(
                        ModItems.NETHERITE_KNIFE.get(),
                        Component.translatable("advancements.nerdsoftkitchen.master_knife.title"),
                        Component.translatable("advancements.nerdsoftkitchen.master_knife.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false
                )
                .rewards(AdvancementRewards.Builder.experience(25))
                .addCriterion("has_diamond_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.DIAMOND_KNIFE.get()))
                .addCriterion("has_obsidian_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.OBSIDIAN_KNIFE.get()))
                .addCriterion("has_netherite_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHERITE_KNIFE.get()))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(saver, id("craft_master_knife"));

        // --- Farming branch: one advancement per crop, feeding into harvest_all ---

        AdvancementHolder growStrawberry = Builder.advancement()
                .parent(root)
                .display(
                        ModItems.STRAWBERRY.get(),
                        Component.translatable("advancements.nerdsoftkitchen.grow_strawberry.title"),
                        Component.translatable("advancements.nerdsoftkitchen.grow_strawberry.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_strawberry", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAWBERRY.get()))
                .save(saver, id("grow_strawberry"));

        AdvancementHolder growLettuce = Builder.advancement()
                .parent(root)
                .display(
                        ModItems.LETTUCE.get(),
                        Component.translatable("advancements.nerdsoftkitchen.grow_lettuce.title"),
                        Component.translatable("advancements.nerdsoftkitchen.grow_lettuce.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_lettuce", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.LETTUCE.get()))
                .save(saver, id("grow_lettuce"));

        AdvancementHolder growPurpleOnion = Builder.advancement()
                .parent(root)
                .display(
                        ModItems.PURPLE_ONION.get(),
                        Component.translatable("advancements.nerdsoftkitchen.grow_purple_onion.title"),
                        Component.translatable("advancements.nerdsoftkitchen.grow_purple_onion.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_purple_onion", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PURPLE_ONION.get()))
                .save(saver, id("grow_purple_onion"));

        AdvancementHolder growTomato = Builder.advancement()
                .parent(root)
                .display(
                        ModItems.TOMATO.get(),
                        Component.translatable("advancements.nerdsoftkitchen.grow_tomato.title"),
                        Component.translatable("advancements.nerdsoftkitchen.grow_tomato.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_tomato", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOMATO.get()))
                .save(saver, id("grow_tomato"));

        AdvancementHolder trellisMaster = Builder.advancement()
                .parent(growTomato)
                .display(
                        ModItems.TOMATO.get(),
                        Component.translatable("advancements.nerdsoftkitchen.trellis_master.title"),
                        Component.translatable("advancements.nerdsoftkitchen.trellis_master.description"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false
                )
                .addCriterion("has_tomato_pole", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOMATO.get()))
                .save(saver, id("build_tomato_trellis"));

        AdvancementHolder harvestAll = Builder.advancement()
                .parent(root)
                .display(
                        ModItems.SALAD.get(),
                        Component.translatable("advancements.nerdsoftkitchen.harvest_all.title"),
                        Component.translatable("advancements.nerdsoftkitchen.harvest_all.description"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false
                )
                .addCriterion("has_strawberry", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.STRAWBERRY.get()))
                .addCriterion("has_tomato", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TOMATO.get()))
                .addCriterion("has_lettuce", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.LETTUCE.get()))
                .addCriterion("has_purple_onion", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PURPLE_ONION.get()))
                .save(saver, id("harvest_all_crops"));

        // --- Cooking branch (grill + cutting board results) ---

        AdvancementHolder makeSalad = Builder.advancement()
                .parent(harvestAll)
                .display(
                        ModItems.SALAD.get(),
                        Component.translatable("advancements.nerdsoftkitchen.make_salad.title"),
                        Component.translatable("advancements.nerdsoftkitchen.make_salad.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_salad", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SALAD.get()))
                .save(saver, id("make_salad"));

        AdvancementHolder cookChicken = Builder.advancement()
                .parent(root)
                .display(
                        ModItems.COOKED_CHICKEN_PIECES.get(),
                        Component.translatable("advancements.nerdsoftkitchen.cook_chicken_pieces.title"),
                        Component.translatable("advancements.nerdsoftkitchen.cook_chicken_pieces.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_cooked_chicken_pieces",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.COOKED_CHICKEN_PIECES.get()))
                .save(saver, id("cook_chicken_pieces"));

        AdvancementHolder fryEgg = Builder.advancement()
                .parent(root)
                .display(
                        ModItems.FRIED_EGG.get(),
                        Component.translatable("advancements.nerdsoftkitchen.fry_egg.title"),
                        Component.translatable("advancements.nerdsoftkitchen.fry_egg.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_fried_egg", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.FRIED_EGG.get()))
                .save(saver, id("fry_egg"));

        AdvancementHolder grilledCheese = Builder.advancement()
                .parent(cuttingBoard)
                .display(
                        ModItems.GRILLED_CHEESE.get(),
                        Component.translatable("advancements.nerdsoftkitchen.grilled_cheese.title"),
                        Component.translatable("advancements.nerdsoftkitchen.grilled_cheese.description"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false
                )
                .rewards(AdvancementRewards.Builder.experience(10))
                .addCriterion("has_grilled_cheese", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GRILLED_CHEESE.get()))
                .save(saver, id("make_grilled_cheese"));

//? if <1.21.2 {
        AdvancementHolder milkCup = Builder.advancement()
                .parent(ironCup)
                .display(
                        ironCupWith(IronCupContent.MILK),
                        Component.translatable("advancements.nerdsoftkitchen.milk_cup.title"),
                        Component.translatable("advancements.nerdsoftkitchen.milk_cup.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_milk_cup",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item()
                                .of(ModItems.IRON_CUP.get())
                                .hasComponents(DataComponentPredicate.allOf(ironCupWith(IronCupContent.MILK).getComponents()))
                                .build()))
                .save(saver, id("fill_cup_with_milk"));

        AdvancementHolder plainYogurt = Builder.advancement()
                .parent(milkCup)
                .display(
                        ironCupWith(IronCupContent.YOGURT),
                        Component.translatable("advancements.nerdsoftkitchen.yogurt.title"),
                        Component.translatable("advancements.nerdsoftkitchen.yogurt.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_yogurt",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item()
                                .of(ModItems.IRON_CUP.get())
                                .hasComponents(DataComponentPredicate.allOf(ironCupWith(IronCupContent.YOGURT).getComponents()))
                                .build()))
                .save(saver, id("make_yogurt"));

        AdvancementHolder strawberryYogurt = Builder.advancement()
                .parent(plainYogurt)
                .display(
                        ironCupWith(IronCupContent.STRAWBERRY_YOGURT),
                        Component.translatable("advancements.nerdsoftkitchen.strawberry_yogurt.title"),
                        Component.translatable("advancements.nerdsoftkitchen.strawberry_yogurt.description"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false
                )
                .rewards(AdvancementRewards.Builder.experience(10))
                .addCriterion("has_strawberry_yogurt",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item()
                                .of(ModItems.IRON_CUP.get())
                                .hasComponents(DataComponentPredicate.allOf(ironCupWith(IronCupContent.STRAWBERRY_YOGURT).getComponents()))
                                .build()))
                .save(saver, id("make_strawberry_yogurt"));

        // --- Capstone: requires every branch's final goal ---

        Builder.advancement()
                .parent(strawberryYogurt)
                .display(
                        ModItems.GRILLED_CHEESE.get(),
                        Component.translatable("advancements.nerdsoftkitchen.gourmet.title"),
                        Component.translatable("advancements.nerdsoftkitchen.gourmet.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, true
                )
                .rewards(AdvancementRewards.Builder.experience(50))
                .addCriterion("has_grilled_cheese", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GRILLED_CHEESE.get()))
                .addCriterion("has_strawberry_yogurt",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item()
                                .of(ModItems.IRON_CUP.get())
                                .hasComponents(DataComponentPredicate.allOf(ironCupWith(IronCupContent.STRAWBERRY_YOGURT).getComponents()))
                                .build()))
                .addCriterion("has_master_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHERITE_KNIFE.get()))
                .addCriterion("has_salad", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SALAD.get()))
                .requirements(AdvancementRequirements.Strategy.AND)
                .save(saver, id("gourmet_chef"));
        //?} else {
        /*HolderLookup.RegistryLookup<Item> itemRegistry = registries.lookupOrThrow(Registries.ITEM);

        AdvancementHolder milkCup = Builder.advancement()
                .parent(ironCup)
                .display(
                        ironCupWith(IronCupContent.MILK),
                        Component.translatable("advancements.nerdsoftkitchen.milk_cup.title"),
                        Component.translatable("advancements.nerdsoftkitchen.milk_cup.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_milk_cup",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item()
                                .of(itemRegistry, ModItems.IRON_CUP.get())
                                .hasComponents(DataComponentPredicate.allOf(ironCupWith(IronCupContent.MILK).getComponents()))
                                .build()))
                .save(saver, id("fill_cup_with_milk"));

        AdvancementHolder plainYogurt = Builder.advancement()
                .parent(milkCup)
                .display(
                        ironCupWith(IronCupContent.YOGURT),
                        Component.translatable("advancements.nerdsoftkitchen.yogurt.title"),
                        Component.translatable("advancements.nerdsoftkitchen.yogurt.description"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_yogurt",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item()
                                .of(itemRegistry, ModItems.IRON_CUP.get())
                                .hasComponents(DataComponentPredicate.allOf(ironCupWith(IronCupContent.YOGURT).getComponents()))
                                .build()))
                .save(saver, id("make_yogurt"));

        AdvancementHolder strawberryYogurt = Builder.advancement()
                .parent(plainYogurt)
                .display(
                        ironCupWith(IronCupContent.STRAWBERRY_YOGURT),
                        Component.translatable("advancements.nerdsoftkitchen.strawberry_yogurt.title"),
                        Component.translatable("advancements.nerdsoftkitchen.strawberry_yogurt.description"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false
                )
                .rewards(AdvancementRewards.Builder.experience(10))
                .addCriterion("has_strawberry_yogurt",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item()
                                .of(itemRegistry, ModItems.IRON_CUP.get())
                                .hasComponents(DataComponentPredicate.allOf(ironCupWith(IronCupContent.STRAWBERRY_YOGURT).getComponents()))
                                .build()))
                .save(saver, id("make_strawberry_yogurt"));


        // --- Capstone: requires every branch's final goal ---

        Builder.advancement()
                .parent(strawberryYogurt)
                .display(
                        ModItems.GRILLED_CHEESE.get(),
                        Component.translatable("advancements.nerdsoftkitchen.gourmet.title"),
                        Component.translatable("advancements.nerdsoftkitchen.gourmet.description"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, true
                )
                .rewards(AdvancementRewards.Builder.experience(50))
                .addCriterion("has_grilled_cheese", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GRILLED_CHEESE.get()))
                .addCriterion("has_strawberry_yogurt",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item()
                                .of(itemRegistry, ModItems.IRON_CUP.get())
                                .hasComponents(DataComponentPredicate.allOf(ironCupWith(IronCupContent.STRAWBERRY_YOGURT).getComponents()))
                                .build()))
                .addCriterion("has_master_knife", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.NETHERITE_KNIFE.get()))
                .addCriterion("has_salad", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.SALAD.get()))
                .requirements(AdvancementRequirements.Strategy.AND)
                .save(saver, id("gourmet_chef"));
         *///?}
    }

    private static ItemStack ironCupWith(IronCupContent content) {
        return IronCupItem.filled(ModItems.IRON_CUP.get(), content);
    }

    private static String id(String name) {
        return NerdSoftKitchen.MOD_ID + ":" + name;
    }
}