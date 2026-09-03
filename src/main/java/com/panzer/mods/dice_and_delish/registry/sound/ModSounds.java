package com.panzer.mods.dice_and_delish.registry.sound;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.util.ModLogger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModSounds {

    private static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, DiceAndDelish.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GRILL_SIZZLE = SOUNDS.register("block.grill.sizzle",
            () -> SoundEvent.createVariableRangeEvent(id("block/grill/sizzle")));

    public static final DeferredHolder<SoundEvent, SoundEvent> GRILL_GRILLING = SOUNDS.register("block.grill.grilling",
            () -> SoundEvent.createVariableRangeEvent(id("block/grill/grilling")));

    public static final DeferredHolder<SoundEvent, SoundEvent> GRILL_PLACE_FOOD = SOUNDS.register("block.grill.place_food",
            () -> SoundEvent.createVariableRangeEvent(id("block/grill/place_food")));

    public static final DeferredHolder<SoundEvent, SoundEvent> SKILLET_SIZZLE_LOOP = SOUNDS.register("block.skillet.sizzle_loop",
            () -> SoundEvent.createVariableRangeEvent(id("block/skillet/sizzle_loop")));

    public static final DeferredHolder<SoundEvent, SoundEvent> SKILLET_CLANG = SOUNDS.register("item.skillet.clang",
            () -> SoundEvent.createVariableRangeEvent(id("item/skillet/clang")));

    private ModSounds() {
    }

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
        ModLogger.info("Sounds registered successfully.");
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, path);
    }
}
