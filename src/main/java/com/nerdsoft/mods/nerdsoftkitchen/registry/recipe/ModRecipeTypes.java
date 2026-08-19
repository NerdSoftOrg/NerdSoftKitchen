package com.nerdsoft.mods.nerdsoftkitchen.registry.recipe;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cook.CookRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cutting.CuttingRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.mix.MixRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipeTypes {

    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE,
            NerdSoftKitchen.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<CookRecipe>> COOK_TYPE = registerType("cook");
    public static final DeferredHolder<RecipeType<?>, RecipeType<MixRecipe>> MIX_TYPE = registerType("mix");
    public static final DeferredHolder<RecipeType<?>, RecipeType<CuttingRecipe>> CUT_TYPE = registerType("cutting");

    private ModRecipeTypes() {
    }

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        NerdSoftKitchenLogger.info("Recipe Types registered successfully.");
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
