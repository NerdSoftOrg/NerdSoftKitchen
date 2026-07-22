package com.nerdsoft.mods.nerdsoftkitchen.recipe;

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

import java.util.ArrayList;
import java.util.List;

public record MixRecipe(List<Ingredient> inputs, ItemStack result) implements Recipe<MixRecipeInput> {

    @Override
    public boolean matches(@NotNull MixRecipeInput recipeInput, @NotNull Level level) {
        if (recipeInput.size() != inputs.size()) return false;
        List<ItemStack> remaining = new ArrayList<>(recipeInput.items());
        for (Ingredient ingredient : inputs) {
            boolean found = false;
            for (int i = 0; i < remaining.size(); i++) {
                if (ingredient.test(remaining.get(i))) {
                    remaining.remove(i);
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull MixRecipeInput recipeInput, HolderLookup.@NotNull Provider registries) {
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
    public @NotNull RecipeSerializer<? extends Recipe<MixRecipeInput>> getSerializer() {
        return ModRecipeSerializers.MIX_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<MixRecipeInput>> getType() {
        return ModRecipeTypes.MIX_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<MixRecipe> {

        public static final com.mojang.serialization.MapCodec<MixRecipe> CODEC =
                com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(instance -> instance.group(Ingredient.CODEC.listOf().fieldOf("inputs").forGetter(MixRecipe::inputs), ItemStack.CODEC.fieldOf("result").forGetter(MixRecipe::result)).apply(instance, MixRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, MixRecipe> STREAM_CODEC =
                StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), MixRecipe::inputs
                        , ItemStack.STREAM_CODEC, MixRecipe::result, MixRecipe::new);

        @Override
        public @NotNull com.mojang.serialization.MapCodec<MixRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, MixRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
