package com.nerdsoft.mods.nerdsoftkitchen.recipe.curdle;

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

public record CurdleRecipe(Ingredient base, Ingredient activator,
                           ItemStack result) implements Recipe<CurdleRecipeInput> {

    @Override
    public boolean matches(@NotNull CurdleRecipeInput recipeInput, @NotNull Level level) {
        return base.test(recipeInput.base()) && activator.test(recipeInput.activator());
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CurdleRecipeInput recipeInput,
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
    public @NotNull RecipeSerializer<? extends Recipe<CurdleRecipeInput>> getSerializer() {
        return ModRecipeSerializers.CURDLE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<CurdleRecipeInput>> getType() {
        return ModRecipeTypes.CURDLE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<CurdleRecipe> {

        public static final com.mojang.serialization.MapCodec<CurdleRecipe> CODEC =
                com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(instance -> instance.group(Ingredient.CODEC.fieldOf("base").forGetter(CurdleRecipe::base), Ingredient.CODEC.fieldOf("activator").forGetter(CurdleRecipe::activator), ItemStack.CODEC.fieldOf("result").forGetter(CurdleRecipe::result)).apply(instance, CurdleRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, CurdleRecipe> STREAM_CODEC =
                StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, CurdleRecipe::base,
                        Ingredient.CONTENTS_STREAM_CODEC, CurdleRecipe::activator, ItemStack.STREAM_CODEC,
                        CurdleRecipe::result, CurdleRecipe::new);

        @Override
        public @NotNull com.mojang.serialization.MapCodec<CurdleRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, CurdleRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
