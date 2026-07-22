package com.nerdsoft.mods.nerdsoftkitchen.datagen;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.CupContentIngredient;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.ShapelessCupCraftingRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    private static final int GRILL_COOK_TIME = 600;

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.IRON_CUP.get())
                .pattern("N N").pattern(" N ")
                .define('N', Items.IRON_NUGGET)
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GRILL_TABLE.get())
                .pattern("I").pattern("S").pattern("C")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STONE_BRICKS)
                .define('C', Ingredient.of(Items.CAMPFIRE, Items.SOUL_CAMPFIRE))
                .unlockedBy(getHasName(Items.CAMPFIRE), has(Items.CAMPFIRE))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.GRILL_TABLE_SOUL.get())
                .requires(ModItems.GRILL_TABLE.get())
                .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                .unlockedBy(getHasName(ModItems.GRILL_TABLE.get()), has(ModItems.GRILL_TABLE.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.SALAD.get())
                .requires(ModItems.TOMATO.get())
                .requires(ModItems.LETTUCE.get())
                .requires(Items.BOWL)
                .unlockedBy(getHasName(ModItems.LETTUCE.get()), has(ModItems.LETTUCE.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RAW_CHICKEN_PIECES.get(), 2)
                .requires(Items.CHICKEN)
                .unlockedBy(getHasName(Items.CRAFTING_TABLE), has(Items.CRAFTING_TABLE))
                .save(output);

        grillCookingRecipe(output, ModItems.RAW_CHICKEN_PIECES.get(), ModItems.COOKED_CHICKEN_PIECES.get(),
                "cooked_chicken_pieces_grill", GRILL_COOK_TIME);
        grillCookingRecipe(output, Items.EGG, ModItems.FRIED_EGG.get(), "fried_egg_grill", GRILL_COOK_TIME / 2);

        cupYogurtRecipe(
                output,
                List.of(CupContentIngredient.of(IronCupContent.MILK), Ingredient.of(Items.SUGAR)),
                IronCupContent.YOGURT,
                "iron_cup_yogurt_from_milk"
        );
        cupYogurtRecipe(
                output,
                List.of(CupContentIngredient.of(IronCupContent.MILK), Ingredient.of(Items.SUGAR), Ingredient.of(ModItems.STRAWBERRY.get())),
                IronCupContent.STRAWBERRY_YOGURT,
                "iron_cup_strawberry_yogurt"
        );
    }

    private void grillCookingRecipe(RecipeOutput output, Item input, Item result, String name, int cookTime) {
        CookRecipeBuilder.cooking(Ingredient.of(input), new ItemStack(result), cookTime)
                .unlockedBy(getHasName(input), has(input))
                .save(output, id(name));
    }

    private void cupYogurtRecipe(RecipeOutput output, List<Ingredient> ingredients, IronCupContent resultContent, String name) {
        ItemStack result = IronCupItem.filled(ModItems.IRON_CUP.get(), resultContent);
        ShapelessCupCraftingRecipe recipe = new ShapelessCupCraftingRecipe(ingredients, result);
        output.accept(id(name), recipe, null);
    }

    private ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, name);
    }
}