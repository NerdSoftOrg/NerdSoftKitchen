package com.nerdsoft.mods.nerdsoftkitchen.compat.jei.category;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cook.CookRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GrillCookingCategory implements IRecipeCategory<CookRecipe> {
    public static final RecipeType<CookRecipe> RECIPE_TYPE =
            RecipeType.create(NerdSoftKitchen.MOD_ID, "grill_cooking", CookRecipe.class);

    private static final ResourceLocation BURN_PROGRESS_SPRITE =
            ResourceLocation.withDefaultNamespace("container/furnace/burn_progress");

    private final IDrawable background;
    private final IDrawable icon;
    private final IGuiHelper guiHelper;
    private final Map<Integer, ITickTimer> tickTimersByCookTime = new ConcurrentHashMap<>();

    public GrillCookingCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.background = guiHelper.createBlankDrawable(82, 34);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModItems.GRILL_TABLE.get()));
    }

    @Override
    public @NotNull RecipeType<CookRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.category.nerdsoftkitchen.grill_cooking");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull CookRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                .addIngredients(recipe.input());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9)
                .addItemStack(recipe.result());
    }

    @Override
    public void draw(@NotNull CookRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int cookTime = recipe.cookingTime() > 0 ? recipe.cookingTime() : 200;
        ITickTimer timer = tickTimersByCookTime.computeIfAbsent(cookTime, time -> guiHelper.createTickTimer(time, 24, true));

        guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, 24, 9, 24, 17);

        int progress = timer.getValue();
        if (progress > 0) {
            guiGraphics.blitSprite(BURN_PROGRESS_SPRITE, 24, 17, 0, 0, 24, 9, progress, 17);
        }

        int cookTimeSeconds = cookTime / 20;
        Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
        Font font = Minecraft.getInstance().font;
        int stringWidth = font.width(timeString);
        guiGraphics.drawString(font, timeString, background.getWidth() - stringWidth, 0, 0xFF888888, false);
    }
}