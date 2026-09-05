package com.panzer.mods.dice_and_delish.registry.recipe;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.recipe.cup.CupContentIngredient;
import com.panzer.mods.dice_and_delish.util.ModLogger;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ModIngredientTypes {

    private static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, DiceAndDelish.MOD_ID);

    @SuppressWarnings("unused")
    public static final DeferredHolder<IngredientType<?>, IngredientType<CupContentIngredient>> CUP_CONTENT =
            INGREDIENT_TYPES.register("cup_content", () -> CupContentIngredient.TYPE);

    private ModIngredientTypes() {
    }

    public static void register(IEventBus eventBus) {
        INGREDIENT_TYPES.register(eventBus);
        ModLogger.debug("Ingredient Types registered successfully.");
    }
}
