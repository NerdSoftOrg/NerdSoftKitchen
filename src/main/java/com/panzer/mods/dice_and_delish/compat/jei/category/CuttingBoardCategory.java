package com.panzer.mods.dice_and_delish.compat.jei.category;

//? if <1.21.2 {
import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.recipe.cutting.CuttingRecipe;
import com.panzer.mods.dice_and_delish.registry.item.ModItems;
import com.panzer.mods.dice_and_delish.registry.tags.ModItemTags;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class CuttingBoardCategory extends AbstractRecipeCategory<RecipeHolder<CuttingRecipe>> {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final RecipeType<RecipeHolder<CuttingRecipe>> RECIPE_TYPE =
            (RecipeType) RecipeType.create(DiceAndDelish.MOD_ID, "cutting_board", RecipeHolder.class);

    private static final int WIDTH = 82;
    private static final int HEIGHT = 44;

    private static final int INPUT_SLOT_X = 32;
    private static final int INPUT_SLOT_Y = 26;
    private static final int OUTPUT_SLOT_X = 61;
    private static final int OUTPUT_SLOT_Y = 9;
    private static final int KNIFE_SLOT_X = 3;
    private static final int KNIFE_SLOT_Y = 8;

    private List<ItemStack> knives;

    public CuttingBoardCategory(IGuiHelper guiHelper) {
        super(
                RECIPE_TYPE,
                Component.translatable("jei.category.dice_and_delish.cutting_board"),
                guiHelper.createDrawableItemStack(new ItemStack(ModItems.CUTTING_BOARD.get())),
                WIDTH,
                HEIGHT
        );
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeHolder<CuttingRecipe> recipeHolder, @NotNull IFocusGroup focuses) {
        CuttingRecipe recipe = recipeHolder.value();

        builder.addInputSlot(INPUT_SLOT_X, INPUT_SLOT_Y)
                .setStandardSlotBackground()
                .addIngredients(recipe.input());

        builder.addOutputSlot(OUTPUT_SLOT_X, OUTPUT_SLOT_Y)
                .setOutputSlotBackground()
                .addItemStack(recipe.result());

        builder.addSlot(mezz.jei.api.recipe.RecipeIngredientRole.CATALYST, KNIFE_SLOT_X, KNIFE_SLOT_Y)
                .setStandardSlotBackground()
                .addIngredients(VanillaTypes.ITEM_STACK, allKnives());
    }

    private List<ItemStack> allKnives() {
        if (knives == null) {
            List<ItemStack> found = new ArrayList<>();
            for (Item item : BuiltInRegistries.ITEM) {
                if (BuiltInRegistries.ITEM.wrapAsHolder(item).is(ModItemTags.KNIFE)) {
                    found.add(new ItemStack(item));
                }
            }
            knives = found;
        }
        return knives;
    }
}
//?}
