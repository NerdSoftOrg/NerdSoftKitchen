package com.nerdsoft.mods.nerdsoftkitchen.item.component;

import com.mojang.serialization.Codec;
import com.nerdsoft.mods.nerdsoftkitchen.food.ModFoods;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public enum IronCupContent implements StringRepresentable {
    MILK("milk", ModFoods.MILK),
    YOGURT("yogurt", ModFoods.YOGURT),
    STRAWBERRY_YOGURT("strawberry_yogurt", ModFoods.STRAWBERRY_YOGURT);

    public static final Codec<IronCupContent> CODEC = StringRepresentable.fromEnum(IronCupContent::values);
    private static final IronCupContent[] VALUES = values();
    public static final StreamCodec<ByteBuf, IronCupContent> STREAM_CODEC =
            ByteBufCodecs.STRING_UTF8.map(IronCupContent::byName, IronCupContent::getSerializedName);
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
        throw new IllegalArgumentException("Unknown IronCupContent: " + name);
    }

    @SuppressWarnings("unused")
    public static IronCupContent byModelIndex(int index) {
        return VALUES[index];
    }

    public static List<ItemStack> allFilledStacks(Item cupItem) {
        List<ItemStack> stacks = new java.util.ArrayList<>(VALUES.length);
        for (IronCupContent content : VALUES) {
            stacks.add(com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem.filled(cupItem, content));
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