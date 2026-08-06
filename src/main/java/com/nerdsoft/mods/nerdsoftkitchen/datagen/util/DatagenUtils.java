package com.nerdsoft.mods.nerdsoftkitchen.datagen.util;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class DatagenUtils {

    public static void trackTexture(ExistingFileHelper helper, String path) {
        helper.trackGenerated(ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, path), PackType.CLIENT_RESOURCES, ".png", "textures");
    }

    public static void trackModel(ExistingFileHelper helper, String path) {
        helper.trackGenerated(ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, path), PackType.CLIENT_RESOURCES, ".json", "models");
    }

    public static void trackSound(ExistingFileHelper helper, String path) {
        helper.trackGenerated(ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, path), PackType.CLIENT_RESOURCES, ".ogg", "sounds");
    }
}