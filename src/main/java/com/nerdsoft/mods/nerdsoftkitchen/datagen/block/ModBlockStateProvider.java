package com.nerdsoft.mods.nerdsoftkitchen.datagen.block;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.block.FertileFarmlandBlock;
import com.nerdsoft.mods.nerdsoftkitchen.crop.TomatoCropPoleBlock;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.util.DatagenUtils;
import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

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
        grillTable(ModBlocks.GRILL_TABLE, "grill_table_lit", "grill_table_unlit", false);
        grillTable(ModBlocks.GRILL_TABLE_SOUL, "grill_table_soul_lit", null, true);

        manualYRotatedBlock(ModBlocks.CUTTING_BOARD);

        wildCropTintable(ModBlocks.WILD_PURPLE_ONION);

        manualBlock(ModBlocks.WILD_TOMATO);
        manualBlock(ModBlocks.WILD_LETTUCE);
        manualBlock(ModBlocks.WILD_STRAWBERRY);

        manualStagedCrop(ModBlocks.STRAWBERRY_CROP, BlockStateProperties.AGE_3, AGE_3);

        standardCrop(ModBlocks.PURPLE_ONION_CROP);

        crossCrop(ModBlocks.TOMATO_CROP, BlockStateProperties.AGE_4, VISUAL_AGE_5);
        tomatoCropPole(ModBlocks.TOMATO_CROP_POLE, BlockStateProperties.AGE_5, VISUAL_AGE_6, POLE_THRESHOLD_2);

        hybridCrop(ModBlocks.LETTUCE_CROP, 2);

        fertileFarmland(ModBlocks.FERTILE_FARMLAND);

        manualBlock(ModBlocks.ORGANIC_SOIL);
    }

    private ModelFile getExistingBlockModel(String name) {
        String path = "block/" + name;
        DatagenUtils.trackModel(this.models().existingFileHelper, path);
        return models().getExistingFile(modLoc(path));
    }

    // Manual Blocks
    private void manualBlock(DeferredBlock<?> block) {
        manualBlock(block, block.getId().getPath());
    }

    private void manualBlock(DeferredBlock<?> block, String name) {
        simpleBlock(block.get(), getExistingBlockModel(name));
    }

    private void manualYRotatedBlock(DeferredBlock<?> block) {
        manualYRotatedBlock(block, block.getId().getPath());
    }

    private void manualYRotatedBlock(DeferredBlock<?> block, String name) {
        horizontalBlock(block.get(), getExistingBlockModel(name));
    }

    // Crops
    private void standardCrop(DeferredBlock<?> block) {
        standardCrop(block, block.getId().getPath(), BlockStateProperties.AGE_3, AGE_3);
    }

    private void standardCrop(DeferredBlock<?> block, String name, IntegerProperty ageProperty, int visualStages) {
        setupCrop(block, name, ageProperty, visualStages, (stage, texture) ->
                models().crop(name + "_stage" + stage, texture).renderType("minecraft:cutout"));
    }

    private void crossCrop(DeferredBlock<?> block, IntegerProperty ageProperty, int visualStages) {
        crossCrop(block, block.getId().getPath(), ageProperty, visualStages);
    }

    private void crossCrop(DeferredBlock<?> block, String name, IntegerProperty ageProperty, int visualStages) {
        setupCrop(block, name, ageProperty, visualStages, (stage, texture) ->
                models().cross(name + "_stage" + stage, texture).renderType("minecraft:cutout"));
    }

    private void hybridCrop(DeferredBlock<?> block, int manualStageThreshold) {
        hybridCrop(block, block.getId().getPath(), manualStageThreshold);
    }

    private void hybridCrop(DeferredBlock<?> block, String name, int manualStageThreshold) {
        setupCrop(block, name, BlockStateProperties.AGE_3, AGE_3, (stage, texture) -> {
            if (stage >= manualStageThreshold) {
                return getExistingBlockModel(name + "_stage" + stage);
            }
            return models().cross(name + "_stage" + stage, texture).renderType("minecraft:cutout");
        });
    }

    private void manualStagedCrop(DeferredBlock<?> block, IntegerProperty ageProperty, int visualStages) {
        manualStagedCrop(block, block.getId().getPath(), ageProperty, visualStages);
    }

    private void manualStagedCrop(DeferredBlock<?> block, String name, IntegerProperty ageProperty, int visualStages) {
        setupCrop(block, name, ageProperty, visualStages, (stage, texture) -> getExistingBlockModel(name + "_stage" + stage));
    }

    private void setupCrop(DeferredBlock<?> block, String name, IntegerProperty ageProperty, int visualStages, BiFunction<Integer, ResourceLocation, ModelFile> modelProvider) {
        int maxAge = ageProperty.getPossibleValues().size() - 1;
        ModelFile[] stageModels = new ModelFile[visualStages];

        for (int stage = 0; stage < visualStages; stage++) {
            ResourceLocation texturePath = ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "block/" + name + "_stage" + stage);
            DatagenUtils.trackTexture(this.models().existingFileHelper, "block/" + name + "_stage" + stage);
            stageModels[stage] = modelProvider.apply(stage, texturePath);
        }

        getVariantBuilder(block.get()).forAllStates(state -> {
            int age = state.getValue(ageProperty);
            int stage = Math.min(visualStages - 1, (age * visualStages) / Math.max(1, maxAge));

            return ConfiguredModel.builder()
                    .modelFile(stageModels[stage])
                    .build();
        });
    }

    private void wildCropTintable(DeferredBlock<?> block) {
        wildCropTintable(block, block.getId().getPath());
    }

    private void wildCropTintable(DeferredBlock<?> block, String name) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get());
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

    private void tomatoCropPole(DeferredBlock<?> block, IntegerProperty ageProperty, int visualStages, int poleThreshold) {
        tomatoCropPole(block, block.getId().getPath(), ageProperty, visualStages, poleThreshold);
    }

    private void tomatoCropPole(DeferredBlock<?> block, String baseName, IntegerProperty ageProperty, int visualStages, int poleThreshold) {
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block.get());

        ModelFile poleLowerModel = getExistingBlockModel("crop_pole_lower_template");
        ModelFile poleUpperModel = getExistingBlockModel("crop_pole_upper_template");

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

    private void fertileFarmland(DeferredBlock<?> block) {
        String name = block.getId().getPath();
        ModelFile[] fertilityModels = new ModelFile[FertileFarmlandBlock.MAX_FERTILITY + 1];

        for (int fertility = 0; fertility <= FertileFarmlandBlock.MAX_FERTILITY; fertility++) {
            String topTexturePath = "block/" + name + "_top_" + fertility;
            DatagenUtils.trackTexture(this.models().existingFileHelper, topTexturePath);

            fertilityModels[fertility] = models()
                    .withExistingParent(name + "_" + fertility, mcLoc("block/template_farmland"))
                    .texture("dirt", mcLoc("block/dirt"))
                    .texture("top", modLoc(topTexturePath))
                    .renderType("minecraft:cutout");
        }

        getVariantBuilder(block.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(fertilityModels[state.getValue(FertileFarmlandBlock.FERTILITY)])
                .build());
    }

    private void grillTable(DeferredBlock<?> block, String litName, String unlitName, boolean ignoreLitProp) {
        ModelFile litModel = getExistingBlockModel(litName);
        ModelFile unlitModel = (unlitName != null) ? getExistingBlockModel(unlitName) : litModel;

        var builder = getVariantBuilder(block.get());
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