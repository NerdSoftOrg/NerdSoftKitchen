package com.nerdsoft.mods.nerdsoftkitchen.recipe.cup;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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

    @Override
    public @NotNull Stream<ItemStack> getItems() {
        return Stream.of(IronCupItem.filled(ModItems.IRON_CUP.get(), content));
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public @NotNull IngredientType<?> getType() {
        return TYPE;
    }
}