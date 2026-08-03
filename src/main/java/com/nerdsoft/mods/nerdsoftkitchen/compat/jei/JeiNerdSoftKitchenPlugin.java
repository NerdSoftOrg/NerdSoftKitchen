package com.nerdsoft.mods.nerdsoftkitchen.compat.jei;

//? if <1.21.2 {
/*import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.compat.jei.category.GrillCookingCategory;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.recipe.cook.CookRecipe;
import com.nerdsoft.mods.nerdsoftkitchen.registry.data.ModDataComponents;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import com.nerdsoft.mods.nerdsoftkitchen.registry.recipe.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class JeiNerdSoftKitchenPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID =
            ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "jei_plugin");
    private static final ISubtypeInterpreter<ItemStack> IRON_CUP_SUBTYPE_INTERPRETER = new ISubtypeInterpreter<>() {
        @Override
        public @Nullable Object getSubtypeData(@NotNull ItemStack stack, @NotNull UidContext context) {
            IronCupContent content = stack.get(ModDataComponents.IRON_CUP_CONTENT.get());
            return content == null ? null : content.getSerializedName();
        }

        @Override
        @Deprecated
        @SuppressWarnings("deprecated")
        public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack stack, @NotNull UidContext context) {
            IronCupContent content = stack.get(ModDataComponents.IRON_CUP_CONTENT.get());
            return content == null ? "" : content.getSerializedName();
        }
    };

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(
                ModItems.IRON_CUP.get(),
                IRON_CUP_SUBTYPE_INTERPRETER
        );
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GrillCookingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.GRILL_TABLE.get()), GrillCookingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.GRILL_TABLE_SOUL.get()), GrillCookingCategory.RECIPE_TYPE);
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

        List<CookRecipe> mergedRecipes = new ArrayList<>(collectGrillRecipes(recipeManager));
        mergedRecipes.addAll(collectVanillaCampfireRecipes(recipeManager, registries));

        jeiRuntime.getRecipeManager().addRecipes(GrillCookingCategory.RECIPE_TYPE, mergedRecipes);
    }

    private List<CookRecipe> collectGrillRecipes(RecipeManager recipeManager) {
        return recipeManager
                .getAllRecipesFor(ModRecipeTypes.COOK_TYPE.get())
                .stream()
                .map(RecipeHolder::value)
                .toList();
    }

    private List<CookRecipe> collectVanillaCampfireRecipes(RecipeManager recipeManager, HolderLookup.Provider registries) {
        return recipeManager
                .getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.CAMPFIRE_COOKING)
                .stream()
                .map(RecipeHolder::value)
                .map(recipe -> toCookRecipe(recipe, registries))
                .toList();
    }

    private CookRecipe toCookRecipe(CampfireCookingRecipe recipe, HolderLookup.Provider registries) {
        return new CookRecipe(recipe.getIngredients().getFirst(), recipe.getResultItem(registries), recipe.getCookingTime());
    }
}
*///?}