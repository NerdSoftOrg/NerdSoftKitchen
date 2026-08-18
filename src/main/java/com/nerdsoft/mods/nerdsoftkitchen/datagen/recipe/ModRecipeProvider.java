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
import net.minecraft.world.item.crafting.Recipe;
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
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//? if <1.21.2 {
public class ModRecipeProvider extends RecipeProvider implements DataProvider {

    private static final int DEFAULT_GRILL_COOK_TIME = 600;
    private static final int FAST_GRILL_COOK_TIME = 300;

    private static final Map<Item, CookingEntry> GRILL_COOKING_RECIPES = Map.of(
            // (Raw -> Toasted)
            ModItems.RAW_SANDWICH_BREAD.get(), new CookingEntry(ModItems.TOASTED_SANDWICH_BREAD.get(), FAST_GRILL_COOK_TIME),
            ModItems.CHEESE_RAW_SANDWICH.get(), new CookingEntry(ModItems.CHEESE_TOASTED_SANDWICH.get(), FAST_GRILL_COOK_TIME),

            ModItems.RAW_CHICKEN_PIECES.get(), new CookingEntry(ModItems.COOKED_CHICKEN_PIECES.get(), DEFAULT_GRILL_COOK_TIME),
            Items.EGG, new CookingEntry(ModItems.FRIED_EGG.get(), FAST_GRILL_COOK_TIME),
            ModItems.CHEESE_SLICE.get(), new CookingEntry(ModItems.GRILLED_CHEESE.get(), FAST_GRILL_COOK_TIME),
            ModItems.RICE.get(), new CookingEntry(ModItems.COOKED_RICE.get(), DEFAULT_GRILL_COOK_TIME)
    );

    private record CookingEntry(Item result, int cookTime) {}

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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.RICE_BOWL.get())
                .requires(ModItems.COOKED_RICE.get())
                .requires(Items.BOWL)
                .unlockedBy(getHasName(ModItems.COOKED_RICE.get()), has(ModItems.COOKED_RICE.get()))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CHEESE.get())
                .requires(Items.MILK_BUCKET)
                .requires(Items.MILK_BUCKET)
                .requires(Items.MILK_BUCKET)
                .unlockedBy(getHasName(Items.MILK_BUCKET), has(Items.MILK_BUCKET))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.CHEESE_RAW_SANDWICH.get())
                .requires(ModItems.RAW_SANDWICH_BREAD.get())
                .requires(ModItems.RAW_SANDWICH_BREAD.get())
                .requires(ModItems.CHEESE_SLICE.get())
                .unlockedBy(getHasName(ModItems.CHEESE_SLICE.get()), has(ModItems.CHEESE_SLICE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SKILLET.get())
                .pattern("III").pattern("I I").pattern(" S ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);

        GRILL_COOKING_RECIPES.forEach((input, entry) ->
                grillCookingRecipe(output, input, entry.result(), entry.cookTime())
        );

        cuttingRecipe(output, Ingredient.of(ModItems.CHEESE.get()), new ItemStack(ModItems.CHEESE_SLICE.get(), 4));
        cuttingRecipe(output, Ingredient.of(Items.CHICKEN), new ItemStack(ModItems.RAW_CHICKEN_PIECES.get(), 2));

        panMixRecipe(output, "scrambled_eggs",
                new ItemStack(ModItems.SCRAMBLED_EGGS.get()),
                Ingredient.of(Items.EGG), Ingredient.of(ModItems.CHEESE_SLICE.get()));

        cupYogurtRecipe(output, IronCupContent.MILK,
                List.of(CupContentIngredient.of(IronCupContent.MILK), Ingredient.of(Items.SUGAR)),
                IronCupContent.YOGURT);

        cupYogurtRecipe(output, IronCupContent.MILK,
                List.of(CupContentIngredient.of(IronCupContent.MILK), Ingredient.of(Items.SUGAR), Ingredient.of(ModItems.STRAWBERRY.get())),
                IronCupContent.STRAWBERRY_YOGURT);

        cupYogurtRecipe(output, IronCupContent.YOGURT,
                List.of(CupContentIngredient.of(IronCupContent.YOGURT), Ingredient.of(ModItems.STRAWBERRY.get())),
                IronCupContent.STRAWBERRY_YOGURT);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ORGANIC_MIXTURE.get())
                .requires(Items.BONE_MEAL, 4)
                .requires(Items.ROTTEN_FLESH, 2)
                .requires(Items.WHEAT, 2)
                .unlockedBy(getHasName(Items.ROTTEN_FLESH), has(Items.ROTTEN_FLESH))
                .save(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ORGANIC_SOIL.get())
                .requires(ModItems.ORGANIC_MIXTURE.get())
                .requires(Items.DIRT)
                .unlockedBy(getHasName(ModItems.ORGANIC_MIXTURE.get()), has(ModItems.ORGANIC_MIXTURE.get()))
                .save(output);
    }

    private void grillCookingRecipe(RecipeOutput output, Item input, Item result, int cookTime) {
        CookRecipeBuilder.cooking(Ingredient.of(input), new ItemStack(result), cookTime)
                .unlockedBy(getHasName(input), has(input))
                .save(output, id(recipeNameFor(result) + "_grill"));
    }

    private void cuttingRecipe(RecipeOutput output, Ingredient input, ItemStack result) {
        output.accept(id(recipeNameFor(result.getItem()) + "_cutting"), new CuttingRecipe(input, result), null);
    }

    @SuppressWarnings("SameParameterValue")
    private void panMixRecipe(RecipeOutput output, String name, ItemStack result, Ingredient... inputs) {
        com.nerdsoft.mods.nerdsoftkitchen.datagen.recipe.MixRecipeBuilder.mixing(result, inputs)
                .unlockedBy(getHasName(inputs[0].getItems()[0].getItem()), has(inputs[0].getItems()[0].getItem()))
                .save(output, id(name + "_skillet"));
    }

    private void cupYogurtRecipe(RecipeOutput output, IronCupContent sourceContent, List<Ingredient> ingredients, IronCupContent resultContent) {
        ItemStack result = IronCupItem.filled(ModItems.IRON_CUP.get(), resultContent);
        ShapelessCupCraftingRecipe recipe = new ShapelessCupCraftingRecipe(ingredients, result);
        String name = "iron_cup_" + resultContent.getSerializedName() + "_from_" + sourceContent.getSerializedName();
        output.accept(id(name), recipe, null);
    }

    private ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, name);
    }
//?} else {
/*public class ModRecipeProvider extends RecipeProvider {

    private static final int DEFAULT_GRILL_COOK_TIME = 600; // 30 segundos
    private static final int FAST_GRILL_COOK_TIME = 300;    // 15 segundos

    private static final Map<Item, CookingEntry> GRILL_COOKING_RECIPES = Map.of(
            // Sándwiches (Raw -> Toasted)
            ModItems.RAW_SANDWICH_BREAD.get(), new CookingEntry(ModItems.TOASTED_SANDWICH_BREAD.get(), FAST_GRILL_COOK_TIME),
            ModItems.CHEESE_RAW_SANDWICH.get(), new CookingEntry(ModItems.CHEESE_TOASTED_SANDWICH.get(), FAST_GRILL_COOK_TIME),

            // Ingredientes / Comidas
            ModItems.RAW_CHICKEN_PIECES.get(), new CookingEntry(ModItems.COOKED_CHICKEN_PIECES.get(), DEFAULT_GRILL_COOK_TIME),
            Items.EGG, new CookingEntry(ModItems.FRIED_EGG.get(), FAST_GRILL_COOK_TIME),
            ModItems.CHEESE_SLICE.get(), new CookingEntry(ModItems.GRILLED_CHEESE.get(), FAST_GRILL_COOK_TIME),
            ModItems.RICE.get(), new CookingEntry(ModItems.COOKED_RICE.get(), DEFAULT_GRILL_COOK_TIME)
    );

    private record CookingEntry(Item result, int cookTime) {}

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

        shapeless(RecipeCategory.FOOD, ModItems.RICE_BOWL.get())
                .requires(ModItems.COOKED_RICE.get())
                .requires(Items.BOWL)
                .unlockedBy(getHasName(ModItems.COOKED_RICE.get()), has(ModItems.COOKED_RICE.get()))
                .save(this.output);

        shapeless(RecipeCategory.FOOD, ModItems.CHEESE.get())
                .requires(Items.MILK_BUCKET)
                .requires(Items.MILK_BUCKET)
                .requires(Items.MILK_BUCKET)
                .unlockedBy(getHasName(Items.MILK_BUCKET), has(Items.MILK_BUCKET))
                .save(this.output);

        shapeless(RecipeCategory.FOOD, ModItems.CHEESE_RAW_SANDWICH.get())
                .requires(ModItems.RAW_SANDWICH_BREAD.get())
                .requires(ModItems.RAW_SANDWICH_BREAD.get())
                .requires(ModItems.CHEESE_SLICE.get())
                .unlockedBy(getHasName(ModItems.RAW_SANDWICH_BREAD.get()), has(ModItems.RAW_SANDWICH_BREAD.get()))
                .save(this.output);

        shaped(RecipeCategory.TOOLS, ModItems.SKILLET.get())
                .pattern("III").pattern("I I").pattern(" S ")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(this.output);

        // --- Procesamiento Automático de Recetas de Grill ---
        GRILL_COOKING_RECIPES.forEach((input, entry) ->
                grillCookingRecipe(this.output, input, entry.result(), entry.cookTime())
        );

        cuttingRecipe(this.output, Ingredient.of(ModItems.CHEESE.get()), new ItemStack(ModItems.CHEESE_SLICE.get(), 4));
        cuttingRecipe(this.output, Ingredient.of(Items.CHICKEN), new ItemStack(ModItems.RAW_CHICKEN_PIECES.get(), 2));

        panMixRecipe(this.output, "scrambled_eggs",
                new ItemStack(ModItems.SCRAMBLED_EGGS.get()),
                Ingredient.of(Items.EGG), Ingredient.of(ModItems.CHEESE_SLICE.get()));

        cupYogurtRecipe(this.output, IronCupContent.MILK,
                List.of(CupContentIngredient.of(IronCupContent.MILK), Ingredient.of(Items.SUGAR)),
                IronCupContent.YOGURT);

        cupYogurtRecipe(this.output, IronCupContent.MILK,
                List.of(CupContentIngredient.of(IronCupContent.MILK), Ingredient.of(Items.SUGAR), Ingredient.of(ModItems.STRAWBERRY.get())),
                IronCupContent.STRAWBERRY_YOGURT);

        cupYogurtRecipe(this.output, IronCupContent.YOGURT,
                List.of(CupContentIngredient.of(IronCupContent.YOGURT), Ingredient.of(ModItems.STRAWBERRY.get())),
                IronCupContent.STRAWBERRY_YOGURT);

        shapeless(RecipeCategory.MISC, ModItems.ORGANIC_MIXTURE.get())
                .requires(Items.BONE_MEAL, 4)
                .requires(Items.ROTTEN_FLESH, 2)
                .requires(Items.WHEAT, 2)
                .unlockedBy(getHasName(Items.ROTTEN_FLESH), has(Items.ROTTEN_FLESH))
                .save(this.output);

        shapeless(RecipeCategory.MISC, ModItems.ORGANIC_SOIL.get())
                .requires(ModItems.ORGANIC_MIXTURE.get())
                .requires(Items.DIRT)
                .unlockedBy(getHasName(ModItems.ORGANIC_MIXTURE.get()), has(ModItems.ORGANIC_MIXTURE.get()))
                .save(this.output);
    }

    private void grillCookingRecipe(RecipeOutput output, Item input, Item result, int cookTime) {
        CookRecipeBuilder.cooking(Ingredient.of(input), new ItemStack(result), cookTime)
                .unlockedBy(getHasName(input), has(input))
                .save(output, recipeKey(recipeNameFor(result) + "_grill"));
    }

    private void cuttingRecipe(RecipeOutput output, Ingredient input, ItemStack result) {
        output.accept(recipeKey(recipeNameFor(result.getItem()) + "_cutting"), new CuttingRecipe(input, result), null);
    }

    @SuppressWarnings("SameParameterValue")
    private void panMixRecipe(RecipeOutput output, String name, ItemStack result, Ingredient... inputs) {
        ItemLike firstItem = inputs[0].items().getFirst().value();
        MixRecipeBuilder.mixing(result, inputs)
                .unlockedBy(getHasName(firstItem), has(firstItem))
                .save(output, recipeKey(name + "_skillet"));
    }

    private void cupYogurtRecipe(RecipeOutput output, IronCupContent sourceContent, List<Ingredient> ingredients, IronCupContent resultContent) {
        ItemStack result = IronCupItem.filled(ModItems.IRON_CUP.get(), resultContent);
        ShapelessCupCraftingRecipe recipe = new ShapelessCupCraftingRecipe(ingredients, result);
        String name = "iron_cup_" + resultContent.getSerializedName() + "_from_" + sourceContent.getSerializedName();
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
*///?}

    private String recipeNameFor(Item item) {
        //? if <1.21.2 {
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item).getPath();
        //?} else {
        /*return net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item).getPath();
         *///?}
    }
}