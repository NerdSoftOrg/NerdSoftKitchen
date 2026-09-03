package com.panzer.mods.dice_and_delish.item.component;

import com.mojang.serialization.Codec;
import com.panzer.mods.dice_and_delish.food.ModFoods;
import com.panzer.mods.dice_and_delish.item.IronCupItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public enum IronCupContent implements StringRepresentable {
    MILK("milk", ModFoods.MILK),
    YOGURT("yogurt", ModFoods.YOGURT),
    STRAWBERRY_YOGURT("strawberry_yogurt", ModFoods.STRAWBERRY_YOGURT),
    LIQUID_EGG("liquid_egg", ModFoods.LIQUID_EGG);

    public static final Codec<IronCupContent> CODEC = StringRepresentable.fromEnum(IronCupContent::values);
    private static final IronCupContent[] VALUES = values();

    private static final IntFunction<IronCupContent> BY_ID = ByIdMap.continuous(
            IronCupContent::ordinal,
            VALUES,
            ByIdMap.OutOfBoundsStrategy.ZERO
    );

    public static final StreamCodec<ByteBuf, IronCupContent> STREAM_CODEC =
            ByteBufCodecs.idMapper(BY_ID, IronCupContent::ordinal);

    private final String name;
    private final FoodProperties food;

    IronCupContent(String name, FoodProperties food) {
        this.name = name;
        this.food = food;
    }

    public static IronCupContent byName(String name) {
        for (IronCupContent value : VALUES) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return MILK;
    }

    @SuppressWarnings("unused")
    public static IronCupContent byModelIndex(int index) {
        if (index < 0 || index >= VALUES.length) return MILK;
        return VALUES[index];
    }

    public static List<ItemStack> allFilledStacks(Item cupItem) {
        List<ItemStack> stacks = new ArrayList<>(VALUES.length);
        for (IronCupContent content : VALUES) {
            stacks.add(IronCupItem.filled(cupItem, content));
        }
        return stacks;
    }

    public int modelIndex() {
        return this.ordinal();
    }

    public FoodProperties food() {
        return this.food;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
