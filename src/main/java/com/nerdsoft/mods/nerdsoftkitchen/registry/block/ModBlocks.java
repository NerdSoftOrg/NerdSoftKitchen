package com.nerdsoft.mods.nerdsoftkitchen.registry.block;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.block.*;
import com.nerdsoft.mods.nerdsoftkitchen.crop.*;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Collection;
import java.util.function.Supplier;

@SuppressWarnings("SameParameterValue")
public final class ModBlocks {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(NerdSoftKitchen.MOD_ID);

    public static Collection<DeferredHolder<Block, ? extends Block>> getBlocks() {
        return BLOCKS.getEntries();
    }

    private static final BlockBehaviour.Properties WILD_CROP_PROPERTIES = wildCropProps();
    private static final BlockBehaviour.Properties WILD_RICE_PROPERTIES = wildCropProps()
            .sound(SoundType.WET_GRASS)
            .noOcclusion();

    //? if <1.21.2 {
    public static final DeferredBlock<GrillTableBlock> GRILL_TABLE = BLOCKS.register("grill_table",
            () -> new GrillTableBlock(false, grillProps(15)));
    public static final DeferredBlock<GrillTableBlock> GRILL_TABLE_SOUL = BLOCKS.register("grill_table_soul",
            () -> new GrillTableBlock(true, grillProps(10)));
    public static final DeferredBlock<GrillTableBlock> GRILL_TABLE_UNLIT = BLOCKS.register("grill_table_unlit",
            () -> new GrillTableBlock(false, grillProps(0)));
    public static final DeferredBlock<GrillTableBlock> GRILL_TABLE_SOUL_UNLIT = BLOCKS.register("grill_table_soul_unlit",
            () -> new GrillTableBlock(true, grillProps(0)));
        public static final DeferredBlock<CuttingBoardBlock> CUTTING_BOARD = BLOCKS.register("cutting_board",
                () -> new CuttingBoardBlock(BlockBehaviour.Properties.of()
                        .mapColor(MapColor.WOOD)
                        .instrument(NoteBlockInstrument.BASS)
                        .strength(2.5F)
                        .sound(SoundType.WOOD)
                        .noOcclusion()));
    public static final DeferredBlock<FertileFarmlandBlock> FERTILE_FARMLAND = BLOCKS.register("fertile_farmland",
            () -> new FertileFarmlandBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
    public static final DeferredBlock<OrganicSoilBlock> ORGANIC_SOIL = BLOCKS.register("organic_soil",
            () -> new OrganicSoilBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)
                    .mapColor(MapColor.PODZOL)
                    .strength(0.5F)
                    .sound(SoundType.MUD)));
    public static final DeferredBlock<SkilletBlock> SKILLET = BLOCKS.register("skillet",
            () -> new SkilletBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(2.5F)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(SkilletBlock.LIT) ? 6 : 0)
                    .ignitedByLava()));
    public static final DeferredBlock<WildCropBlock> WILD_STRAWBERRY = registerWildCrop("wild_strawberry");
    public static final DeferredBlock<WildCropBlock> WILD_TOMATO = registerWildCrop("wild_tomato");
    public static final DeferredBlock<WildCropBlock> WILD_LETTUCE = registerWildCrop("wild_lettuce");
    public static final DeferredBlock<WildCropBlock> WILD_PURPLE_ONION = registerWildCrop("wild_purple_onion");
    public static final DeferredBlock<WildRiceBlock> WILD_RICE = BLOCKS.registerBlock("wild_rice",
            WildRiceBlock::new, WILD_RICE_PROPERTIES);
    //?} else {

    /*public static final DeferredBlock<GrillTableBlock> GRILL_TABLE = BLOCKS.registerBlock("grill_table",
            props -> new GrillTableBlock(false, props), grillProps(15));

    public static final DeferredBlock<GrillTableBlock> GRILL_TABLE_SOUL = BLOCKS.registerBlock("grill_table_soul",
            props -> new GrillTableBlock(true, props), grillProps(10));

    public static final DeferredBlock<GrillTableBlock> GRILL_TABLE_UNLIT = BLOCKS.registerBlock("grill_table_unlit",
            props -> new GrillTableBlock(false, props), grillProps(0));

    public static final DeferredBlock<GrillTableBlock> GRILL_TABLE_SOUL_UNLIT = BLOCKS.registerBlock("grill_table_soul_unlit",
            props -> new GrillTableBlock(true, props), grillProps(0));

    public static final DeferredBlock<CuttingBoardBlock> CUTTING_BOARD = BLOCKS.registerBlock("cutting_board",
            CuttingBoardBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WOOD)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.5F)
                    .sound(SoundType.WOOD)
                    .noOcclusion());

    public static final DeferredBlock<FertileFarmlandBlock> FERTILE_FARMLAND = BLOCKS.registerBlock("fertile_farmland",
            FertileFarmlandBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND));

    public static final DeferredBlock<OrganicSoilBlock> ORGANIC_SOIL = BLOCKS.registerBlock("organic_soil",
            OrganicSoilBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.DIRT)
                    .mapColor(MapColor.PODZOL)
                    .strength(0.5F)
                    .sound(SoundType.MUD));

    public static final DeferredBlock<SkilletBlock> SKILLET = BLOCKS.registerBlock("skillet",
            SkilletBlock::new, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(2.5F)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(SkilletBlock.LIT) ? 6 : 0)
                    .ignitedByLava());

    public static final DeferredBlock<WildCropBlock> WILD_STRAWBERRY = registerWildCrop("wild_strawberry");
    public static final DeferredBlock<WildCropBlock> WILD_TOMATO = registerWildCrop("wild_tomato");
    public static final DeferredBlock<WildCropBlock> WILD_LETTUCE = registerWildCrop("wild_lettuce");
    public static final DeferredBlock<WildCropBlock> WILD_PURPLE_ONION = registerWildCrop("wild_purple_onion");
    public static final DeferredBlock<WildRiceBlock> WILD_RICE = BLOCKS.registerBlock("wild_rice",
            WildRiceBlock::new, WILD_RICE_PROPERTIES);
    *///?}

    private static final BlockBehaviour.Properties CROP_PROPERTIES = cropProps();
    public static final DeferredBlock<ModCropBlock.Ages> STRAWBERRY_CROP = registerCrop3("strawberry_crop", ModItems.STRAWBERRY_SEEDS);
    public static final DeferredBlock<ModCropBlock.Ages> LETTUCE_CROP = registerCrop3("lettuce_crop", ModItems.LETTUCE_SEEDS);
    public static final DeferredBlock<ModCropBlock.Ages> PURPLE_ONION_CROP = registerCrop3("purple_onion_crop", ModItems.PURPLE_ONION_SEEDS);
    public static final DeferredBlock<RiceCropBlock> RICE_CROP = BLOCKS.registerBlock("rice_crop",
            props -> new RiceCropBlock(props, ModCropBlock.SHAPES_AGE_3, ModItems.RICE_SEEDS), CROP_PROPERTIES);
    public static final DeferredBlock<TomatoCropPoleBlock> TOMATO_CROP_POLE = registerTomatoCropPole("tomato_crop_pole", ModItems.TOMATO_SEEDS, ModItems.TOMATO);
    public static final DeferredBlock<TomatoCropBlock> TOMATO_CROP = registerTomatoCrop("tomato_crop", ModItems.TOMATO_SEEDS, TOMATO_CROP_POLE);

    private ModBlocks() {
    }

    private static BlockBehaviour.Properties wildCropProps() {
        return BlockBehaviour.Properties.of()
                .noCollission()
                .instabreak()
                .sound(SoundType.CROP)
                .pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties cropProps() {
        return wildCropProps().randomTicks();
    }

    private static BlockBehaviour.Properties grillProps(int lightLevel) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_GRAY)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .noOcclusion()
                .ignitedByLava()
                .lightLevel(state -> state.hasProperty(GrillTableBlock.LIT) && state.getValue(GrillTableBlock.LIT) ? lightLevel : 0);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        NerdSoftKitchenLogger.info("Blocks registered successfully.");
    }

    private static DeferredBlock<WildCropBlock> registerWildCrop(String name) {
        return BLOCKS.registerBlock(name, props -> new WildCropBlock(props, WildCropBlock.DEFAULT_WILD_SHAPE), WILD_CROP_PROPERTIES);
    }

    private static DeferredBlock<ModCropBlock.Ages> registerCrop3(String name, Supplier<? extends ItemLike> seedSupplier) {
        return BLOCKS.registerBlock(name, props -> new ModCropBlock.Ages(props, 3, ModCropBlock.SHAPES_AGE_3, seedSupplier), CROP_PROPERTIES);
    }

    private static DeferredBlock<TomatoCropBlock> registerTomatoCrop(String name, Supplier<? extends ItemLike> seedSupplier,
                                                                     Supplier<? extends TomatoCropPoleBlock> poleBlockSupplier) {
        return BLOCKS.registerBlock(name, props -> new TomatoCropBlock(props, ModCropBlock.SHAPES_AGE_5, seedSupplier, poleBlockSupplier), CROP_PROPERTIES);
    }

    private static DeferredBlock<TomatoCropPoleBlock> registerTomatoCropPole(String name, Supplier<? extends ItemLike> seedSupplier,
                                                                             Supplier<? extends ItemLike> harvestItemSupplier) {
        return BLOCKS.registerBlock(name, props -> new TomatoCropPoleBlock(props, ModCropBlock.SHAPES_AGE_5, seedSupplier, harvestItemSupplier), CROP_PROPERTIES);
    }
}
