package com.nerdsoft.mods.nerdsoftkitchen.compat.jade.extension;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.GrillTableBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.ui.IElement;

import java.util.ArrayList;
import java.util.List;

public class GrillTableItemStorageExtension extends BaseCookingItemExtension<GrillTableBlockEntity> {

    public static final GrillTableItemStorageExtension INSTANCE = new GrillTableItemStorageExtension();

    private GrillTableItemStorageExtension() {
        super(
            ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "grill_table_progress"),
            GrillTableBlockEntity.class,
            "GrillItems"
        );
    }

    @Override
    protected int getSlotCount(GrillTableBlockEntity blockEntity) {
        return GrillTableBlockEntity.TOTAL_SLOTS;
    }

    @Override
    protected int getCookTime(GrillTableBlockEntity blockEntity, int slot) {
        return blockEntity.getCookTime(slot);
    }

    @Override
    protected int getCookProgress(GrillTableBlockEntity blockEntity, int slot) {
        return blockEntity.getCookProgress(slot);
    }

    @Override
    protected ItemStack getItemInSlot(GrillTableBlockEntity blockEntity, int slot) {
        return blockEntity.getItem(slot);
    }

    @Override
    protected void buildTooltipLayout(ITooltip tooltip, BlockAccessor accessor, ListTag itemsList) {
        List<IElement> topRow = new ArrayList<>();
        List<IElement> bottomRow = new ArrayList<>();

        for (int i = 0; i < itemsList.size(); i++) {
            CompoundTag itemTag = itemsList.getCompound(i);
            int slot = itemTag.getInt("Slot");

            IElement element = createItemElement(accessor, itemTag);
            if (element == null) {
                continue;
            }

            if (slot < 4) {
                topRow.add(element);
            } else {
                bottomRow.add(element);
            }
        }

        appendRowToTooltip(tooltip, topRow);
        appendRowToTooltip(tooltip, bottomRow);
    }
}
