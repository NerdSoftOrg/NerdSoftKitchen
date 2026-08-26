package com.nerdsoft.mods.nerdsoftkitchen.registry.item;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;

public final class ModItemTags {

    public static final TagKey<Item> IRON_CUP = mod("iron_cup");
    public static final TagKey<Item> FRYING_PAN = c("tools/frying_pan");
    public static final TagKey<Item> KNIFE = c("tools/knife");
    public static final TagKey<Item> KNIFE_MIN_STONE = c("tools/knife/min_stone");
    public static final TagKey<Item> KNIFE_MIN_IRON = c("tools/knife/min_iron");
    public static final TagKey<Item> KNIFE_MIN_GOLD = c("tools/knife/min_gold");
    public static final TagKey<Item> KNIFE_MIN_DIAMOND = c("tools/knife/min_diamond");
    public static final TagKey<Item> KNIFE_MIN_OBSIDIAN = c("tools/knife/min_obsidian");
    public static final TagKey<Item> KNIFE_MIN_NETHERITE = c("tools/knife/min_netherite");
    public static final TagKey<Item> TORTILLAS = c("foods/tortillas");

    private ModItemTags() {
    }

    @SuppressWarnings("unused")
    public static void register(IEventBus eventBus) {
        NerdSoftKitchenLogger.info("Item Tags registered successfully.");
    }

    public static TagKey<Item> mod(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, path));
    }

    public static TagKey<Item> c(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
