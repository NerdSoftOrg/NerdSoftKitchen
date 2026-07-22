package com.nerdsoft.mods.nerdsoftkitchen.datagen;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import java.util.function.Supplier;

public class ModSoundDefinitionsProvider extends SoundDefinitionsProvider {

    public ModSoundDefinitionsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NerdSoftKitchen.MOD_ID, existingFileHelper);
    }

    @Override
    public void registerSounds() {
        addSound(ModSounds.GRILL_SIZZLE, "block/grill/sizzle", "block.nerdsoftkitchen.grill.sizzle");
        addSound(ModSounds.GRILL_PLACE_FOOD, "block/grill/place_food", "block.nerdsoftkitchen.grill.place_food");

        addLoopingSound(ModSounds.GRILL_GRILLING, "block/grill/grilling", "block.nerdsoftkitchen.grill.grilling");
    }

    private void addSound(Supplier<SoundEvent> soundEvent, String path, String subtitleKey) {
        addSound(soundEvent, path, subtitleKey, false);
    }

    private void addLoopingSound(Supplier<SoundEvent> soundEvent, String path, String subtitleKey) {
        addSound(soundEvent, path, subtitleKey, true);
    }

    private void addSound(Supplier<SoundEvent> soundEvent, String path, String subtitleKey, boolean isLooping) {
        boolean shouldPreload = !isLooping;

        add(soundEvent, SoundDefinition.definition()
                .with(sound(id(path)).preload(shouldPreload))
                .subtitle("subtitles." + subtitleKey));
    }

    private ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, path);
    }
}