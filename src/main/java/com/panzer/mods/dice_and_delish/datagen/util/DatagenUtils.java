package com.panzer.mods.dice_and_delish.datagen.util;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class DatagenUtils {

    public static void trackTexture(ExistingFileHelper helper, String path) {
        String cleanPath = path.endsWith(".png") ? path.substring(0, path.length() - 4) : path;
        helper.trackGenerated(
                ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, cleanPath),
                PackType.CLIENT_RESOURCES,
                ".png",
                "textures"
        );
    }

    public static void trackModel(ExistingFileHelper helper, String path) {
        String cleanPath = path.endsWith(".json") ? path.substring(0, path.length() - 5) : path;
        helper.trackGenerated(
                ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, cleanPath),
                PackType.CLIENT_RESOURCES,
                ".json",
                "models"
        );
    }

    public static void trackSound(ExistingFileHelper helper, String path) {
        String cleanPath = path.endsWith(".ogg") ? path.substring(0, path.length() - 4) : path;
        helper.trackGenerated(
                ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, cleanPath),
                PackType.CLIENT_RESOURCES,
                ".ogg",
                "sounds"
        );
    }
}
