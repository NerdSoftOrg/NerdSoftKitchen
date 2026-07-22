package com.nerdsoft.mods.nerdsoftkitchen.compat.jade;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.GrillTableBlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import snownee.jade.api.Accessor;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.view.*;

import java.util.List;
import java.util.Optional;

public enum GrillTableItemStorageExtension implements IServerExtensionProvider<ItemStack>, IClientExtensionProvider<ItemStack, ItemView> {
    INSTANCE;

    private static final MapCodec<Integer> COOKING_TIME_CODEC = Codec.INT.fieldOf("nerdsoftkitchen:cooking");
    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath("nerdsoftkitchen", "grill_table_progress");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public List<ViewGroup<ItemStack>> getGroups(Accessor<?> accessor) {
        if (!(accessor.getTarget() instanceof GrillTableBlockEntity grill)) {
            return null;
        }
        ViewGroup<ItemStack> grillRow = buildRow(grill, GrillTableBlockEntity.GRILL_SLOTS_START, GrillTableBlockEntity.GRILL_SLOTS_COUNT);
        ViewGroup<ItemStack> campfireRow = buildRow(grill, GrillTableBlockEntity.CAMPFIRE_SLOTS_START, GrillTableBlockEntity.CAMPFIRE_SLOTS_COUNT);
        if (grillRow == null && campfireRow == null) {
            return List.of();
        }
        List<ViewGroup<ItemStack>> rows = Lists.newArrayListWithCapacity(2);
        if (grillRow != null) {
            rows.add(grillRow);
        }
        if (campfireRow != null) {
            rows.add(campfireRow);
        }
        return rows;
    }

    private ViewGroup<ItemStack> buildRow(GrillTableBlockEntity grill, int start, int count) {
        List<ItemStack> views = Lists.newArrayListWithCapacity(count);
        for (int i = 0; i < count; i++) {
            int slot = start + i;
            ItemStack stack = grill.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            views.add(buildView(grill, slot, stack));
        }
        if (views.isEmpty()) {
            return null;
        }
        return new ViewGroup<>(views);
    }

    private ItemStack buildView(GrillTableBlockEntity grill, int slot, ItemStack stack) {
        int cookTime = grill.getCookTime(slot);
        if (cookTime <= 0) {
            return stack.copy();
        }
        int remainingTicks = Math.max(0, cookTime - grill.getCookProgress(slot));
        ItemStack tagged = stack.copy();
        CustomData customData = tagged.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).update(
                NbtOps.INSTANCE,
                COOKING_TIME_CODEC,
                remainingTicks).getOrThrow();
        tagged.set(DataComponents.CUSTOM_DATA, customData);
        return tagged;
    }

    @Override
    public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> groups) {
        return ClientViewGroup.map(groups, stack -> {
            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            if (customData.isEmpty()) {
                return new ItemView(stack);
            }
            Optional<Integer> remainingTicks = customData.read(COOKING_TIME_CODEC).result();
            if (remainingTicks.isEmpty()) {
                return new ItemView(stack);
            }
            String text = IThemeHelper.get().seconds(remainingTicks.get(), accessor.tickRate()).getString();
            return new ItemView(stack).amountText(text);
        }, null);
    }
}