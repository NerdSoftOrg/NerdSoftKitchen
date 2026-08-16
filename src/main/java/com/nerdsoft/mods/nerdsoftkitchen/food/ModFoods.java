package com.nerdsoft.mods.nerdsoftkitchen.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public final class ModFoods {

    /// Raw Ingredients
    public static final FoodProperties STRAWBERRY =
            new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build();
    public static final FoodProperties TOMATO =
            new FoodProperties.Builder().nutrition(2).saturationModifier(0.3f).alwaysEdible().build();
    public static final FoodProperties WILD_TOMATO =
            new FoodProperties.Builder().nutrition(2).saturationModifier(0.25f).alwaysEdible().build();
    public static final FoodProperties LETTUCE =
            new FoodProperties.Builder().nutrition(1).saturationModifier(0.2f).alwaysEdible().build();
    public static final FoodProperties PURPLE_ONION_RAW =
            new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f).alwaysEdible().build();
    @SuppressWarnings("deprecation")
    public static final FoodProperties RAW_CHICKEN_PIECES =
            new FoodProperties.Builder().nutrition(1).saturationModifier(0.15f).alwaysEdible()
                    .effect(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.15F).build();
    public static final FoodProperties RAW_SANDWICH_BREAD =
            new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f).alwaysEdible().build();

    /// Dairy products
    public static final FoodProperties MILK =
            new FoodProperties.Builder().nutrition(0).saturationModifier(0f).alwaysEdible().build();
    public static final FoodProperties YOGURT =
            new FoodProperties.Builder().nutrition(3).saturationModifier(0.6f).build();
    public static final FoodProperties STRAWBERRY_YOGURT =
            new FoodProperties.Builder().nutrition(5).saturationModifier(0.6f).build();
    public static final FoodProperties CHEESE =
            new FoodProperties.Builder().nutrition(5).saturationModifier(0.5f).build();
    public static final FoodProperties CHEESE_SLICE =
            new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).build();

    /// Cooked and Prepared Meals
    public static final FoodProperties COOKED_CHICKEN_PIECES =
            new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f).build();
    public static final FoodProperties FRIED_EGG =
            new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties SALAD =
            new FoodProperties.Builder().nutrition(5).saturationModifier(0.4f).build();
    public static final FoodProperties TOASTED_SANDWICH_BREAD =
            new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties CHEESE_RAW_SANDWICH =
            new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).build();
    public static final FoodProperties CHEESE_TOASTED_SANDWICH =
            new FoodProperties.Builder().nutrition(9).saturationModifier(0.7f).build();
    public static final FoodProperties GRILLED_CHEESE =
            new FoodProperties.Builder().nutrition(4).saturationModifier(0.5f).build();
    public static final FoodProperties SCRAMBLED_EGGS =
            new FoodProperties.Builder().nutrition(6).saturationModifier(0.5f).build();

    private ModFoods() {
    }
}