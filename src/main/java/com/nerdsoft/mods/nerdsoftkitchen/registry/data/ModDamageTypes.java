package com.nerdsoft.mods.nerdsoftkitchen.registry.data;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public final class ModDamageTypes {

    public static final ResourceKey<DamageType> GRILL_BURN = create("grill_burn");

    private ModDamageTypes() {
    }

    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE,
                ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, name));
    }
}