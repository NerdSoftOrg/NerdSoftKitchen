package com.nerdsoft.mods.nerdsoftkitchen.compat.jei.category;

//? if <1.21.2 {
import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cook.CookRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.mix.MixRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class EggMixCategory extends AbstractRecipeCategory<RecipeHolder<MixRecipe>> {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final RecipeType<RecipeHolder<MixRecipe>> RECIPE_TYPE =
            (RecipeType) RecipeType.create(NerdSoftKitchen.MOD_ID, "egg_mix_cooking", RecipeHolder.class);

    private static final int WIDTH = 82;
    private static final int HEIGHT = 44;

    private static final int OUTPUT_X = 61;
    private static final int OUTPUT_Y = 9;
    private static final int EGG_INPUT_X = 1;
    private static final int EGG_INPUT_Y = 1;
    private static final int OTHER_INPUT_X = 1;
    private static final int OTHER_INPUT_Y = 18;

    private static final int LIQUID_PREVIEW_X = 30;
    private static final int LIQUID_PREVIEW_Y = 16;
    private static final float LIQUID_PREVIEW_SIZE = 10.0F;
    private static final int EGG_LIQUID_RGB = 0xF2D34A;
    private static final long PULSE_PERIOD_MILLIS = 1400L;
    private static final float PULSE_MIN_ALPHA = 0.45F;
    private static final float PULSE_MAX_ALPHA = 0.95F;

    public EggMixCategory(IGuiHelper guiHelper) {
        super(
                RECIPE_TYPE,
                Component.translatable("jei.category.nerdsoftkitchen.egg_mix_cooking"),
                guiHelper.createDrawableItemStack(new ItemStack(Items.EGG)),
                WIDTH,
                HEIGHT
        );
    }

    public static boolean isEggMix(MixRecipe recipe) {
        for (Ingredient ingredient : recipe.inputs()) {
            if (ingredient.test(new ItemStack(Items.EGG))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<MixRecipe> recipeHolder, @NotNull IFocusGroup focuses) {
        MixRecipe recipe = recipeHolder.value();
        List<Ingredient> inputs = recipe.inputs();

        builder.addInputSlot(EGG_INPUT_X, EGG_INPUT_Y)
                .setStandardSlotBackground()
                .addIngredient(mezz.jei.api.constants.VanillaTypes.ITEM_STACK, new ItemStack(Items.EGG));

        for (Ingredient ingredient : inputs) {
            if (!ingredient.test(new ItemStack(Items.EGG))) {
                builder.addInputSlot(OTHER_INPUT_X, OTHER_INPUT_Y)
                        .setStandardSlotBackground()
                        .addIngredients(ingredient);
                break;
            }
        }

        builder.addOutputSlot(OUTPUT_X, OUTPUT_Y)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());
    }

    @Override
    public void createRecipeExtras(@NotNull IRecipeExtrasBuilder builder, @NotNull RecipeHolder<MixRecipe> recipeHolder, @NotNull IFocusGroup focuses) {
        int cookTimeSeconds = CookRecipe.DEFAULT_COOKING_TIME / 20;
        Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
        builder.addText(timeString, getWidth() - 20, 10)
                .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
                .setTextAlignment(HorizontalAlignment.RIGHT)
                .setColor(0xFF808080);
    }

    @Override
    public void draw(@NotNull RecipeHolder<MixRecipe> recipeHolder, @NotNull IRecipeSlotsView recipeSlotsView,
                     @NotNull net.minecraft.client.gui.GuiGraphics guiGraphics, double mouseX, double mouseY) {
        drawEggLiquidPreview(guiGraphics);
    }

    private void drawEggLiquidPreview(net.minecraft.client.gui.GuiGraphics guiGraphics) {
        float phase = (System.currentTimeMillis() % PULSE_PERIOD_MILLIS) / (float) PULSE_PERIOD_MILLIS;
        float wave = (float) (0.5 - 0.5 * Math.cos(phase * 2.0 * Math.PI)); // 0 -> 1 -> 0, smooth
        float alpha = PULSE_MIN_ALPHA + (PULSE_MAX_ALPHA - PULSE_MIN_ALPHA) * wave;
        int argb = ((int) (alpha * 255.0F) << 24) | EGG_LIQUID_RGB;

        guiGraphics.fill(
                LIQUID_PREVIEW_X, LIQUID_PREVIEW_Y,
                (int) (LIQUID_PREVIEW_X + LIQUID_PREVIEW_SIZE), (int) (LIQUID_PREVIEW_Y + LIQUID_PREVIEW_SIZE),
                argb
        );
    }

    @Override
    public void getTooltip(@NotNull ITooltipBuilder tooltip,
                           @NotNull RecipeHolder<MixRecipe> recipeHolder,
                           @NotNull IRecipeSlotsView recipeSlotsView,
                           double mouseX, double mouseY) {
        if (mouseX >= LIQUID_PREVIEW_X && mouseX <= LIQUID_PREVIEW_X + LIQUID_PREVIEW_SIZE
                && mouseY >= LIQUID_PREVIEW_Y && mouseY <= LIQUID_PREVIEW_Y + LIQUID_PREVIEW_SIZE) {
            tooltip.add(Component.translatable("jei.category.nerdsoftkitchen.egg_mix_cooking.tooltip"));
        }
    }
}
//?}
