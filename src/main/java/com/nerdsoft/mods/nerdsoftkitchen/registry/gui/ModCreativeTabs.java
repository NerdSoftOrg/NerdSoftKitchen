package com.nerdsoft.mods.nerdsoftkitchen.registry.gui;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, NerdSoftKitchen.MOD_ID);
    @SuppressWarnings("unused")
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> KITCHEN_TAB = CREATIVE_TABS.register("kitchen_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.nerdsoftkitchen" + ".kitchen_tab")).icon(() -> new ItemStack(ModItems.GRILL_TABLE.get())).displayItems((parameters, output) -> {
        output.accept(ModItems.GRILL_TABLE.get());
        output.accept(ModItems.GRILL_TABLE_SOUL.get());
        output.accept(ModItems.WILD_STRAWBERRY.get());
        output.accept(ModItems.WILD_TOMATO.get());
        output.accept(ModItems.WILD_LETTUCE.get());
        output.accept(ModItems.WILD_PURPLE_ONION.get());
        output.accept(ModItems.STRAWBERRY_SEEDS.get());
        output.accept(ModItems.TOMATO_SEEDS.get());
        output.accept(ModItems.LETTUCE_SEEDS.get());
        output.accept(ModItems.PURPLE_ONION_SEEDS.get());
        output.accept(ModItems.STRAWBERRY.get());
        output.accept(ModItems.TOMATO.get());
        output.accept(ModItems.LETTUCE.get());
        output.accept(ModItems.PURPLE_ONION.get());
        output.accept(ModItems.IRON_CUP.get());
        output.accept(IronCupItem.filled(ModItems.IRON_CUP.get(), IronCupContent.MILK));
        output.accept(IronCupItem.filled(ModItems.IRON_CUP.get(), IronCupContent.YOGURT));
        output.accept(IronCupItem.filled(ModItems.IRON_CUP.get(), IronCupContent.STRAWBERRY_YOGURT));
        output.accept(ModItems.RAW_CHICKEN_PIECES.get());
        output.accept(ModItems.COOKED_CHICKEN_PIECES.get());
        output.accept(ModItems.FRIED_EGG.get());
        output.accept(ModItems.SALAD.get());
    }).build());

    private ModCreativeTabs() {
    }
}