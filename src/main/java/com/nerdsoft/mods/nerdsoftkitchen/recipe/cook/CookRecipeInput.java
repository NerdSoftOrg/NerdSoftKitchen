package com.nerdsoft.mods.nerdsoftkitchen.recipe.cook;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record CookRecipeInput(ItemStack item) implements RecipeInput {

    @Override
    public @NotNull ItemStack getItem(int index) {
        return item;
    }

    @Override
    public int size() {
        return 1;
    }
}
