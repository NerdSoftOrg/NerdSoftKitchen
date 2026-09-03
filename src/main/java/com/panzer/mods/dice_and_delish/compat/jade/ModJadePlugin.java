package com.panzer.mods.dice_and_delish.compat.jade;

import com.panzer.mods.dice_and_delish.block.GrillTableBlock;
import com.panzer.mods.dice_and_delish.block.SkilletBlock;
import com.panzer.mods.dice_and_delish.blockentity.GrillTableBlockEntity;
import com.panzer.mods.dice_and_delish.blockentity.SkilletBlockEntity;
import com.panzer.mods.dice_and_delish.compat.jade.extension.GrillTableItemStorageExtension;
import com.panzer.mods.dice_and_delish.compat.jade.extension.SkilletItemStorageExtension;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.JadeIds;

@WailaPlugin
public class ModJadePlugin implements IWailaPlugin {

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
