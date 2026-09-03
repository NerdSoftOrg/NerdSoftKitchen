package com.panzer.mods.dice_and_delish.datagen.advancement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.panzer.mods.dice_and_delish.recipe.mix.MixRecipeInput;
import com.panzer.mods.dice_and_delish.registry.recipe.ModRecipeSerializers;
import com.panzer.mods.dice_and_delish.registry.recipe.ModRecipeTypes;
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

//? if >=1.21.2 {
/*import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeBookCategories;
*///?}

public record MixRecipe(List<Ingredient> inputs, ItemStack result) implements Recipe<MixRecipeInput> {

    @Override
    public boolean matches(@NotNull MixRecipeInput recipeInput, @NotNull Level level) {
        return matchAssignment(recipeInput) != null;
    }

    private int[] matchAssignment(@NotNull MixRecipeInput recipeInput) {
        int size = recipeInput.size();
        if (size != inputs.size()) {
            return null;
        }
        boolean[] consumed = new boolean[size];
        int[] assignment = new int[inputs.size()];

        for (int i = 0; i < inputs.size(); i++) {
            Ingredient ingredient = inputs.get(i);
            int match = -1;
            for (int j = 0; j < size; j++) {
                if (!consumed[j] && ingredient.test(recipeInput.getItem(j))) {
                    consumed[j] = true;
                    match = j;
                    break;
                }
            }
            if (match < 0) {
                return null;
            }
            assignment[i] = match;
        }
        return assignment;
    }

    public int batchSize(@NotNull MixRecipeInput recipeInput) {
        int[] assignment = matchAssignment(recipeInput);
        if (assignment == null) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        for (int index : assignment) {
            min = Math.min(min, recipeInput.getItem(index).getCount());
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull MixRecipeInput recipeInput, HolderLookup.@NotNull Provider registries) {
        ItemStack output = result.copy();
        int batches = batchSize(recipeInput);
        output.setCount(output.getCount() * Math.max(1, batches));
        return output;
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
        return ModRecipeSerializers.MIX_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.MIX_TYPE.get();
    }
    //?} else {
    /*@Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(inputs);
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<MixRecipeInput>> getSerializer() {
        return ModRecipeSerializers.MIX_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<MixRecipeInput>> getType() {
        return ModRecipeTypes.MIX_TYPE.get();
    }
    *///?}

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
