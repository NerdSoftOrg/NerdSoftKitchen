package com.panzer.mods.dice_and_delish.recipe.cutting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.panzer.mods.dice_and_delish.registry.tags.ModItemTags;
import com.panzer.mods.dice_and_delish.registry.recipe.ModRecipeSerializers;
import com.panzer.mods.dice_and_delish.registry.recipe.ModRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

//? if >=1.21.2 {
/*import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategories;

import java.util.List;
*///?}

public record CuttingRecipe(Ingredient input, ItemStack result, Ingredient knife) implements Recipe<CuttingRecipeInput> {

    public CuttingRecipe(Ingredient input, ItemStack result) {
        this(input, result, defaultKnifeIngredient(null));
    }

    public CuttingRecipe(Ingredient input, ItemStack result, HolderLookup.Provider registries) {
        this(input, result, defaultKnifeIngredient(registries));
    }

    public static Ingredient defaultKnifeIngredient(HolderLookup.Provider registries) {
        //? if <1.21.2 {
        return Ingredient.of(ModItemTags.KNIFE);
        //?} else {
        /*if (registries != null) {
            var itemLookup = registries.lookupOrThrow(Registries.ITEM);
            return Ingredient.of(itemLookup.getOrThrow(ModItemTags.KNIFE));
        }
        return Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(ModItemTags.KNIFE));
        *///?}
    }

    @Override
    public boolean matches(@NotNull CuttingRecipeInput recipeInput, @NotNull Level level) {
        if (!input.test(recipeInput.item())) {
            return false;
        }
        ItemStack heldKnife = recipeInput.knife();
        return heldKnife.isEmpty() || knife.test(heldKnife);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CuttingRecipeInput recipeInput,
                                       HolderLookup.@NotNull Provider registries) {
        return result.copy();
    }

    //? if <1.21.2 {
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CUT_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.CUT_TYPE.get();
    }
    //?} else {
    /*@Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(List.of(input));
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public @NotNull RecipeSerializer<CuttingRecipe> getSerializer() {
        return ModRecipeSerializers.CUT_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<CuttingRecipe> getType() {
        return ModRecipeTypes.CUT_TYPE.get();
    }
    *///?}

    public static class Serializer implements RecipeSerializer<CuttingRecipe> {

        public static final MapCodec<CuttingRecipe> CODEC =
                RecordCodecBuilder.mapCodec(instance -> instance.group(
                                Ingredient.CODEC.fieldOf("input").forGetter(CuttingRecipe::input),
                                ItemStack.CODEC.fieldOf("result").forGetter(CuttingRecipe::result),
                                Ingredient.CODEC
                                        .optionalFieldOf("knife")
                                        .forGetter(recipe -> java.util.Optional.of(recipe.knife())))
                        .apply(instance, (input, result, knifeOpt) ->
                                new CuttingRecipe(input, result, knifeOpt.orElseGet(() -> CuttingRecipe.defaultKnifeIngredient(null)))));

        public static final StreamCodec<RegistryFriendlyByteBuf, CuttingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, CuttingRecipe::input,
                        ItemStack.STREAM_CODEC, CuttingRecipe::result,
                        Ingredient.CONTENTS_STREAM_CODEC, CuttingRecipe::knife,
                        CuttingRecipe::new);

        @Override
        public @NotNull MapCodec<CuttingRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, CuttingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
