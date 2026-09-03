package com.panzer.mods.dice_and_delish.compat.jei;

//? if <1.21.2 {

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.compat.jei.category.CuttingBoardCategory;
import com.panzer.mods.dice_and_delish.compat.jei.category.GrillCookingCategory;
import com.panzer.mods.dice_and_delish.compat.jei.category.SkilletCookingCategory;
import com.panzer.mods.dice_and_delish.item.IronCupItem;
import com.panzer.mods.dice_and_delish.item.component.IronCupContent;
import com.panzer.mods.dice_and_delish.recipe.cook.CookRecipe;
import com.panzer.mods.dice_and_delish.recipe.cutting.CuttingRecipe;
import com.panzer.mods.dice_and_delish.recipe.mix.MixRecipe;
import com.panzer.mods.dice_and_delish.registry.item.ModItems;
import com.panzer.mods.dice_and_delish.registry.recipe.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
@SuppressWarnings("unused")
public class JeiModPlugin implements IModPlugin {
    public static final String EMPTY_SUBTYPE = "";

    private static final ResourceLocation PLUGIN_ID =
            ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, "jei_plugin");

    private static final ISubtypeInterpreter<ItemStack> IRON_CUP_INTERPRETER = new ISubtypeInterpreter<>() {
        @Override
        public @NotNull String getSubtypeData(@NotNull ItemStack stack, @NotNull UidContext context) {
            IronCupContent content = IronCupItem.contentOf(stack);
            return content != null ? content.getSerializedName() : EMPTY_SUBTYPE;
        }

        @Override
        @SuppressWarnings({"deprecation", "RedundantSuppression"})
        public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack stack, @NotNull UidContext context) {
            IronCupContent content = IronCupItem.contentOf(stack);
            return content != null ? content.getSerializedName() : EMPTY_SUBTYPE;
        }
    };

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(ModItems.IRON_CUP.get(), IRON_CUP_INTERPRETER);
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();

        registration.addRecipeCategories(
                new GrillCookingCategory(guiHelper),
                new CuttingBoardCategory(guiHelper),
                new SkilletCookingCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModItems.GRILL_TABLE.get()),
                GrillCookingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(
                new ItemStack(ModItems.GRILL_TABLE_SOUL.get()),
                GrillCookingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(
                new ItemStack(ModItems.CUTTING_BOARD.get()),
                CuttingBoardCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(
                new ItemStack(ModItems.SKILLET.get()),
                SkilletCookingCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        registration.addItemStackInfo(
                new ItemStack(ModItems.GRILL_TABLE.get()),
                Component.translatable("dice_and_delish.jei.info.grill_table")
        );
        registration.addItemStackInfo(
                new ItemStack(ModItems.GRILL_TABLE_SOUL.get()),
                Component.translatable("dice_and_delish.jei.info.grill_table_soul")
        );
        registration.addItemStackInfo(
                new ItemStack(ModItems.IRON_CUP.get()),
                Component.translatable("dice_and_delish.jei.info.iron_cup")
        );
        registration.addItemStackInfo(
                IronCupItem.filled(ModItems.IRON_CUP.get(), IronCupContent.MILK),
                Component.translatable("dice_and_delish.jei.info.iron_cup_milk")
        );
        registration.addItemStackInfo(
                IronCupItem.filled(ModItems.IRON_CUP.get(), IronCupContent.YOGURT),
                Component.translatable("dice_and_delish.jei.info.iron_cup_yogurt")
        );
        registration.addItemStackInfo(
                IronCupItem.filled(ModItems.IRON_CUP.get(), IronCupContent.STRAWBERRY_YOGURT),
                Component.translatable("dice_and_delish.jei.info.iron_cup_strawberry_yogurt")
        );
        registration.addItemStackInfo(
                new ItemStack(ModItems.CUTTING_BOARD.get()),
                Component.translatable("dice_and_delish.jei.info.cutting_board")
        );
        registration.addItemStackInfo(
                new ItemStack(ModItems.SKILLET.get()),
                Component.translatable("dice_and_delish.jei.info.skillet")
        );
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        IIngredientManager ingredientManager = jeiRuntime.getIngredientManager();
        List<ItemStack> filledCups = IronCupContent.allFilledStacks(ModItems.IRON_CUP.get());
        ingredientManager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, filledCups);

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        RecipeManager recipeManager = level.getRecipeManager();
        HolderLookup.Provider registries = level.registryAccess();

        List<RecipeHolder<CookRecipe>> grillRecipes = collectGrillRecipes(recipeManager);
        List<RecipeHolder<CampfireCookingRecipe>> campfireRecipes = recipeManager.getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.CAMPFIRE_COOKING);

        List<RecipeHolder<CookRecipe>> mergedRecipes = new ArrayList<>(grillRecipes.size() + campfireRecipes.size());
        mergedRecipes.addAll(grillRecipes);

        for (RecipeHolder<CampfireCookingRecipe> holder : campfireRecipes) {
            mergedRecipes.add(toCookRecipeHolder(holder, registries));
        }

        jeiRuntime.getRecipeManager().addRecipes(GrillCookingCategory.RECIPE_TYPE, mergedRecipes);
        jeiRuntime.getRecipeManager().addRecipes(CuttingBoardCategory.RECIPE_TYPE, collectCuttingRecipes(recipeManager));
        jeiRuntime.getRecipeManager().addRecipes(SkilletCookingCategory.RECIPE_TYPE, collectMixRecipes(recipeManager));
    }

    private List<RecipeHolder<CuttingRecipe>> collectCuttingRecipes(RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(ModRecipeTypes.CUT_TYPE.get());
    }

    private List<RecipeHolder<MixRecipe>> collectMixRecipes(RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(ModRecipeTypes.MIX_TYPE.get());
    }

    private List<RecipeHolder<CookRecipe>> collectGrillRecipes(RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(ModRecipeTypes.COOK_TYPE.get());
    }

    private RecipeHolder<CookRecipe> toCookRecipeHolder(RecipeHolder<CampfireCookingRecipe> holder, HolderLookup.Provider registries) {
        CampfireCookingRecipe recipe = holder.value();
        CookRecipe cookRecipe = new CookRecipe(
                recipe.getIngredients().getFirst(),
                recipe.getResultItem(registries),
                recipe.getCookingTime()
        );
        return new RecipeHolder<>(holder.id(), cookRecipe);
    }
}
//?}
