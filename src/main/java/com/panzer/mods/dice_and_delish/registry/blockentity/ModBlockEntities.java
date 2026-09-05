package com.panzer.mods.dice_and_delish.registry.blockentity;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.blockentity.CuttingBoardBlockEntity;
import com.panzer.mods.dice_and_delish.blockentity.GrillTableBlockEntity;
import com.panzer.mods.dice_and_delish.blockentity.OrganicSoilBlockEntity;
import com.panzer.mods.dice_and_delish.blockentity.SkilletBlockEntity;
import com.panzer.mods.dice_and_delish.registry.block.ModBlocks;
import com.panzer.mods.dice_and_delish.util.ModLogger;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

//? if >=1.21.2 {
/*import java.util.Set;
 *///?}

public final class ModBlockEntities {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, DiceAndDelish.MOD_ID);

    //? if <1.21.2 {
    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GrillTableBlockEntity>> GRILL_TABLE =
            BLOCK_ENTITIES.register("grill_table", () -> BlockEntityType.Builder.of(GrillTableBlockEntity::new,
                    ModBlocks.GRILL_TABLE.get(),
                    ModBlocks.GRILL_TABLE_SOUL.get(),
                    ModBlocks.GRILL_TABLE_UNLIT.get(),
                    ModBlocks.GRILL_TABLE_SOUL_UNLIT.get()).build(null));
    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CuttingBoardBlockEntity>> CUTTING_BOARD =
            BLOCK_ENTITIES.register("cutting_board", () -> BlockEntityType.Builder.of(CuttingBoardBlockEntity::new,
                    ModBlocks.CUTTING_BOARD.get()).build(null));
    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OrganicSoilBlockEntity>> ORGANIC_SOIL =
            BLOCK_ENTITIES.register("organic_soil", () -> BlockEntityType.Builder.of(OrganicSoilBlockEntity::new,
                    ModBlocks.ORGANIC_SOIL.get()).build(null));
    @SuppressWarnings("DataFlowIssue")
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SkilletBlockEntity>> SKILLET =
            BLOCK_ENTITIES.register("skillet", () -> BlockEntityType.Builder.of(SkilletBlockEntity::new,
                    ModBlocks.SKILLET.get()).build(null));
    //?} else {
    /*public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GrillTableBlockEntity>> GRILL_TABLE =
            BLOCK_ENTITIES.register("grill_table", () -> new BlockEntityType<>(
                    GrillTableBlockEntity::new,
                    Set.of(ModBlocks.GRILL_TABLE.get(), ModBlocks.GRILL_TABLE_SOUL.get(), ModBlocks.GRILL_TABLE_UNLIT.get(), ModBlocks.GRILL_TABLE_SOUL_UNLIT.get())
            ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CuttingBoardBlockEntity>> CUTTING_BOARD =
            BLOCK_ENTITIES.register("cutting_board", () -> new BlockEntityType<>(
                    CuttingBoardBlockEntity::new,
                    Set.of(ModBlocks.CUTTING_BOARD.get())
            ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OrganicSoilBlockEntity>> ORGANIC_SOIL =
            BLOCK_ENTITIES.register("organic_soil", () -> new BlockEntityType<>(
                    OrganicSoilBlockEntity::new,
                    Set.of(ModBlocks.ORGANIC_SOIL.get())
            ));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SkilletBlockEntity>> SKILLET =
            BLOCK_ENTITIES.register("skillet", () -> new BlockEntityType<>(
                    SkilletBlockEntity::new,
                    Set.of(ModBlocks.SKILLET.get())
            ));
    *///?}

    private ModBlockEntities() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
        ModLogger.debug("Block Entities registered successfully.");
    }
}
