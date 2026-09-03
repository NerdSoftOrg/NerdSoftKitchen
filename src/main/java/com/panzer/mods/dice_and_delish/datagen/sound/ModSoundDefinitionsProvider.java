package com.panzer.mods.dice_and_delish.datagen.sound;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.datagen.util.DatagenUtils;
import com.panzer.mods.dice_and_delish.registry.sound.ModSounds;
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
        super(output, DiceAndDelish.MOD_ID, existingFileHelper);
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    public void registerSounds() {
        addSound(ModSounds.GRILL_PLACE_FOOD, "block/grill/place_food", "block.dice_and_delish.grill.place_food");

        addLoopingSound(ModSounds.GRILL_GRILLING, "block/grill/grilling", "block.dice_and_delish.grill.grilling");
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
        return ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, path);
    }
}
