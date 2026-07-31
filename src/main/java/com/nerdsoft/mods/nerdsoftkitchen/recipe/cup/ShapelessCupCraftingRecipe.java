package com.nerdsoft.mods.nerdsoftkitchen.recipe.cup;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.registry.recipe.ModRecipeSerializers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ShapelessCupCraftingRecipe(List<Ingredient> ingredients,
                                         ItemStack result) implements CraftingRecipe {

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        int inputSize = input.size();
        int nonEmptyCount = 0;
        for (int i = 0; i < inputSize; i++) {
            if (!input.getItem(i).isEmpty()) {
                nonEmptyCount++;
            }
        }
        if (nonEmptyCount != ingredients.size()) {
            return false;
        }
        boolean[] consumed = new boolean[ingredients.size()];
        for (int i = 0; i < inputSize; i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            boolean found = false;
            for (int j = 0; j < ingredients.size(); j++) {
                if (!consumed[j] && ingredients.get(j).test(stack)) {
                    consumed[j] = true;
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull CraftingInput input, HolderLookup.@NotNull Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= ingredients.size();
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull CraftingInput input) {
        int inputSize = input.size();
        NonNullList<ItemStack> remaining = NonNullList.withSize(inputSize, ItemStack.EMPTY);
        for (int i = 0; i < inputSize; i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && !IronCupItem.isEmpty(stack)) {
                remaining.set(i, new ItemStack(stack.getItem()));
            }
        }
        return remaining;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result.copy();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return NonNullList.copyOf(ingredients);
    }

    @Override
    public @NotNull CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<CraftingInput>> getSerializer() {
        return ModRecipeSerializers.SHAPELESS_CUP_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<CraftingInput>> getType() {
        return RecipeType.CRAFTING;
    }

    public static class Serializer implements RecipeSerializer<ShapelessCupCraftingRecipe> {

        public static final MapCodec<ShapelessCupCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(ShapelessCupCraftingRecipe::ingredients),
                        ItemStack.CODEC.fieldOf("result").forGetter(ShapelessCupCraftingRecipe::result)
                ).apply(instance, ShapelessCupCraftingRecipe::new)
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessCupCraftingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
                        ShapelessCupCraftingRecipe::ingredients,
                        ItemStack.STREAM_CODEC,
                        ShapelessCupCraftingRecipe::result,
                        ShapelessCupCraftingRecipe::new
                );

        @Override
        public @NotNull MapCodec<ShapelessCupCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ShapelessCupCraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}