package com.nerdsoft.mods.nerdsoftkitchen.datagen.block;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.crop.TomatoCropPoleBlock;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.DatagenUtils;
import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
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
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.BiFunction;

@SuppressWarnings("SameParameterValue")
public class ModBlockStateProvider extends BlockStateProvider {

    private static final int AGE_3 = 4;
    private static final int VISUAL_AGE_5 = 5;
    private static final int VISUAL_AGE_6 = 6;
    private static final int POLE_THRESHOLD_2 = 2;

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NerdSoftKitchen.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        grillTable(ModBlocks.GRILL_TABLE.get(), "grill_table_lit", "grill_table_unlit", false);
        grillTable(ModBlocks.GRILL_TABLE_SOUL.get(), "grill_table_soul_lit", null, true);

        wildCropTintable(ModBlocks.WILD_PURPLE_ONION.get(), "wild_purple_onion");

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
                models()
                        .crop(name + "_stage" + stage, texture)
                        .renderType("minecraft:cutout"));
    }

    private void crossCrop(Block block, String name, IntegerProperty ageProperty, int visualStages) {
        setupCrop(block, name, ageProperty, visualStages, (stage, texture) ->
                models()
                        .cross(name + "_stage" + stage, texture)
                        .renderType("minecraft:cutout"));
    }

    private void hybridCrop(Block block, String name, int manualStageThreshold) {
        setupCrop(block, name, BlockStateProperties.AGE_3, AGE_3, (stage, texture) -> {
            if (stage >= manualStageThreshold) {
                DatagenUtils.trackModel(this.models().existingFileHelper, "block/" + name + "_stage" + stage);
                return models().getExistingFile(modLoc("block/" + name + "_stage" + stage));
            }
            return models()
                    .cross(name + "_stage" + stage, texture)
                    .renderType("minecraft:cutout");
        });
    }

    private void manualStagedCrop(Block block, String name, IntegerProperty ageProperty, int visualStages) {
        setupCrop(block, name, ageProperty, visualStages, (stage, texture) -> {
            String path = "block/" + name + "_stage" + stage;

            DatagenUtils.trackModel(this.models().existingFileHelper, path);

            return models().getExistingFile(modLoc(path));
        });
    }

    private void tomatoCropPole(Block block, String baseName, IntegerProperty ageProperty, int visualStages, int poleThreshold) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

        DatagenUtils.trackModel(this.models().existingFileHelper, "block/crop_pole_lower_template");
        DatagenUtils.trackModel(this.models().existingFileHelper, "block/crop_pole_upper_template");

        ModelFile poleLowerModel = models().getExistingFile(modLoc("block/crop_pole_lower_template"));
        ModelFile poleUpperModel = models().getExistingFile(modLoc("block/crop_pole_upper_template"));

        builder.part()
                .modelFile(poleLowerModel)
                .addModel()
                .condition(TomatoCropPoleBlock.HALF, DoubleBlockHalf.LOWER)
                .condition(ageProperty, getAgesForThreshold(ageProperty, visualStages, poleThreshold, true))
                .end();

        builder.part()
                .modelFile(poleUpperModel)
                .addModel()
                .condition(TomatoCropPoleBlock.HALF, DoubleBlockHalf.UPPER)
                .end();

        int maxAge = ageProperty.getPossibleValues().size() - 1;

        for (int age : ageProperty.getPossibleValues()) {
            int stage = Math.min(visualStages - 1, (age * visualStages) / Math.max(1, maxAge));

            for (DoubleBlockHalf half : DoubleBlockHalf.values()) {
                if (half == DoubleBlockHalf.UPPER && stage < poleThreshold) {
                    continue;
                }

                String cropTextureName = baseName + "_" + (half == DoubleBlockHalf.LOWER && stage >= poleThreshold ? "lower_" : "upper_") + "stage" + stage;
                DatagenUtils.trackTexture(this.models().existingFileHelper, "block/" + cropTextureName);

                ModelFile crossPlantModel = models()
                        .singleTexture(cropTextureName, mcLoc("block/cross"), "cross", modLoc("block/" + cropTextureName))
                        .renderType("minecraft:cutout");

                builder.part()
                        .modelFile(crossPlantModel)
                        .addModel()
                        .condition(ageProperty, age)
                        .condition(TomatoCropPoleBlock.HALF, half)
                        .end();
            }
        }
    }

    private Integer[] getAgesForThreshold(IntegerProperty ageProperty, int visualStages, int poleThreshold, boolean greaterOrEqual) {
        int maxAge = ageProperty.getPossibleValues().size() - 1;
        return ageProperty.getPossibleValues().stream()
                .filter(age -> {
                    int stage = Math.min(visualStages - 1, (age * visualStages) / Math.max(1, maxAge));
                    return greaterOrEqual == (stage >= poleThreshold);
                })
                .toArray(Integer[]::new);
    }

    private void wildCropTintable(Block block, String name) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);
        String tintPath = "block/" + name + "_tint";
        String overlayPath = "block/" + name + "_overlay";

        DatagenUtils.trackTexture(this.models().existingFileHelper, tintPath);
        DatagenUtils.trackTexture(this.models().existingFileHelper, overlayPath);

        ModelFile tintModel = models()
                .withExistingParent(name + "_tint", mcLoc("block/tinted_cross"))
                .texture("cross", modLoc(tintPath))
                .renderType("minecraft:cutout");

        ModelFile overlayModel = models()
                .cross(name + "_overlay", modLoc(overlayPath))
                .renderType("minecraft:cutout");

        builder.part().modelFile(tintModel).addModel().end();
        builder.part().modelFile(overlayModel).addModel().end();
    }

    @SuppressWarnings("unused")
    private void wildCrop(Block block, String name) {
        String texturePath = "block/" + name;
        DatagenUtils.trackTexture(this.models().existingFileHelper, texturePath);

        ModelFile model = models()
                .cross(name, modLoc(texturePath))
                .renderType("minecraft:cutout");

        simpleBlock(block, model);
    }

    private void manualCrop(Block block, String name) {
        DatagenUtils.trackModel(this.models().existingFileHelper, "block/" + name);

        String texturePath = "block/" + name;
        ModelFile model = models().getExistingFile(modLoc(texturePath));

        simpleBlock(block, model);
    }

    private void setupCrop(Block block, String name, IntegerProperty ageProperty, int visualStages, BiFunction<Integer, ResourceLocation, ModelFile> modelProvider) {
        int maxAge = ageProperty.getPossibleValues().size() - 1;
        ModelFile[] stageModels = new ModelFile[visualStages];

        for (int stage = 0; stage < visualStages; stage++) {
            ResourceLocation texturePath = ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "block/" + name + "_stage" + stage);
            DatagenUtils.trackTexture(this.models().existingFileHelper, "block/" + name + "_stage" + stage);

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
        DatagenUtils.trackModel(this.models().existingFileHelper, "block/" + litName);
        ModelFile litModel = models().getExistingFile(modLoc("block/" + litName));

        ModelFile unlitModel;
        if (unlitName != null) {
            DatagenUtils.trackModel(this.models().existingFileHelper, "block/" + unlitName);
            unlitModel = models().getExistingFile(modLoc("block/" + unlitName));
        } else {
            unlitModel = litModel;
        }

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
