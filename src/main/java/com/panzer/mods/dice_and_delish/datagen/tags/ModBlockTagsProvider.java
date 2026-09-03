package com.panzer.mods.dice_and_delish.datagen.tags;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.block.GrillTableBlock;
import com.panzer.mods.dice_and_delish.crop.HarvestableCropBlock;
import com.panzer.mods.dice_and_delish.crop.ModCropBlock;
import com.panzer.mods.dice_and_delish.crop.TallPlantBlock;
import com.panzer.mods.dice_and_delish.crop.TriplePlantBlock;
import com.panzer.mods.dice_and_delish.crop.WildCropBlock;
import com.panzer.mods.dice_and_delish.registry.tags.ModBlockTags;
import com.panzer.mods.dice_and_delish.registry.block.ModBlocks;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, DiceAndDelish.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

        Block[] grillTables = filterBlocks(GrillTableBlock.class);
        Block[] modCrops = filterBlocks(ModCropBlock.class);
        Block[] wildCrops = filterBlocks(WildCropBlock.class);
        Block[] tallPlants = filterBlocks(TallPlantBlock.class);
        Block[] triplePlants = filterBlocks(TriplePlantBlock.class);
        Block[] harvestableCrops = filterBlocks(HarvestableCropBlock.class);

        tag(ModBlockTags.CROPS).add(modCrops);
        tag(ModBlockTags.WILD_CROPS).add(wildCrops);
        tag(ModBlockTags.TALL_PLANTS).add(tallPlants);
        tag(ModBlockTags.TRIPLE_PLANTS).add(triplePlants);
        tag(ModBlockTags.HARVESTABLE_CROPS).add(harvestableCrops);

        tag(ModBlockTags.C_GRILL_TABLES).add(grillTables);
        tag(ModBlockTags.C_WILD_CROPS).addTag(ModBlockTags.WILD_CROPS);
        tag(ModBlockTags.C_TALL_PLANTS).addTag(ModBlockTags.TALL_PLANTS);
        tag(ModBlockTags.C_TRIPLE_PLANTS).addTag(ModBlockTags.TRIPLE_PLANTS);
        tag(ModBlockTags.C_HARVESTABLE_CROPS).addTag(ModBlockTags.HARVESTABLE_CROPS);

        tag(BlockTags.CROPS)
                .addTag(ModBlockTags.CROPS)
                .addTag(ModBlockTags.WILD_CROPS)
                .addTag(ModBlockTags.HARVESTABLE_CROPS);

        tag(ModBlockTags.C_CROPS).addTag(BlockTags.CROPS);

        tag(ModBlockTags.HEAT_SOURCES)
                .add(
                        Blocks.MAGMA_BLOCK,
                        Blocks.LAVA_CAULDRON
                )
                .addTag(BlockTags.CAMPFIRES)
                .addTag(BlockTags.FIRE)
                .addTag(ModBlockTags.C_GRILL_TABLES);

        tag(ModBlockTags.C_HEAT_SOURCES)
                .addTag(ModBlockTags.HEAT_SOURCES);

        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                ModBlocks.ORGANIC_SOIL.get(),
                ModBlocks.FERTILE_FARMLAND.get()
        );

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.CUTTING_BOARD.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.SKILLET.get())
                .addTag(ModBlockTags.C_GRILL_TABLES);
    }

    private <T extends Block> Block[] filterBlocks(Class<T> blockClass) {
        return ModBlocks.getBlocks().stream()
                .map(DeferredHolder::get)
                .filter(blockClass::isInstance)
                .toArray(Block[]::new);
    }
}
