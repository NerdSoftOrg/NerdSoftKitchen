package com.nerdsoft.mods.nerdsoftkitchen.recipe.cutting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nerdsoft.mods.nerdsoftkitchen.registry.recipe.ModRecipeSerializers;
import com.nerdsoft.mods.nerdsoftkitchen.registry.recipe.ModRecipeTypes;
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
/*import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategories;

import java.util.List;
*///?}

public record CuttingRecipe(Ingredient input, ItemStack result) implements Recipe<CuttingRecipeInput> {

    @Override
    public boolean matches(@NotNull CuttingRecipeInput recipeInput, @NotNull Level level) {
        return input.test(recipeInput.item());
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
                                ItemStack.CODEC.fieldOf("result").forGetter(CuttingRecipe::result))
                        .apply(instance, CuttingRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CuttingRecipe> STREAM_CODEC =
                StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, CuttingRecipe::input,
                        ItemStack.STREAM_CODEC, CuttingRecipe::result, CuttingRecipe::new);

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
