package com.nerdsoft.mods.nerdsoftkitchen.registry.recipe;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cook.CookRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cup.ShapelessCupCraftingRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.curdle.CurdleRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cutting.CuttingRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.mix.MixRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeSerializers {

    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, NerdSoftKitchen.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, CookRecipe.Serializer> COOK_SERIALIZER =
            RECIPE_SERIALIZERS.register("cook", CookRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, MixRecipe.Serializer> MIX_SERIALIZER =
            RECIPE_SERIALIZERS.register("mix", MixRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, CurdleRecipe.Serializer> CURDLE_SERIALIZER =
            RECIPE_SERIALIZERS.register("curdle", CurdleRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, ShapelessCupCraftingRecipe.Serializer> SHAPELESS_CUP_SERIALIZER =
            RECIPE_SERIALIZERS.register("shapeless_cup_crafting", ShapelessCupCraftingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, CuttingRecipe.Serializer> CUT_SERIALIZER =
            RECIPE_SERIALIZERS.register("cutting", CuttingRecipe.Serializer::new);

    private ModRecipeSerializers() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
        NerdSoftKitchenLogger.info("Recipe Serializers registered successfully.");
    }
}