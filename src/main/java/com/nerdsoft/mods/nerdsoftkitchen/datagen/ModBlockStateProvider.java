package com.nerdsoft.mods.nerdsoftkitchen.datagen;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.crop.TomatoCropPoleBlock;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.BiFunction;

public class ModBlockStateProvider extends BlockStateProvider {

    private static final int AGE_3 = 4;
    private static final int VISUAL_AGE_5 = 5;
    private static final int VISUAL_AGE_6 = 6;
    private static final int POLE_THRESHOLD_2 = 2;

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, NerdSoftKitchen.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        grillTable(ModBlocks.GRILL_TABLE.get(), "grill_table_lit", "grill_table_unlit", false);
        grillTable(ModBlocks.GRILL_TABLE_SOUL.get(), "grill_table_soul_lit", null, true);

        wildCrop(ModBlocks.WILD_PURPLE_ONION.get(), "wild_purple_onion");

        manualCrop(ModBlocks.WILD_TOMATO.get(), "wild_tomato");
        manualCrop(ModBlocks.WILD_LETTUCE.get(), "wild_lettuce");
        manualCrop(ModBlocks.WILD_STRAWBERRY.get(), "wild_strawberry");

        manualStagedCrop(ModBlocks.STRAWBERRY_CROP.get(), "strawberry_crop", BlockStateProperties.AGE_3, AGE_3);

        standardCrop(ModBlocks.PURPLE_ONION_CROP.get(), "purple_onion_crop");

        crossCrop(ModBlocks.TOMATO_CROP.get(), "tomato_crop", BlockStateProperties.AGE_4, VISUAL_AGE_5);
        tomatoCropPole(ModBlocks.TOMATO_CROP_POLE.get(), "tomato_crop_pole", BlockStateProperties.AGE_5, VISUAL_AGE_6, POLE_THRESHOLD_2);

        hybridCrop(ModBlocks.LETTUCE_CROP.get(), "lettuce_crop", 2);
    }

    private void standardCrop(Block block, String name) {
        standardCrop(block, name, BlockStateProperties.AGE_3, AGE_3);
    }

    private void standardCrop(Block block, String name, IntegerProperty ageProperty, int visualStages) {
        setupCrop(block, name, ageProperty, visualStages, (stage, texture) ->
                models().crop(name + "_stage" + stage, texture).renderType("minecraft:cutout"));
    }

    private void crossCrop(Block block, String name, IntegerProperty ageProperty, int visualStages) {
        setupCrop(block, name, ageProperty, visualStages, (stage, texture) ->
                models().cross(name + "_stage" + stage, texture).renderType("minecraft:cutout"));
    }

    private void hybridCrop(Block block, String name, int manualStageThreshold) {
        setupCrop(block, name, BlockStateProperties.AGE_3, AGE_3, (stage, texture) -> {
            if (stage >= manualStageThreshold) {
                return models().getExistingFile(modLoc("block/" + name + "_stage" + stage));
            }
            return models().cross(name + "_stage" + stage, texture).renderType("minecraft:cutout");
        });
    }

    private void manualStagedCrop(Block block, String name, IntegerProperty ageProperty, int visualStages) {
        setupCrop(block, name, ageProperty, visualStages, (stage, texture) ->
                models().getExistingFile(modLoc("block/" + name + "_stage" + stage)));
    }

    private void tomatoCropPole(Block block, String baseName, IntegerProperty ageProperty, int visualStages, int poleThreshold) {
        getVariantBuilder(block).forAllStates(state -> {
            int age = state.getValue(ageProperty);
            var half = state.getValue(TomatoCropPoleBlock.HALF);

            int stage = Math.min(visualStages - 1, (age * visualStages) / Math.max(1, ageProperty.getPossibleValues().size() - 1));

            if (half == DoubleBlockHalf.UPPER) {
                if (stage < poleThreshold) {
                    return ConfiguredModel.builder().modelFile(models().getExistingFile(mcLoc("block/air"))).build();
                }

                String modelName = baseName + "_upper_stage" + stage;

                ModelFile model = models().withExistingParent(modelName, modLoc("block/crop_pole_upper_template"))
                        .texture("texture", modLoc("block/" + modelName))
                        .renderType("minecraft:cutout");

                return ConfiguredModel.builder().modelFile(model).build();
            }

            if (stage < poleThreshold) {
                String modelName = baseName + "_upper_stage" + stage;

                ModelFile model = models().withExistingParent(modelName, modLoc("block/crop_pole_upper_template"))
                        .texture("texture", modLoc("block/" + modelName))
                        .renderType("minecraft:cutout");

                return ConfiguredModel.builder().modelFile(model).build();
            }

            String modelName = baseName + "_lower_stage" + stage;

            ModelFile model = models().withExistingParent(modelName, modLoc("block/crop_pole_lower_template"))
                    .texture("texture", modLoc("block/" + modelName))
                    .renderType("minecraft:cutout");

            return ConfiguredModel.builder().modelFile(model).build();
        });
    }

    private void wildCrop(Block block, String name) {
        simpleBlock(block, models().cross(name, modLoc("block/" + name)).renderType("minecraft:cutout"));
    }

    private void manualCrop(Block block, String name) {
        simpleBlock(block, models().getExistingFile(modLoc("block/" + name)));
    }

    private void setupCrop(Block block, String name, IntegerProperty ageProperty, int visualStages, BiFunction<Integer, ResourceLocation, ModelFile> modelProvider) {
        int maxAge = ageProperty.getPossibleValues().size() - 1;
        ModelFile[] stageModels = new ModelFile[visualStages];

        for (int stage = 0; stage < visualStages; stage++) {
            ResourceLocation texturePath = ResourceLocation.fromNamespaceAndPath(
                    NerdSoftKitchen.MOD_ID, "block/" + name + "_stage" + stage
            );
            stageModels[stage] = modelProvider.apply(stage, texturePath);
        }

        getVariantBuilder(block).forAllStates(state -> {
            int age = state.getValue(ageProperty);
            int stage = Math.min(visualStages - 1, (age * visualStages) / Math.max(1, maxAge));

            return ConfiguredModel.builder()
                    .modelFile(stageModels[stage])
                    .build();
        });
    }

    private void grillTable(Block block, String litName, String unlitName, boolean ignoreLitProp) {
        ModelFile litModel = models().getExistingFile(modLoc("block/" + litName));
        ModelFile unlitModel = unlitName != null ? models().getExistingFile(modLoc("block/" + unlitName)) : litModel;

        var builder = getVariantBuilder(block);
        var ignoredProps = ignoreLitProp
                ? new Property<?>[]{BlockStateProperties.WATERLOGGED, BlockStateProperties.LIT}
                : new Property<?>[]{BlockStateProperties.WATERLOGGED};

        builder.forAllStatesExcept(state -> {
            boolean lit = !ignoreLitProp && state.getValue(BlockStateProperties.LIT);
            int yRot = ((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 360) % 360;

            return ConfiguredModel.builder()
                    .modelFile(lit ? litModel : unlitModel)
                    .rotationY(yRot)
                    .build();
        }, ignoredProps);
    }
}