package com.nerdsoft.mods.nerdsoftkitchen.registry.recipe;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cook.CookRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.curdle.CurdleRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.mix.MixRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE
            , NerdSoftKitchen.MOD_ID);
    public static final DeferredHolder<RecipeType<?>, RecipeType<CookRecipe>> COOK_TYPE = registerType("cook");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MixRecipe>> MIX_TYPE = registerType("mix");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CurdleRecipe>> CURDLE_TYPE = registerType("curdle");

    private ModRecipeTypes() {
    }

    private static <T extends net.minecraft.world.item.crafting.Recipe<?>> DeferredHolder<RecipeType<?>,
            RecipeType<T>> registerType(String name) {
        return RECIPE_TYPES.register(name, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return NerdSoftKitchen.MOD_ID + ":" + name;
            }
        });
    }
}
