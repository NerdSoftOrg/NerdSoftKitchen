package com.panzer.mods.dice_and_delish.registry.data;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.util.ModLogger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.bus.api.IEventBus;

public final class ModDamageTypes {

    public static final ResourceKey<DamageType> COOKWARE_BURN = create("cookware_burn");

    private ModDamageTypes() {
    }

    @SuppressWarnings("unused")
    public static void register(IEventBus eventBus) {
        ModLogger.debug("Damage Types registered successfully.");
    }

    @SuppressWarnings("SameParameterValue")
    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE,
                ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, name));
    }
}
