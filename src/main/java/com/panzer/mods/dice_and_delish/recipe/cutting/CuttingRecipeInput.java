package com.panzer.mods.dice_and_delish.recipe.cutting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record CuttingRecipeInput(ItemStack item, ItemStack knife) implements RecipeInput {

    public CuttingRecipeInput(ItemStack item) {
        this(item, ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        return index == 0 ? item : knife;
    }

    @Override
    public int size() {
        return 2;
    }
}
