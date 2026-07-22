package com.nerdsoft.mods.nerdsoftkitchen.registry;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.GrillTableBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, NerdSoftKitchen.MOD_ID);

    private ModBlockEntities() {
    }

    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GrillTableBlockEntity>> GRILL_TABLE =
            BLOCK_ENTITIES.register("grill_table", () -> BlockEntityType.Builder.of(GrillTableBlockEntity::new,
                    ModBlocks.GRILL_TABLE.get(), ModBlocks.GRILL_TABLE_SOUL.get()).build(null));

}