package com.nerdsoft.mods.nerdsoftkitchen.registry;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.CookRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.CurdleRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.MixRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.ShapelessCupCraftingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, NerdSoftKitchen.MOD_ID);
    public static final DeferredHolder<RecipeSerializer<?>, CookRecipe.Serializer> COOK_SERIALIZER =
            RECIPE_SERIALIZERS.register("cook", CookRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, MixRecipe.Serializer> MIX_SERIALIZER =
            RECIPE_SERIALIZERS.register("mix", MixRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, CurdleRecipe.Serializer> CURDLE_SERIALIZER =
            RECIPE_SERIALIZERS.register("curdle", CurdleRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, ShapelessCupCraftingRecipe.Serializer> SHAPELESS_CUP_SERIALIZER =
            RECIPE_SERIALIZERS.register("shapeless_cup_crafting", ShapelessCupCraftingRecipe.Serializer::new);

    private ModRecipeSerializers() {
    }
}