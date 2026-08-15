package com.nerdsoft.mods.nerdsoftkitchen.registry.gui;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NerdSoftKitchen.MOD_ID);

    @SuppressWarnings("unused")
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> KITCHEN_TAB = CREATIVE_TABS.register("kitchen_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.nerdsoftkitchen" + ".kitchen_tab")).icon(() -> new ItemStack(ModItems.GRILL_TABLE.get())).displayItems((parameters, output) -> {
        output.accept(ModItems.GRILL_TABLE);
        output.accept(ModItems.GRILL_TABLE_SOUL);
        output.accept(ModItems.CUTTING_BOARD);
        output.accept(ModItems.WILD_STRAWBERRY);
        output.accept(ModItems.WILD_TOMATO);
        output.accept(ModItems.WILD_LETTUCE);
        output.accept(ModItems.WILD_PURPLE_ONION);
        output.accept(ModItems.STRAWBERRY_SEEDS);
        output.accept(ModItems.TOMATO_SEEDS);
        output.accept(ModItems.LETTUCE_SEEDS);
        output.accept(ModItems.PURPLE_ONION_SEEDS);
        output.accept(ModItems.STRAWBERRY);
        output.accept(ModItems.TOMATO);
        output.accept(ModItems.LETTUCE);
        output.accept(ModItems.PURPLE_ONION);
        output.accept(ModItems.IRON_CUP);
        output.accept(IronCupItem.filled(ModItems.IRON_CUP.get(), IronCupContent.MILK));
        output.accept(IronCupItem.filled(ModItems.IRON_CUP.get(), IronCupContent.YOGURT));
        output.accept(IronCupItem.filled(ModItems.IRON_CUP.get(), IronCupContent.STRAWBERRY_YOGURT));
        output.accept(ModItems.RAW_CHICKEN_PIECES);
        output.accept(ModItems.COOKED_CHICKEN_PIECES);
        output.accept(ModItems.FRIED_EGG);
        output.accept(ModItems.SALAD);
        output.accept(ModItems.RAW_SANDWICH_BREAD);
        output.accept(ModItems.CHEESE_RAW_SANDWICH);
        output.accept(ModItems.TOASTED_SANDWICH_BREAD);
        output.accept(ModItems.CHEESE_TOASTED_SANDWICH);
        output.accept(ModItems.CHEESE);
        output.accept(ModItems.CHEESE_SLICE);
        output.accept(ModItems.GRILLED_CHEESE);
        output.accept(ModItems.STONE_KNIFE);
        output.accept(ModItems.IRON_KNIFE);
        output.accept(ModItems.GOLD_KNIFE);
        output.accept(ModItems.DIAMOND_KNIFE);
        output.accept(ModItems.OBSIDIAN_KNIFE);
        output.accept(ModItems.NETHERITE_KNIFE);
//        output.accept(ModItems.ORGANIC_MIXTURE);
//        output.accept(ModItems.FERTILE_FARMLAND);
//        output.accept(ModItems.ORGANIC_SOIL);
    }).build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
        NerdSoftKitchenLogger.info("Creative Tabs registered successfully.");
    }
}