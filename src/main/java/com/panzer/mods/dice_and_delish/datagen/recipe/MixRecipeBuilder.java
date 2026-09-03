package com.panzer.mods.dice_and_delish.datagen.recipe;

import com.panzer.mods.dice_and_delish.recipe.mix.MixRecipe;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MixRecipeBuilder implements RecipeBuilder {

    private final List<Ingredient> inputs;
    private final ItemStack result;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    private MixRecipeBuilder(List<Ingredient> inputs, ItemStack result) {
        this.inputs = inputs;
        this.result = result;
    }

    public static MixRecipeBuilder mixing(ItemStack result, Ingredient... inputs) {
        return new MixRecipeBuilder(new ArrayList<>(List.of(inputs)), result);
    }

    public static MixRecipeBuilder mixing(ItemStack result, List<Ingredient> inputs) {
        return new MixRecipeBuilder(new ArrayList<>(inputs), result);
    }

    @Override
    public @NotNull MixRecipeBuilder unlockedBy(@NotNull String name, @NotNull Criterion<?> criterion) {
        criteria.put(name, criterion);
        return this;
    }

    @Override
    public @NotNull MixRecipeBuilder group(@Nullable String groupName) {
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

        MixRecipe recipe = new MixRecipe(inputs, result);
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

        MixRecipe recipe = new MixRecipe(inputs, result);
        output.accept(key, recipe, advancement.build(key.location().withPrefix("recipes/")));
    }
    *///?}
}
