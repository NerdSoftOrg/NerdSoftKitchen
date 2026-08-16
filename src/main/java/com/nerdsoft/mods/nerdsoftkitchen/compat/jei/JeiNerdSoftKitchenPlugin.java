package com.nerdsoft.mods.nerdsoftkitchen.compat.jei;

//? if <1.21.2 {

/*import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.compat.jei.category.GrillCookingCategory;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
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
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
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
@SuppressWarnings("unused")
public class JeiNerdSoftKitchenPlugin implements IModPlugin {

    private static final ResourceLocation PLUGIN_ID =
            ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(
                VanillaTypes.ITEM_STACK,
                ModItems.IRON_CUP.get(),
                new IronCupSubtypeInterpreter(ModDataComponents.IRON_CUP_CONTENT.get())
        );
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GrillCookingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(ModItems.GRILL_TABLE.get()),
                GrillCookingCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(
                new ItemStack(ModItems.GRILL_TABLE_SOUL.get()),
                GrillCookingCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        registration.addItemStackInfo(
                new ItemStack(ModItems.GRILL_TABLE.get()),
                Component.translatable("nerdsoftkitchen.jei.info.grill_table")
        );
        registration.addItemStackInfo(
                new ItemStack(ModItems.GRILL_TABLE_SOUL.get()),
                Component.translatable("nerdsoftkitchen.jei.info.grill_table_soul")
        );
        registration.addItemStackInfo(
                new ItemStack(ModItems.IRON_CUP.get()),
                Component.translatable("nerdsoftkitchen.jei.info.iron_cup")
        );
        registration.addItemStackInfo(
                IronCupItem.filled(ModItems.IRON_CUP.get(), IronCupContent.MILK),
                Component.translatable("nerdsoftkitchen.jei.info.iron_cup_milk")
        );
        registration.addItemStackInfo(
                IronCupItem.filled(ModItems.IRON_CUP.get(), IronCupContent.YOGURT),
                Component.translatable("nerdsoftkitchen.jei.info.iron_cup_yogurt")
        );
        registration.addItemStackInfo(
                IronCupItem.filled(ModItems.IRON_CUP.get(), IronCupContent.STRAWBERRY_YOGURT),
                Component.translatable("nerdsoftkitchen.jei.info.iron_cup_strawberry_yogurt")
        );
        registration.addItemStackInfo(
                new ItemStack(ModItems.CUTTING_BOARD.get()),
                Component.translatable("nerdsoftkitchen.jei.info.cutting_board")
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

        List<RecipeHolder<CookRecipe>> mergedRecipes = new ArrayList<>(collectGrillRecipes(recipeManager));
        mergedRecipes.addAll(collectVanillaCampfireRecipes(recipeManager, registries));

        jeiRuntime.getRecipeManager().addRecipes(GrillCookingCategory.RECIPE_TYPE, mergedRecipes);
    }

    private List<RecipeHolder<CookRecipe>> collectGrillRecipes(RecipeManager recipeManager) {
        return recipeManager.getAllRecipesFor(ModRecipeTypes.COOK_TYPE.get());
    }

    private List<RecipeHolder<CookRecipe>> collectVanillaCampfireRecipes(RecipeManager recipeManager, HolderLookup.Provider registries) {
        return recipeManager
                .getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.CAMPFIRE_COOKING)
                .stream()
                .map(holder -> toCookRecipeHolder(holder, registries))
                .toList();
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

    private record IronCupSubtypeInterpreter(
            DataComponentType<IronCupContent> componentType
    ) implements ISubtypeInterpreter<ItemStack> {

        @Override
        public @Nullable String getSubtypeData(@NotNull ItemStack ingredient, @NotNull UidContext context) {
            IronCupContent content = ingredient.get(componentType);
            return content != null ? content.getSerializedName() : null;
        }

        @Override
        public @NotNull String getLegacyStringSubtypeInfo(@NotNull ItemStack ingredient, @NotNull UidContext context) {
            return "";
        }
    }
}
*///?}