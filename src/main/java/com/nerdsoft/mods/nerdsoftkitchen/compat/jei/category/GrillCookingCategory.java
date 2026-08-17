package com.nerdsoft.mods.nerdsoftkitchen.compat.jei.category;

//? if <1.21.2 {
import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cook.CookRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

public final class GrillCookingCategory extends AbstractRecipeCategory<RecipeHolder<CookRecipe>> {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final RecipeType<RecipeHolder<CookRecipe>> RECIPE_TYPE =
            (RecipeType) RecipeType.create(NerdSoftKitchen.MOD_ID, "grill_cooking", RecipeHolder.class);

    private static final int REGULAR_COOK_TIME = CookRecipe.DEFAULT_COOKING_TIME;

    public GrillCookingCategory(IGuiHelper guiHelper) {
        super(
                RECIPE_TYPE,
                Component.translatable("jei.category.nerdsoftkitchen.grill_cooking"),
                guiHelper.createDrawableItemStack(new ItemStack(ModItems.GRILL_TABLE.get())),
                82,
                44
        );
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<CookRecipe> recipeHolder, @NotNull IFocusGroup focuses) {
        CookRecipe recipe = recipeHolder.value();

        builder.addInputSlot(1, 1)
                .setStandardSlotBackground()
                .addIngredients(recipe.input());

        builder.addOutputSlot(61, 9)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull RecipeHolder<CookRecipe> recipeHolder, @NotNull IFocusGroup focuses) {
        CookRecipe recipe = recipeHolder.value();
        int cookTime = recipe.cookingTime() > 0 ? recipe.cookingTime() : REGULAR_COOK_TIME;

        builder.addAnimatedRecipeArrow(cookTime).setPosition(26, 7);
        builder.addAnimatedRecipeFlame(300).setPosition(1, 20);

        int cookTimeSeconds = cookTime / 20;
        Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
        builder.addText(timeString, getWidth() - 20, 10)
                .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
                .setTextAlignment(HorizontalAlignment.RIGHT)
                .setColor(0xFF808080);
    }
}
//?}
