package com.nerdsoft.mods.nerdsoftkitchen.compat.jade;

import com.nerdsoft.mods.nerdsoftkitchen.blockentity.GrillTableBlockEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class NerdSoftKitchenJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerItemStorage(GrillTableItemStorageExtension.INSTANCE, GrillTableBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerItemStorageClient(GrillTableItemStorageExtension.INSTANCE);
    }
}