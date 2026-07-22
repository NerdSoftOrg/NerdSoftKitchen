package com.nerdsoft.mods.nerdsoftkitchen.datagen;

import com.nerdsoft.mods.nerdsoftkitchen.recipe.CookRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class CookRecipeBuilder implements RecipeBuilder {

    private final Ingredient input;
    private final ItemStack result;
    private final int cookingTime;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    private CookRecipeBuilder(Ingredient input, ItemStack result, int cookingTime) {
        this.input = input;
        this.result = result;
        this.cookingTime = cookingTime;
    }

    public static CookRecipeBuilder cooking(Ingredient input, ItemStack result, int cookingTime) {
        return new CookRecipeBuilder(input, result, cookingTime);
    }

    @Override
    public @NotNull CookRecipeBuilder unlockedBy(@NotNull String name, @NotNull Criterion<?> criterion) {
        criteria.put(name, criterion);
        return this;
    }

    @Override
    public @NotNull CookRecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(@NotNull RecipeOutput output, @NotNull ResourceLocation id) {
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        criteria.forEach(advancement::addCriterion);

        CookRecipe recipe = new CookRecipe(input, result, cookingTime);
        output.accept(id, recipe, advancement.build(id.withPrefix("recipes/")));
    }
}