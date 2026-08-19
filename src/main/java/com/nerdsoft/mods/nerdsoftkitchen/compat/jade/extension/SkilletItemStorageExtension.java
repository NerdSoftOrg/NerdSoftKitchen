package com.nerdsoft.mods.nerdsoftkitchen.compat.jade.extension;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.SkilletBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.ItemStackElement;

import java.util.ArrayList;
import java.util.List;

public enum SkilletItemStorageExtension implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "skillet_progress");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof SkilletBlockEntity skillet)) {
            return;
        }

        ListTag itemsList = new ListTag();
        for (int slot = 0; slot < SkilletBlockEntity.PAN_SLOTS_COUNT; slot++) {
            ItemStack stack = skillet.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }

            CompoundTag itemTag = new CompoundTag();
            itemTag.putInt("Slot", slot);

            Tag customStackTag = stack.save(accessor.getLevel().registryAccess());
            itemTag.put("Item", customStackTag);

            int cookTime = skillet.getCookTime(slot);
            int remainingTicks = (cookTime > 0) ? Math.max(0, cookTime - skillet.getCookProgress(slot)) : 0;
            itemTag.putInt("Remaining", remainingTicks);

            itemsList.add(itemTag);
        }

        data.put("SkilletItems", itemsList);
    }

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        if (!data.contains("SkilletItems")) {
            return;
        }

        ListTag itemsList = data.getList("SkilletItems", 10);
        if (itemsList.isEmpty()) {
            return;
        }

        IElementHelper elements = IElementHelper.get();
        List<IElement> row = new ArrayList<>();

        for (int i = 0; i < itemsList.size(); i++) {
            CompoundTag itemTag = itemsList.getCompound(i);

            ItemStack stack = ItemStack.parse(accessor.getLevel().registryAccess(), itemTag.getCompound("Item")).orElse(ItemStack.EMPTY);
            if (stack.isEmpty()) {
                continue;
            }

            int remainingTicks = itemTag.getInt("Remaining");

            IElement itemElement;
            if (remainingTicks > 0) {
                String secondsText = IThemeHelper.get().seconds(remainingTicks, accessor.tickRate()).getString();
                itemElement = ItemStackElement.of(stack, 1.0f, secondsText);
            } else {
                itemElement = elements.item(stack);
            }

            row.add(itemElement);
        }

        appendRowToTooltip(tooltip, row);
    }

    private void appendRowToTooltip(ITooltip tooltip, List<IElement> rowElements) {
        if (rowElements.isEmpty()) {
            return;
        }

        for (int i = 0; i < rowElements.size(); i++) {
            if (i == 0) {
                tooltip.add(rowElements.get(i));
            } else {
                tooltip.append(rowElements.get(i));
            }
        }
    }
}