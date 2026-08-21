package com.nerdsoft.mods.nerdsoftkitchen.compat.jade.extension;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.SkilletBlockEntity;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.ui.IElement;

import java.util.ArrayList;
import java.util.List;

public class SkilletItemStorageExtension extends BaseCookingItemExtension<SkilletBlockEntity> {

    public static final SkilletItemStorageExtension INSTANCE = new SkilletItemStorageExtension();

    private SkilletItemStorageExtension() {
        super(
                ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "skillet_progress"),
                SkilletBlockEntity.class,
                "SkilletItems"
        );
    }

    @Override
    protected int getSlotCount(SkilletBlockEntity blockEntity) {
        return SkilletBlockEntity.PAN_SLOTS_COUNT;
    }

    @Override
    protected int getCookTime(SkilletBlockEntity blockEntity, int slot) {
        return blockEntity.getCookTime(slot);
    }

    @Override
    protected int getCookProgress(SkilletBlockEntity blockEntity, int slot) {
        return blockEntity.getCookProgress(slot);
    }

    @Override
    protected ItemStack getItemInSlot(SkilletBlockEntity blockEntity, int slot) {
        return blockEntity.getItem(slot);
    }

    @Override
    protected void buildTooltipLayout(ITooltip tooltip, BlockAccessor accessor, ListTag itemsList) {
        List<IElement> row = new ArrayList<>();
        for (int i = 0; i < itemsList.size(); i++) {
            IElement element = createItemElement(accessor, itemsList.getCompound(i));
            if (element != null) {
                row.add(element);
            }
        }
        appendRowToTooltip(tooltip, row);
    }
}
