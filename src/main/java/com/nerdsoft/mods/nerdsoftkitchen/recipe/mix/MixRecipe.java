package com.nerdsoft.mods.nerdsoftkitchen.recipe.mix;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nerdsoft.mods.nerdsoftkitchen.registry.recipe.ModRecipeSerializers;
import com.nerdsoft.mods.nerdsoftkitchen.registry.recipe.ModRecipeTypes;
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

import java.util.List;

public record MixRecipe(List<Ingredient> inputs, ItemStack result) implements Recipe<MixRecipeInput> {

    @Override
    public boolean matches(@NotNull MixRecipeInput recipeInput, @NotNull Level level) {
        int size = recipeInput.size();
        if (size != inputs.size()) return false;
        boolean[] consumed = new boolean[size];
        for (Ingredient ingredient : inputs) {
            boolean found = false;
            for (int i = 0; i < size; i++) {
                if (!consumed[i] && ingredient.test(recipeInput.getItem(i))) {
                    consumed[i] = true;
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
        return result.copy();
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

        public static final MapCodec<MixRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Ingredient.CODEC.listOf().fieldOf("inputs").forGetter(MixRecipe::inputs),
                                ItemStack.CODEC.fieldOf("result").forGetter(MixRecipe::result)
                        )
                        .apply(instance, MixRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, MixRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                MixRecipe::inputs,
                ItemStack.STREAM_CODEC,
                MixRecipe::result,
                MixRecipe::new
        );

        @Override
        public @NotNull MapCodec<MixRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, MixRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
