package com.nerdsoft.mods.nerdsoftkitchen.registry.data;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.bus.api.IEventBus;

public final class ModDamageTypes {

    public static final ResourceKey<DamageType> GRILL_BURN = create("grill_burn");

    private ModDamageTypes() {
    }

    @SuppressWarnings("unused")
    public static void register(IEventBus eventBus) {
        NerdSoftKitchenLogger.info("Damage Types registered successfully.");
    }

    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE,
                ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, name));
    }
}