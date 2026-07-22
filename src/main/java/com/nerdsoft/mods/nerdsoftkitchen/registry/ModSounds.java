package com.nerdsoft.mods.nerdsoftkitchen.registry;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, NerdSoftKitchen.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GRILL_SIZZLE = SOUNDS.register("block.grill.sizzle",
            () -> SoundEvent.createVariableRangeEvent(id("block/grill/sizzle")));

    public static final DeferredHolder<SoundEvent, SoundEvent> GRILL_GRILLING = SOUNDS.register("block.grill.grilling",
            () -> SoundEvent.createVariableRangeEvent(id("block/grill/grilling")));

    public static final DeferredHolder<SoundEvent, SoundEvent> GRILL_PLACE_FOOD = SOUNDS.register("block.grill.place_food",
            () -> SoundEvent.createVariableRangeEvent(id("block/grill/place_food")));

    private ModSounds() {
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, path);
    }
}