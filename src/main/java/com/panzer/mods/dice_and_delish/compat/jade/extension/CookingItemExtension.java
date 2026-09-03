package com.panzer.mods.dice_and_delish.compat.jade.extension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.ItemStackElement;

import java.util.List;

public abstract class CookingItemExtension<T extends BlockEntity> implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    private final ResourceLocation uid;
    private final Class<T> blockEntityClass;
    private final String nbtKey;

    protected CookingItemExtension(ResourceLocation uid, Class<T> blockEntityClass, String nbtKey) {
        this.uid = uid;
        this.blockEntityClass = blockEntityClass;
        this.nbtKey = nbtKey;
    }

    @Override
    public ResourceLocation getUid() {
        return uid;
    }

    protected abstract int getSlotCount(T blockEntity);
    protected abstract int getCookTime(T blockEntity, int slot);
    protected abstract int getCookProgress(T blockEntity, int slot);

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (!blockEntityClass.isInstance(accessor.getBlockEntity())) {
            return;
        }

        T blockEntity = blockEntityClass.cast(accessor.getBlockEntity());
        int totalSlots = getSlotCount(blockEntity);
        ListTag itemsList = new ListTag();

        for (int slot = 0; slot < totalSlots; slot++) {
            ItemStack stack = getItemInSlot(blockEntity, slot);
            if (stack.isEmpty()) {
                continue;
            }

            CompoundTag itemTag = new CompoundTag();
            itemTag.putInt("Slot", slot);

            Tag customStackTag = stack.save(accessor.getLevel().registryAccess());
            itemTag.put("Item", customStackTag);

            int cookTime = getCookTime(blockEntity, slot);
            int remainingTicks = (cookTime > 0) ? Math.max(0, cookTime - getCookProgress(blockEntity, slot)) : 0;
            itemTag.putInt("Remaining", remainingTicks);

            itemsList.add(itemTag);
        }

        // Skip writing the (empty) tag entirely when the block has nothing to show
        if (!itemsList.isEmpty()) {
            data.put(nbtKey, itemsList);
        }
    }

    protected abstract ItemStack getItemInSlot(T blockEntity, int slot);

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        if (!data.contains(nbtKey)) {
            return;
        }

        ListTag itemsList = data.getList(nbtKey, 10);
        if (itemsList.isEmpty()) {
            return;
        }

        buildTooltipLayout(tooltip, accessor, itemsList);
    }

    protected abstract void buildTooltipLayout(ITooltip tooltip, BlockAccessor accessor, ListTag itemsList);

    protected IElement createItemElement(BlockAccessor accessor, CompoundTag itemTag) {
        ItemStack stack = ItemStack.parse(accessor.getLevel().registryAccess(), itemTag.getCompound("Item")).orElse(ItemStack.EMPTY);
        if (stack.isEmpty()) {
            return null;
        }

        int remainingTicks = itemTag.getInt("Remaining");
        if (remainingTicks > 0) {
            String secondsText = IThemeHelper.get().seconds(remainingTicks, accessor.tickRate()).getString();
            return ItemStackElement.of(stack, 1.0f, secondsText);
        }
        return IElementHelper.get().item(stack);
    }

    protected void appendRowToTooltip(ITooltip tooltip, List<IElement> rowElements) {
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
