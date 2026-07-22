package com.nerdsoft.mods.nerdsoftkitchen.registry;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.block.GrillTableBlock;
import com.nerdsoft.mods.nerdsoftkitchen.crop.ModCropBlock;
import com.nerdsoft.mods.nerdsoftkitchen.crop.TomatoCropBlock;
import com.nerdsoft.mods.nerdsoftkitchen.crop.TomatoCropPoleBlock;
import com.nerdsoft.mods.nerdsoftkitchen.crop.WildCropBlock;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModBlocks {

    /// Blocks
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(NerdSoftKitchen.MOD_ID);
    public static final DeferredBlock<GrillTableBlock> GRILL_TABLE = BLOCKS.register("grill_table", () -> new GrillTableBlock(false, BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).noOcclusion().lightLevel(state -> state.getValue(GrillTableBlock.LIT) ? 15 : 0).ignitedByLava()));
    public static final DeferredBlock<GrillTableBlock> GRILL_TABLE_SOUL = BLOCKS.register("grill_table_soul", () -> new GrillTableBlock(true, BlockBehaviour.Properties.of().mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).noOcclusion().lightLevel(state -> state.getValue(GrillTableBlock.LIT) ? 10 : 0).ignitedByLava()));

    /// Wild Crops
    private static final BlockBehaviour.Properties WILD_CROP_PROPERTIES = BlockBehaviour.Properties.of().noCollission().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY);

    public static final DeferredBlock<WildCropBlock> WILD_STRAWBERRY = registerWildCrop("wild_strawberry");
    public static final DeferredBlock<WildCropBlock> WILD_TOMATO = registerWildCrop("wild_tomato");
    public static final DeferredBlock<WildCropBlock> WILD_LETTUCE = registerWildCrop("wild_lettuce");
    public static final DeferredBlock<WildCropBlock> WILD_PURPLE_ONION = registerWildCrop("wild_purple_onion");

    /// Default Crops
    private static final BlockBehaviour.Properties CROP_PROPERTIES = BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY);

    // Seeds as Seed
    public static final DeferredBlock<ModCropBlock.Ages> STRAWBERRY_CROP = registerCrop3("strawberry_crop", ModItems.STRAWBERRY_SEEDS);
    public static final DeferredBlock<ModCropBlock.Ages> LETTUCE_CROP = registerCrop3("lettuce_crop", ModItems.LETTUCE_SEEDS);
    public static final DeferredBlock<ModCropBlock.Ages> PURPLE_ONION_CROP = registerCrop3("purple_onion_crop", ModItems.PURPLE_ONION_SEEDS);

    public static final DeferredBlock<TomatoCropPoleBlock> TOMATO_CROP_POLE = registerTomatoCropPole("tomato_crop_pole", ModItems.TOMATO_SEEDS, ModItems.TOMATO);
    public static final DeferredBlock<TomatoCropBlock> TOMATO_CROP = registerTomatoCrop("tomato_crop", ModItems.TOMATO_SEEDS, TOMATO_CROP_POLE);

    private ModBlocks() {
    }

    /// Register
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