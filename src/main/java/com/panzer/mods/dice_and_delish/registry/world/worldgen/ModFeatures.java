package com.panzer.mods.dice_and_delish.registry.world.worldgen;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.worldgen.feature.TriplePlantFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModFeatures {

    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, DiceAndDelish.MOD_ID);

    public static final DeferredHolder<Feature<?>, TriplePlantFeature> TRIPLE_PLANT =
            FEATURES.register("triple_plant", () -> new TriplePlantFeature(TriplePlantFeature.Configuration.CODEC));

    private ModFeatures() {
    }

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
