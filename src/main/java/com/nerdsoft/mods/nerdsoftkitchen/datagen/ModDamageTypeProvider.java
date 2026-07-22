package com.nerdsoft.mods.nerdsoftkitchen.datagen;

import com.nerdsoft.mods.nerdsoftkitchen.registry.ModDamageTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

public final class ModDamageTypeProvider {

    private static final float GRILL_BURN_EXHAUSTION = 0.1F;

    private ModDamageTypeProvider() {
    }

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(ModDamageTypes.GRILL_BURN, new DamageType(
                "onFire",
                DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
                GRILL_BURN_EXHAUSTION,
                DamageEffects.BURNING,
                DeathMessageType.FALL_VARIANTS
        ));
    }
}