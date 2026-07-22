package com.nerdsoft.mods.nerdsoftkitchen.registry;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

@EventBusSubscriber(modid = NerdSoftKitchen.MOD_ID)
public final class ModCapabilities {

    private ModCapabilities() {
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.GRILL_TABLE.get(),
                (blockEntity, side) -> side == null
                        ? new InvWrapper(blockEntity)
                        : new SidedInvWrapper(blockEntity, side)
        );
    }
}