package com.panzer.mods.dice_and_delish.recipe.cup;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.panzer.mods.dice_and_delish.item.IronCupItem;
import com.panzer.mods.dice_and_delish.item.component.IronCupContent;
import com.panzer.mods.dice_and_delish.registry.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public record CupContentIngredient(IronCupContent content) implements ICustomIngredient {

    public static final MapCodec<CupContentIngredient> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(IronCupContent.CODEC.fieldOf("content").forGetter(CupContentIngredient::content))
                    .apply(instance, CupContentIngredient::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, CupContentIngredient> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8.map(IronCupContent::byName, IronCupContent::getSerializedName),
                    CupContentIngredient::content,
                    CupContentIngredient::new
            );

    public static final IngredientType<CupContentIngredient> TYPE = new IngredientType<>(CODEC, STREAM_CODEC);

    public static Ingredient of(IronCupContent content) {
        return new CupContentIngredient(content).toVanilla();
    }

    @Override
    public boolean test(@NotNull ItemStack stack) {
        return stack.is(ModItems.IRON_CUP.get()) && content == IronCupItem.contentOf(stack);
    }

    //? if <1.21.2 {
    @Override
    public @NotNull Stream<ItemStack> getItems() {
        return Stream.of(IronCupItem.filled(ModItems.IRON_CUP.get(), content));
    }
    //?} else {
    /*@Override
    public @NotNull Stream<Holder<Item>> items() {
        return Stream.of(ModItems.IRON_CUP.getDelegate());
    }
    *///?}

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public @NotNull IngredientType<?> getType() {
        return TYPE;
    }
}
