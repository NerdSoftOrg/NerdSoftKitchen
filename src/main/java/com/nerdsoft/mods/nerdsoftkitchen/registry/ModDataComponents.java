package com.nerdsoft.mods.nerdsoftkitchen.registry;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, NerdSoftKitchen.MOD_ID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<IronCupContent>> IRON_CUP_CONTENT =
            DATA_COMPONENTS.register("iron_cup_content",
                    () -> DataComponentType.<IronCupContent>builder().persistent(IronCupContent.CODEC).networkSynchronized(IronCupContent.STREAM_CODEC).build());

    private ModDataComponents() {
    }
}
