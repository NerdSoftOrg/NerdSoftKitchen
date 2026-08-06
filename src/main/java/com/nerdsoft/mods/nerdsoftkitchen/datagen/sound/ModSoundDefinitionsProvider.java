package com.nerdsoft.mods.nerdsoftkitchen.datagen.sound;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.util.DatagenUtils;
import com.nerdsoft.mods.nerdsoftkitchen.registry.sound.ModSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import java.util.function.Supplier;

public class ModSoundDefinitionsProvider extends SoundDefinitionsProvider {

    private final ExistingFileHelper existingFileHelper;

    public ModSoundDefinitionsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NerdSoftKitchen.MOD_ID, existingFileHelper);
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    public void registerSounds() {
        addSound(ModSounds.GRILL_PLACE_FOOD, "block/grill/place_food", "block.nerdsoftkitchen.grill.place_food");

        addLoopingSound(ModSounds.GRILL_GRILLING, "block/grill/grilling", "block.nerdsoftkitchen.grill.grilling");
    }

    @SuppressWarnings("SameParameterValue")
    private void addSound(Supplier<SoundEvent> soundEvent, String path, String subtitleKey) {
        addSound(soundEvent, path, subtitleKey, false);
    }

    @SuppressWarnings("SameParameterValue")
    private void addLoopingSound(Supplier<SoundEvent> soundEvent, String path, String subtitleKey) {
        addSound(soundEvent, path, subtitleKey, true);
    }

    private void addSound(Supplier<SoundEvent> soundEvent, String path, String subtitleKey, boolean isLooping) {
        boolean shouldPreload = !isLooping;

        DatagenUtils.trackSound(this.existingFileHelper, path);

        add(soundEvent, SoundDefinition.definition()
                .with(sound(id(path)).preload(shouldPreload))
                .subtitle("subtitles." + subtitleKey));
    }

    private ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, path);
    }
}