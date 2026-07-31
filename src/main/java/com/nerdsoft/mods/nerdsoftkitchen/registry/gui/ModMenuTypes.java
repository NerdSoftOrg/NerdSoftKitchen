package com.nerdsoft.mods.nerdsoftkitchen.registry.gui;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenuTypes {

    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU,
            NerdSoftKitchen.MOD_ID);

    private ModMenuTypes() {
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
        NerdSoftKitchenLogger.info("Menu Types registered successfully.");
    }
}