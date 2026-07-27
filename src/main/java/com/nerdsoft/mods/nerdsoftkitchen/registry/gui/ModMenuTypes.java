package com.nerdsoft.mods.nerdsoftkitchen.registry.gui;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU,
            NerdSoftKitchen.MOD_ID);

    private ModMenuTypes() {
    }
}
