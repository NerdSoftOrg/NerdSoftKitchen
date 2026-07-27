package com.nerdsoft.mods.nerdsoftkitchen.registry.recipe;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cup.CupContentIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModIngredientTypes {

    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, NerdSoftKitchen.MOD_ID);

    @SuppressWarnings("unused")
    public static final DeferredHolder<IngredientType<?>, IngredientType<CupContentIngredient>> CUP_CONTENT =
            INGREDIENT_TYPES.register("cup_content", () -> CupContentIngredient.TYPE);

    private ModIngredientTypes() {
    }
}