package com.panzer.mods.dice_and_delish.registry.recipe;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.recipe.cook.CookRecipe;
import com.panzer.mods.dice_and_delish.recipe.cup.ShapelessCupCraftingRecipe;
import com.panzer.mods.dice_and_delish.recipe.cutting.CuttingRecipe;
import com.panzer.mods.dice_and_delish.recipe.mix.MixRecipe;
import com.panzer.mods.dice_and_delish.util.ModLogger;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeSerializers {

    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, DiceAndDelish.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, CookRecipe.Serializer> COOK_SERIALIZER =
            RECIPE_SERIALIZERS.register("cook", CookRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, MixRecipe.Serializer> MIX_SERIALIZER =
            RECIPE_SERIALIZERS.register("mix", MixRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, ShapelessCupCraftingRecipe.Serializer> SHAPELESS_CUP_SERIALIZER =
            RECIPE_SERIALIZERS.register("shapeless_cup_crafting", ShapelessCupCraftingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, CuttingRecipe.Serializer> CUT_SERIALIZER =
            RECIPE_SERIALIZERS.register("cutting", CuttingRecipe.Serializer::new);

    private ModRecipeSerializers() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
        ModLogger.debug("Recipe Serializers registered successfully.");
    }
}
