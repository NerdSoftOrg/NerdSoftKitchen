package com.panzer.mods.dice_and_delish.datagen.recipe;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.item.IronCupItem;
import com.panzer.mods.dice_and_delish.item.component.IronCupContent;
import com.panzer.mods.dice_and_delish.recipe.cup.CupContentIngredient;
import com.panzer.mods.dice_and_delish.recipe.cup.ShapelessCupCraftingRecipe;
import com.panzer.mods.dice_and_delish.recipe.cutting.CuttingRecipe;
import com.panzer.mods.dice_and_delish.registry.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
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

//? if >=1.21.2 {
/*import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
*///?} else {
import net.minecraft.data.DataProvider;
//?}

public class ModRecipeProvider extends RecipeProvider /*? if <1.21.2 {*/ implements DataProvider /*?}*/ {

    private static final int DEFAULT_GRILL_COOK_TIME = 600;
    private static final int FAST_GRILL_COOK_TIME = 300;

    private static final Map<ItemLike, CookingEntry> GRILL_COOKING_RECIPES = Map.of(
            ModItems.RAW_SANDWICH_BREAD, new CookingEntry(ModItems.TOASTED_SANDWICH_BREAD, FAST_GRILL_COOK_TIME),
            ModItems.CHEESE_RAW_SANDWICH, new CookingEntry(ModItems.CHEESE_TOASTED_SANDWICH, FAST_GRILL_COOK_TIME),
            ModItems.RAW_CHICKEN_PIECES, new CookingEntry(ModItems.COOKED_CHICKEN_PIECES, DEFAULT_GRILL_COOK_TIME),
            Items.EGG, new CookingEntry(ModItems.FRIED_EGG, FAST_GRILL_COOK_TIME),
            ModItems.CHEESE_SLICE, new CookingEntry(ModItems.GRILLED_CHEESE, FAST_GRILL_COOK_TIME),
            ModItems.RICE_BOWL, new CookingEntry(ModItems.COOKED_RICE_BOWL, DEFAULT_GRILL_COOK_TIME)
    );

    private record CookingEntry(ItemLike result, int cookTime) {}

    //? if <1.21.2 {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        generateAllRecipes(output);
    }
    //?} else {
    /*protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes() {
        generateAllRecipes(this.output);
    }
    *///?}

    private void generateAllRecipes(RecipeOutput output) {
        createShaped(RecipeCategory.MISC, ModItems.IRON_CUP).pattern("N N").pattern(" N ")
                .define('N', Items.IRON_NUGGET).unlockedBy(getHasName(Items.IRON_NUGGET), has(Items.IRON_NUGGET)).save(output);

        createShaped(RecipeCategory.MISC, ModItems.GRILL_TABLE).pattern("I").pattern("S").pattern("C")
                .define('I', Items.IRON_INGOT).define('S', Items.STONE_BRICKS).define('C', Items.CAMPFIRE)
                .unlockedBy(getHasName(Items.CAMPFIRE), has(Items.CAMPFIRE)).save(output);

        saveCustomKey(createShaped(RecipeCategory.MISC, ModItems.GRILL_TABLE_SOUL).pattern("I").pattern("S").pattern("C")
                .define('I', Items.IRON_INGOT).define('S', Items.STONE_BRICKS).define('C', Items.SOUL_CAMPFIRE)
                .unlockedBy(getHasName(Items.SOUL_CAMPFIRE), has(Items.SOUL_CAMPFIRE)), output, "grill_table_soul");

        saveCustomKey(createShapeless(RecipeCategory.MISC, ModItems.GRILL_TABLE)
                        .requires(ModItems.GRILL_TABLE_UNLIT).requires(Items.BLAZE_POWDER)
                        .unlockedBy(getHasName(ModItems.GRILL_TABLE_UNLIT), has(ModItems.GRILL_TABLE_UNLIT)),
                output, "grill_table_relight");

        saveCustomKey(createShapeless(RecipeCategory.MISC, ModItems.GRILL_TABLE_SOUL)
                        .requires(ModItems.GRILL_TABLE_UNLIT).requires(Items.BLAZE_POWDER)
                        .requires(Ingredient.of(Items.SOUL_SAND, Items.SOUL_SOIL))
                        .unlockedBy(getHasName(ModItems.GRILL_TABLE_UNLIT), has(ModItems.GRILL_TABLE_UNLIT)),
                output, "grill_table_soul_relight");

        saveCustomKey(createShapeless(RecipeCategory.MISC, ModItems.GRILL_TABLE_SOUL)
                        .requires(ModItems.GRILL_TABLE_SOUL_UNLIT).requires(Items.BLAZE_POWDER)
                        .unlockedBy(getHasName(ModItems.GRILL_TABLE_SOUL_UNLIT), has(ModItems.GRILL_TABLE_SOUL_UNLIT)),
                output, "grill_table_soul_unlit_relight");

        createShaped(RecipeCategory.MISC, ModItems.CUTTING_BOARD).pattern("PPP")
                .define('P', Items.OAK_PLANKS).unlockedBy(getHasName(Items.OAK_PLANKS), has(Items.OAK_PLANKS)).save(output);

        knifeRecipe(output, ModItems.STONE_KNIFE, Items.COBBLESTONE);
        knifeRecipe(output, ModItems.IRON_KNIFE, Items.IRON_INGOT);
        knifeRecipe(output, ModItems.GOLDEN_KNIFE, Items.GOLD_INGOT);
        knifeRecipe(output, ModItems.DIAMOND_KNIFE, Items.DIAMOND);
        knifeRecipe(output, ModItems.OBSIDIAN_KNIFE, Items.OBSIDIAN);

        toNetheriteSmithing(output, ModItems.DIAMOND_KNIFE, RecipeCategory.TOOLS, ModItems.NETHERITE_KNIFE);

        createShapeless(RecipeCategory.FOOD, ModItems.SALAD).requires(ModItems.TOMATO).requires(ModItems.LETTUCE).requires(Items.BOWL)
                .unlockedBy(getHasName(ModItems.LETTUCE), has(ModItems.LETTUCE)).save(output);

        createShapeless(RecipeCategory.FOOD, ModItems.RICE_BOWL).requires(ModItems.RICE).requires(Items.BOWL)
                .unlockedBy(getHasName(ModItems.RICE), has(ModItems.RICE)).save(output);

        createShapeless(RecipeCategory.FOOD, ModItems.CHEESE).requires(Items.MILK_BUCKET, 3)
                .unlockedBy(getHasName(Items.MILK_BUCKET), has(Items.MILK_BUCKET)).save(output);

        createShapeless(RecipeCategory.FOOD, ModItems.CHEESE_RAW_SANDWICH).requires(ModItems.RAW_SANDWICH_BREAD, 2).requires(ModItems.CHEESE_SLICE)
                .unlockedBy(getHasName(ModItems.CHEESE_SLICE), has(ModItems.CHEESE_SLICE)).save(output);

        createShaped(RecipeCategory.TOOLS, ModItems.SKILLET).pattern("III").pattern("I I").pattern(" S ")
                .define('I', Items.IRON_INGOT).define('S', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT)).save(output);

        GRILL_COOKING_RECIPES.forEach((input, entry) -> grillCookingRecipe(output, input, entry.result(), entry.cookTime()));

        cuttingRecipe(output, Ingredient.of(ModItems.CHEESE), new ItemStack(ModItems.CHEESE_SLICE.get(), 4));
        cuttingRecipe(output, Ingredient.of(Items.CHICKEN), new ItemStack(ModItems.RAW_CHICKEN_PIECES.get(), 2));
        cuttingRecipe(output, Ingredient.of(Items.POTATO), new ItemStack(ModItems.CUT_POTATO.get(), 2));
        cuttingRecipe(output, Ingredient.of(ModItems.PURPLE_ONION), new ItemStack(ModItems.CUT_PURPLE_ONION.get(), 2));

        panMixRecipe(output, "tortilla", new ItemStack(ModItems.TORTILLA.get()), Ingredient.of(Items.EGG));
        panMixRecipe(output, "potato_tortilla", new ItemStack(ModItems.POTATO_TORTILLA.get()), Ingredient.of(Items.EGG), Ingredient.of(ModItems.CUT_POTATO));
        panMixRecipe(output, "onion_tortilla", new ItemStack(ModItems.PURPLE_ONION_TORTILLA.get()), Ingredient.of(Items.EGG), Ingredient.of(ModItems.CUT_PURPLE_ONION));

        cupYogurtRecipe(output, IronCupContent.MILK, List.of(CupContentIngredient.of(IronCupContent.MILK), Ingredient.of(Items.SUGAR)), IronCupContent.YOGURT);
        cupYogurtRecipe(output, IronCupContent.MILK, List.of(CupContentIngredient.of(IronCupContent.MILK), Ingredient.of(Items.SUGAR), Ingredient.of(ModItems.STRAWBERRY)), IronCupContent.STRAWBERRY_YOGURT);
        cupYogurtRecipe(output, IronCupContent.YOGURT, List.of(CupContentIngredient.of(IronCupContent.YOGURT), Ingredient.of(ModItems.STRAWBERRY)), IronCupContent.STRAWBERRY_YOGURT);

        createShapeless(RecipeCategory.MISC, ModItems.ORGANIC_MIXTURE).requires(Items.BONE_MEAL, 4).requires(Items.ROTTEN_FLESH, 2).requires(Items.WHEAT, 2)
                .unlockedBy(getHasName(Items.ROTTEN_FLESH), has(Items.ROTTEN_FLESH)).save(output);

        createShapeless(RecipeCategory.MISC, ModItems.ORGANIC_SOIL).requires(ModItems.ORGANIC_MIXTURE).requires(Items.DIRT)
                .unlockedBy(getHasName(ModItems.ORGANIC_MIXTURE), has(ModItems.ORGANIC_MIXTURE)).save(output);
    }

    private void grillCookingRecipe(RecipeOutput output, ItemLike input, ItemLike result, int cookTime) {
        saveCustomKey(CookRecipeBuilder.cooking(Ingredient.of(input), new ItemStack(result), cookTime)
                .unlockedBy(getHasName(input), has(input)), output, recipeNameFor(result.asItem()) + "_grill");
    }

    private void knifeRecipe(RecipeOutput output, ItemLike knife, ItemLike material) {
        createShaped(RecipeCategory.TOOLS, knife).pattern(" M").pattern("S ")
                .define('M', material).define('S', Items.STICK)
                .unlockedBy(getHasName(material), has(material)).save(output);
    }

    @SuppressWarnings("SameParameterValue")
    private void toNetheriteSmithing(RecipeOutput output, ItemLike ingredientItem, RecipeCategory category, ItemLike resultItem) {
        //? if <1.21.2 {
        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(ingredientItem),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        category,
                        resultItem.asItem()
                )
                .unlocks(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT))
                .save(output, id(recipeNameFor(resultItem.asItem()) + "_smithing"));
        //?} else {
        /*SmithingTransformRecipeBuilder.smithing(
                        Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                        Ingredient.of(ingredientItem),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        category,
                        resultItem.asItem()
                    )
                    .unlocks(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT))
                    .save(output, recipeKey(recipeNameFor(resultItem.asItem()) + "_smithing"));
    *///?}
    }

    private void cuttingRecipe(RecipeOutput output, Ingredient input, ItemStack result) {
        //? if <1.21.2 {
        output.accept(id(recipeNameFor(result.getItem()) + "_cutting"), new CuttingRecipe(input, result), null);
        //?} else {
        /*output.accept(recipeKey(recipeNameFor(result.getItem()) + "_cutting"), new CuttingRecipe(input, result, this.registries), null);
         *///?}
    }

    private void panMixRecipe(RecipeOutput output, String name, ItemStack result, Ingredient... inputs) {
        //? if <1.21.2 {
        ItemLike firstItem = inputs[0].getItems()[0].getItem();
        //?} else {
        /*ItemLike firstItem = inputs[0].items().getFirst().value();
         *///?}
        saveCustomKey(MixRecipeBuilder.mixing(result, inputs)
                .unlockedBy(getHasName(firstItem), has(firstItem)), output, name + "_skillet");
    }

    private void cupYogurtRecipe(RecipeOutput output, IronCupContent sourceContent, List<Ingredient> ingredients, IronCupContent resultContent) {
        ItemStack result = IronCupItem.filled(ModItems.IRON_CUP.get(), resultContent);
        ShapelessCupCraftingRecipe recipe = new ShapelessCupCraftingRecipe(ingredients, result);
        String name = "iron_cup_" + resultContent.getSerializedName() + "_from_" + sourceContent.getSerializedName();
        //? if <1.21.2 {
        output.accept(id(name), recipe, null);
        //?} else {
        /*output.accept(recipeKey(name), recipe, null);
         *///?}
    }

    private void saveCustomKey(RecipeBuilder builder, RecipeOutput output, String name) {
        //? if <1.21.2 {
        builder.save(output, id(name));
        //?} else {
        /*builder.save(output, recipeKey(name));
         *///?}
    }

    protected ShapedRecipeBuilder createShaped(RecipeCategory category, ItemLike result) {
        //? if <1.21.2 {
        return ShapedRecipeBuilder.shaped(category, result);
        //?} else {
        /*return super.shaped(category, result);
         *///?}
    }

    protected ShapelessRecipeBuilder createShapeless(RecipeCategory category, ItemLike result) {
        //? if <1.21.2 {
        return ShapelessRecipeBuilder.shapeless(category, result);
        //?} else {
        /*return super.shapeless(category, result);
         *///?}
    }

    private ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, name);
    }

    //? if >=1.21.2 {
    /*private ResourceKey<Recipe<?>> recipeKey(String name) {
        return ResourceKey.create(Registries.RECIPE, id(name));
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
            return "DiceAndDelish Recipes";
        }
    }
    *///?}

    private String recipeNameFor(Item item) {
        return BuiltInRegistries.ITEM.getKey(item).getPath();
    }
}
