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

    public static final TagKey<Item> FRYING_PANS = tag("c", "tools/frying_pans");
    public static final TagKey<Item> KNIFE = tag("c", "tools/knife");
    public static final TagKey<Item> KNIFE_MIN_STONE = tag("c", "tools/knife/min_stone");
    public static final TagKey<Item> KNIFE_MIN_IRON = tag("c", "tools/knife/min_iron");
    public static final TagKey<Item> KNIFE_MIN_GOLD = tag("c", "tools/knife/min_gold");
    public static final TagKey<Item> KNIFE_MIN_DIAMOND = tag("c", "tools/knife/min_diamond");
    public static final TagKey<Item> KNIFE_MIN_OBSIDIAN = tag("c", "tools/knife/min_obsidian");
    public static final TagKey<Item> KNIFE_MIN_NETHERITE = tag("c", "tools/knife/min_netherite");

    public static final TagKey<Item> TORTILLAS = tag("c", "foods/tortillas");

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
