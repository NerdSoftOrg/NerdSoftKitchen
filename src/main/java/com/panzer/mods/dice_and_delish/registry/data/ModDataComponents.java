package com.panzer.mods.dice_and_delish.registry.data;

import com.mojang.serialization.Codec;
import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.item.component.IronCupContent;
import com.panzer.mods.dice_and_delish.item.component.SkilletHotState;
import com.panzer.mods.dice_and_delish.util.ModLogger;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents {

    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, DiceAndDelish.MOD_ID);

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

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SkilletHotState>> SKILLET_HOT_STATE =
            DATA_COMPONENTS.register("skillet_hot_state",
                    () -> DataComponentType.<SkilletHotState>builder()
                            .persistent(SkilletHotState.CODEC)
                            .networkSynchronized(SkilletHotState.STREAM_CODEC)
                            .build());

    private ModDataComponents() {
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
        ModLogger.debug("Data Components registered successfully.");
    }
}
