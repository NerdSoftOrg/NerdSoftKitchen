package com.nerdsoft.mods.nerdsoftkitchen.registry.item;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

public final class ModItemTags {

    public static final TagKey<Item> IRON_CUP = tag(NerdSoftKitchen.MOD_ID, "iron_cup");
    public static final TagKey<Item> KNIFE = tag("c", "tools/knife");

    private ModItemTags() {
    }

    @SuppressWarnings("unused")
    public static void register(IEventBus eventBus) {
        NerdSoftKitchenLogger.info("Item Tags registered successfully.");
    }

    private static TagKey<Item> tag(String namespace, String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(namespace, name));
    }
}