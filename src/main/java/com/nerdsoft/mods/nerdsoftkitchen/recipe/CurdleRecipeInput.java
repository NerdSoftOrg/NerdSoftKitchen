package com.nerdsoft.mods.nerdsoftkitchen.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record CurdleRecipeInput(ItemStack base, ItemStack activator) implements RecipeInput {

    @Override
    public @NotNull ItemStack getItem(int index) {
        return index == 0 ? base : activator;
    }

    @Override
    public int size() {
        return 2;
    }
}
