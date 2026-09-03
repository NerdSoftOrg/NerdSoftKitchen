package com.panzer.mods.dice_and_delish.datagen.recipe;

import com.panzer.mods.dice_and_delish.recipe.cook.CookRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
//? if >=1.21.2 {
/*import net.minecraft.resources.ResourceKey;
*///?}
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
//? if <=1.21.1 {
import net.minecraft.resources.ResourceLocation;
//?}
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
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

    //? if <1.21.2 {
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
    //?} else {
    /*@Override
    public void save(@NotNull RecipeOutput output, @NotNull ResourceKey<Recipe<?>> key) {
        Advancement.Builder advancement = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                .rewards(AdvancementRewards.Builder.recipe(key))
                .requirements(AdvancementRequirements.Strategy.OR);
        criteria.forEach(advancement::addCriterion);

        CookRecipe recipe = new CookRecipe(input, result, cookingTime);
        output.accept(key, recipe, advancement.build(key.location().withPrefix("recipes/")));
    }
    *///?}
}
