package com.nerdsoft.mods.nerdsoftkitchen.registry;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

//? if <1.21.2 {
/*import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
*///?} else {
import net.minecraft.world.item.ToolMaterial;
 //?}

public final class ModTiers {

    private static final int OBSIDIAN_DURABILITY = 3500;
    private static final float OBSIDIAN_SPEED = 9.0F;
    private static final float OBSIDIAN_ATTACK_DAMAGE_BONUS = 4.0F;
    private static final int OBSIDIAN_ENCHANTMENT_VALUE = 12;

    public static final TagKey<Item> REPAIRS_OBSIDIAN_KNIFE =
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "repairs_obsidian_knife"));

    //? if <1.21.2 {
    /*public static final Tier OBSIDIAN = new SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            OBSIDIAN_DURABILITY,
            OBSIDIAN_SPEED,
            OBSIDIAN_ATTACK_DAMAGE_BONUS,
            OBSIDIAN_ENCHANTMENT_VALUE,
            () -> Ingredient.of(Items.OBSIDIAN)
    );
    *///?} else {
    public static final ToolMaterial OBSIDIAN = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            OBSIDIAN_DURABILITY,
            OBSIDIAN_SPEED,
            OBSIDIAN_ATTACK_DAMAGE_BONUS,
            OBSIDIAN_ENCHANTMENT_VALUE,
            REPAIRS_OBSIDIAN_KNIFE
    );
    //?}

    private ModTiers() {
    }
}