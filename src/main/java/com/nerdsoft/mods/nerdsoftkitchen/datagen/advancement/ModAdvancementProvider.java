package com.nerdsoft.mods.nerdsoftkitchen.datagen.advancement;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.advancements.AdvancementHolder;
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

    @SuppressWarnings("unused")
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

//? if <1.21.2 {
        /*AdvancementHolder milkCup = Builder.advancement()
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

        AdvancementHolder strawberryYogurt = Builder.advancement()
                .parent(milkCup)
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
        *///?} else {
        HolderLookup.RegistryLookup<Item> itemRegistry = registries.lookupOrThrow(Registries.ITEM);

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

        AdvancementHolder strawberryYogurt = Builder.advancement()
                .parent(milkCup)
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
        //?}

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
    }

    private static ItemStack ironCupWith(IronCupContent content) {
        return IronCupItem.filled(ModItems.IRON_CUP.get(), content);
    }

    private static String id(String name) {
        return NerdSoftKitchen.MOD_ID + ":" + name;
    }
}