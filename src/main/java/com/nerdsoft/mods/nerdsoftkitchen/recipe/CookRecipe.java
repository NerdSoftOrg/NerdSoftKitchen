package com.nerdsoft.mods.nerdsoftkitchen.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModRecipeSerializers;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record CookRecipe(Ingredient input, ItemStack result, int cookingTime) implements Recipe<CookRecipeInput> {

    public static final int DEFAULT_COOKING_TIME = 200;

    @Override
    public boolean matches(@NotNull CookRecipeInput recipeInput, @NotNull Level level) {
        return input.test(recipeInput.item());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CookRecipeInput recipeInput,
                                       HolderLookup.@NotNull Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result;
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<CookRecipeInput>> getSerializer() {
        return ModRecipeSerializers.COOK_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<CookRecipeInput>> getType() {
        return ModRecipeTypes.COOK_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<CookRecipe> {

        public static final MapCodec<CookRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Ingredient.CODEC.fieldOf("input").forGetter(CookRecipe::input),
                                ItemStack.CODEC.fieldOf("result").forGetter(CookRecipe::result),
                                Codec.intRange(1, 20000)
                                        .optionalFieldOf("cookingtime", DEFAULT_COOKING_TIME)
                                        .forGetter(CookRecipe::cookingTime)
                        )
                        .apply(instance, CookRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, CookRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC,
                CookRecipe::input,
                ItemStack.STREAM_CODEC,
                CookRecipe::result,
                ByteBufCodecs.VAR_INT,
                CookRecipe::cookingTime,
                CookRecipe::new
        );

        @Override
        public @NotNull MapCodec<CookRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, CookRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}