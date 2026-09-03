package com.panzer.mods.dice_and_delish.registry.data;

import com.panzer.mods.dice_and_delish.registry.blockentity.ModBlockEntities;
import com.panzer.mods.dice_and_delish.util.ModLogger;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;

public final class ModCapabilities {

    private ModCapabilities() {
    }

    public static void register(IEventBus eventBus) {
        eventBus.addListener(ModCapabilities::registerCapabilities);
        ModLogger.info("Capabilities registered successfully.");
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.GRILL_TABLE.get(),
                (blockEntity, side) -> side == null
                        ? new InvWrapper(blockEntity)
                        : new SidedInvWrapper(blockEntity, side)
        );
    }
}
