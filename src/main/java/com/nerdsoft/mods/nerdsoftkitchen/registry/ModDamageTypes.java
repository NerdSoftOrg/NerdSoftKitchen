package com.nerdsoft.mods.nerdsoftkitchen.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public final class ModDamageTypes {

    public static final ResourceKey<DamageType> GRILL_BURN = create("grill_burn");

    private ModDamageTypes() {
    }

    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(net.minecraft.core.registries.Registries.DAMAGE_TYPE,
                ResourceLocation.fromNamespaceAndPath("nerdsoftkitchen", name));
    }
}