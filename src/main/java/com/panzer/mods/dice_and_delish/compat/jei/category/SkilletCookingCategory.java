package com.panzer.mods.dice_and_delish.compat.jei.category;

//? if <1.21.2 {
import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.recipe.cook.CookRecipe;
import com.panzer.mods.dice_and_delish.recipe.mix.MixRecipe;
import com.panzer.mods.dice_and_delish.registry.item.ModItems;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SkilletCookingCategory extends AbstractRecipeCategory<RecipeHolder<MixRecipe>> {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final RecipeType<RecipeHolder<MixRecipe>> RECIPE_TYPE =
            (RecipeType) RecipeType.create(DiceAndDelish.MOD_ID, "skillet_cooking", RecipeHolder.class);

    private static final int MAX_DISPLAYED_INPUTS = 4;

    private static final int OUTPUT_X = 61;
    private static final int OUTPUT_Y = 9;
    private static final int ARROW_X = 26;
    private static final int ARROW_Y = 7;
    private static final int FLAME_X = 1;
    private static final int FLAME_Y = 20;

    private static final int[] INPUT_SLOT_X = {1, 1, 18, 18};
    private static final int[] INPUT_SLOT_Y = {1, 18, 1, 18};

    public SkilletCookingCategory(IGuiHelper guiHelper) {
        super(
                RECIPE_TYPE,
                Component.translatable("jei.category.dice_and_delish.skillet_cooking"),
                guiHelper.createDrawableItemStack(new ItemStack(ModItems.SKILLET.get())),
                82,
                44
        );
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<MixRecipe> recipeHolder, @NotNull IFocusGroup focuses) {
        MixRecipe recipe = recipeHolder.value();
        List<Ingredient> inputs = recipe.inputs();

        int shown = Math.min(inputs.size(), MAX_DISPLAYED_INPUTS);
        for (int i = 0; i < shown; i++) {
            builder.addInputSlot(INPUT_SLOT_X[i], INPUT_SLOT_Y[i])
                    .setStandardSlotBackground()
                    .addIngredients(inputs.get(i));
        }

        builder.addOutputSlot(OUTPUT_X, OUTPUT_Y)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull RecipeHolder<MixRecipe> recipeHolder, @NotNull IFocusGroup focuses) {
        int cookTime = CookRecipe.DEFAULT_COOKING_TIME;

        builder.addAnimatedRecipeArrow(cookTime).setPosition(ARROW_X, ARROW_Y);
        builder.addAnimatedRecipeFlame(300).setPosition(FLAME_X, FLAME_Y);

        int cookTimeSeconds = cookTime / 20;
        Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
        builder.addText(timeString, getWidth() - 20, 10)
                .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
                .setTextAlignment(HorizontalAlignment.RIGHT)
                .setColor(0xFF808080);
    }
}
//?}
