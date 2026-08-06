package com.nerdsoft.mods.nerdsoftkitchen.datagen.recipe;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cup.CupContentIngredient;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cup.ShapelessCupCraftingRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cutting.CuttingRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.core.HolderLookup;
//? if >=1.21.2 {
/*import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
*///?}
//? if <=1.21.1 {
import net.minecraft.data.DataProvider;
//?}
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
@SuppressWarnings("Commented out code")
//? if <1.21.2 {
public class ModRecipeProvider extends RecipeProvider implements DataProvider {

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
                .define('C', Items.CAMPFIRE)
                .unlockedBy(getHasName(Items.CAMPFIRE), has(Items.CAMPFIRE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GRILL_TABLE_SOUL.get())
                .pattern("I").pattern("S").pattern("C")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STONE_BRICKS)
                .define('C', Items.SOUL_CAMPFIRE)
                .unlockedBy(getHasName(Items.SOUL_CAMPFIRE), has(Items.SOUL_CAMPFIRE))
                .save(output, id("grill_table_soul"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CUTTING_BOARD.get())
                .pattern("PPP")
                .define('P', Items.OAK_PLANKS)
                .unlockedBy(getHasName(Items.OAK_PLANKS), has(Items.OAK_PLANKS))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.DIAMOND_KNIFE.get())
                .pattern(" D").pattern("S ")
                .define('D', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CHEESE.get())
                .requires(Items.MILK_BUCKET)
                .requires(Items.MILK_BUCKET)
                .requires(Items.MILK_BUCKET)
                .unlockedBy(getHasName(Items.MILK_BUCKET), has(Items.MILK_BUCKET))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CHEESE_SANDWICH.get())
                .requires(Items.BREAD)
                .requires(Items.BREAD)
                .requires(ModItems.CHEESE_SLICE.get())
                .unlockedBy(getHasName(ModItems.CHEESE_SLICE.get()), has(ModItems.CHEESE_SLICE.get()))
                .save(output);

        grillCookingRecipe(output, ModItems.RAW_CHICKEN_PIECES.get(), ModItems.COOKED_CHICKEN_PIECES.get(),
                "cooked_chicken_pieces_grill", GRILL_COOK_TIME);
        grillCookingRecipe(output, Items.EGG, ModItems.FRIED_EGG.get(), "fried_egg_grill", GRILL_COOK_TIME / 2);
        grillCookingRecipe(output, ModItems.CHEESE_SANDWICH.get(), ModItems.GRILLED_CHEESE.get(),
                "grilled_cheese_grill", GRILL_COOK_TIME / 2);

        cuttingRecipe(output, Ingredient.of(ModItems.CHEESE.get()), new ItemStack(ModItems.CHEESE_SLICE.get(), 4),
                "cheese_slice_cutting");
        cuttingRecipe(output, Ingredient.of(Items.CHICKEN), new ItemStack(ModItems.RAW_CHICKEN_PIECES.get(), 2),
                "chicken_pieces_cutting");

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
        cupYogurtRecipe(
                output,
                List.of(CupContentIngredient.of(IronCupContent.YOGURT), Ingredient.of(ModItems.STRAWBERRY.get())),
                IronCupContent.STRAWBERRY_YOGURT,
                "iron_cup_strawberry_yogurt_from_yogurt"
        );
    }

    private void grillCookingRecipe(RecipeOutput output, Item input, Item result, String name, int cookTime) {
        CookRecipeBuilder.cooking(Ingredient.of(input), new ItemStack(result), cookTime)
                .unlockedBy(getHasName(input), has(input))
                .save(output, id(name));
    }

    private void cuttingRecipe(RecipeOutput output, Ingredient input, ItemStack result, String name) {
        output.accept(id(name), new CuttingRecipe(input, result), null);
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
//?} else {
/*public class ModRecipeProvider extends RecipeProvider {

    private static final int GRILL_COOK_TIME = 600;

    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes() {
        shaped(RecipeCategory.MISC, ModItems.IRON_CUP.get())
                .pattern("N N").pattern(" N ")
                .define('N', Items.IRON_NUGGET)
                .unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET))
                .save(this.output);

        shaped(RecipeCategory.MISC, ModItems.GRILL_TABLE.get())
                .pattern("I").pattern("S").pattern("C")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STONE_BRICKS)
                .define('C', Items.CAMPFIRE)
                .unlockedBy(getHasName(Items.CAMPFIRE), has(Items.CAMPFIRE))
                .save(this.output);

        shaped(RecipeCategory.MISC, ModItems.GRILL_TABLE_SOUL.get())
                .pattern("I").pattern("S").pattern("C")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STONE_BRICKS)
                .define('C', Items.SOUL_CAMPFIRE)
                .unlockedBy(getHasName(Items.SOUL_CAMPFIRE), has(Items.SOUL_CAMPFIRE))
                .save(this.output, recipeKey("grill_table_soul"));

        shaped(RecipeCategory.MISC, ModItems.CUTTING_BOARD.get())
                .pattern("PPP")
                .define('P', Items.OAK_PLANKS)
                .unlockedBy(getHasName(Items.OAK_PLANKS), has(Items.OAK_PLANKS))
                .save(this.output);

        shaped(RecipeCategory.TOOLS, ModItems.DIAMOND_KNIFE.get())
                .pattern(" D").pattern("S ")
                .define('D', Items.DIAMOND)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(this.output);

        shapeless(RecipeCategory.FOOD, ModItems.SALAD.get())
                .requires(ModItems.TOMATO.get())
                .requires(ModItems.LETTUCE.get())
                .requires(Items.BOWL)
                .unlockedBy(getHasName(ModItems.LETTUCE.get()), has(ModItems.LETTUCE.get()))
                .save(this.output);

        shapeless(RecipeCategory.FOOD, ModItems.RAW_CHICKEN_PIECES.get(), 2)
                .requires(Items.CHICKEN)
                .unlockedBy(getHasName(Items.CRAFTING_TABLE), has(Items.CRAFTING_TABLE))
                .save(this.output);

        shapeless(RecipeCategory.FOOD, ModItems.CHEESE.get())
                .requires(Items.MILK_BUCKET)
                .requires(Items.MILK_BUCKET)
                .requires(Items.MILK_BUCKET)
                .unlockedBy(getHasName(Items.MILK_BUCKET), has(Items.MILK_BUCKET))
                .save(this.output);

        shapeless(RecipeCategory.FOOD, ModItems.CHEESE_SANDWICH.get())
                .requires(Items.BREAD)
                .requires(Items.BREAD)
                .requires(ModItems.CHEESE_SLICE.get())
                .unlockedBy(getHasName(ModItems.CHEESE_SLICE.get()), has(ModItems.CHEESE_SLICE.get()))
                .save(this.output);

        grillCookingRecipe(this.output, ModItems.RAW_CHICKEN_PIECES.get(), ModItems.COOKED_CHICKEN_PIECES.get(),
                "cooked_chicken_pieces_grill", GRILL_COOK_TIME);
        grillCookingRecipe(this.output, Items.EGG, ModItems.FRIED_EGG.get(), "fried_egg_grill", GRILL_COOK_TIME / 2);
        grillCookingRecipe(this.output, ModItems.CHEESE_SANDWICH.get(), ModItems.GRILLED_CHEESE.get(),
                "grilled_cheese_grill", GRILL_COOK_TIME / 2);

        cuttingRecipe(this.output, Ingredient.of(ModItems.CHEESE.get()), new ItemStack(ModItems.CHEESE_SLICE.get(), 4),
                "cheese_slice_cutting");
        cuttingRecipe(this.output, Ingredient.of(Items.CHICKEN), new ItemStack(ModItems.RAW_CHICKEN_PIECES.get(), 2),
                "chicken_pieces_cutting");

        cupYogurtRecipe(
                this.output,
                List.of(CupContentIngredient.of(IronCupContent.MILK), Ingredient.of(Items.SUGAR)),
                IronCupContent.YOGURT,
                "iron_cup_yogurt_from_milk"
        );
        cupYogurtRecipe(
                this.output,
                List.of(CupContentIngredient.of(IronCupContent.MILK), Ingredient.of(Items.SUGAR), Ingredient.of(ModItems.STRAWBERRY.get())),
                IronCupContent.STRAWBERRY_YOGURT,
                "iron_cup_strawberry_yogurt"
        );
    }

    private void grillCookingRecipe(RecipeOutput output, Item input, Item result, String name, int cookTime) {
        CookRecipeBuilder.cooking(Ingredient.of(input), new ItemStack(result), cookTime)
                .unlockedBy(getHasName(input), has(input))
                .save(output, recipeKey(name));
    }

    private void cuttingRecipe(RecipeOutput output, Ingredient input, ItemStack result, String name) {
        output.accept(recipeKey(name), new CuttingRecipe(input, result), null);
    }

    private void cupYogurtRecipe(RecipeOutput output, List<Ingredient> ingredients, IronCupContent resultContent, String name) {
        ItemStack result = IronCupItem.filled(ModItems.IRON_CUP.get(), resultContent);
        ShapelessCupCraftingRecipe recipe = new ShapelessCupCraftingRecipe(ingredients, result);
        output.accept(recipeKey(name), recipe, null);
    }

    private ResourceKey<Recipe<?>> recipeKey(String name) {
        return ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, name));
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider registries, @NotNull RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public @NotNull String getName() {
            return "NerdSoftKitchen Recipes";
        }
    }
}
*///?}