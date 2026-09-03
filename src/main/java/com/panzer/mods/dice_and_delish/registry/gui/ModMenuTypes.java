package com.panzer.mods.dice_and_delish.registry.gui;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.util.ModLogger;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenuTypes {

    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU,
            DiceAndDelish.MOD_ID);

    private ModMenuTypes() {
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
        ModLogger.info("Menu Types registered successfully.");
    }
}
