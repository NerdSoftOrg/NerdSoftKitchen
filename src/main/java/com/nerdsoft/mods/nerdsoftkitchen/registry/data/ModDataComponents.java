package com.nerdsoft.mods.nerdsoftkitchen.registry.data;

import com.mojang.serialization.Codec;
import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents {

    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, NerdSoftKitchen.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<IronCupContent>> IRON_CUP_CONTENT =
            DATA_COMPONENTS.register("iron_cup_content",
                    () -> DataComponentType.<IronCupContent>builder()
                            .persistent(IronCupContent.CODEC)
                            .networkSynchronized(IronCupContent.STREAM_CODEC)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> COOK_PROGRESS =
            DATA_COMPONENTS.register("cook_progress",
                    () -> DataComponentType.<Float>builder()
                            .persistent(Codec.FLOAT)
                            .networkSynchronized(ByteBufCodecs.FLOAT)
                            .build());

    private ModDataComponents() {
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
        NerdSoftKitchenLogger.info("Data Components registered successfully.");
    }
}