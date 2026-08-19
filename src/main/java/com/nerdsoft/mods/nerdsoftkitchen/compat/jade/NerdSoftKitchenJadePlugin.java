package com.nerdsoft.mods.nerdsoftkitchen.compat.jade;

import com.nerdsoft.mods.nerdsoftkitchen.block.GrillTableBlock;
import com.nerdsoft.mods.nerdsoftkitchen.block.SkilletBlock;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.GrillTableBlockEntity;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.SkilletBlockEntity;
import com.nerdsoft.mods.nerdsoftkitchen.compat.jade.extension.GrillTableItemStorageExtension;
import com.nerdsoft.mods.nerdsoftkitchen.compat.jade.extension.SkilletItemStorageExtension;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.JadeIds;

@WailaPlugin
public class NerdSoftKitchenJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(GrillTableItemStorageExtension.INSTANCE, GrillTableBlockEntity.class);
        registration.registerBlockDataProvider(SkilletItemStorageExtension.INSTANCE, SkilletBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(GrillTableItemStorageExtension.INSTANCE, GrillTableBlock.class);
        registration.registerBlockComponent(SkilletItemStorageExtension.INSTANCE, SkilletBlock.class);

        registration.addTooltipCollectedCallback((boxElement, accessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                if (blockAccessor.getBlock() instanceof GrillTableBlock || blockAccessor.getBlock() instanceof SkilletBlock) {
                    boxElement.getTooltip().remove(JadeIds.UNIVERSAL_ITEM_STORAGE);
                    boxElement.getTooltip().remove(JadeIds.UNIVERSAL_ITEM_STORAGE_DEFAULT);
                }
            }
        });
    }
}