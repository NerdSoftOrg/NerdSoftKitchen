package com.nerdsoft.mods.nerdsoftkitchen.datagen.data;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.registry.data.ModDamageTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

public final class ModDamageTypeProvider {

    private static final float COOKWARE_BURN_EXHAUSTION = 0.1F;

    private ModDamageTypeProvider() {
    }

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(ModDamageTypes.COOKWARE_BURN, new DamageType(
                NerdSoftKitchen.MOD_ID + ".cookware_burn",
                DamageScaling.NEVER,
                COOKWARE_BURN_EXHAUSTION,
                DamageEffects.BURNING,
                DeathMessageType.DEFAULT
        ));
    }
}
