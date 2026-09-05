package com.panzer.mods.dice_and_delish.datagen.block;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.block.FertileFarmlandBlock;
import com.panzer.mods.dice_and_delish.crop.TomatoCropPoleBlock;
import com.panzer.mods.dice_and_delish.crop.TripleBlockHalf;
import com.panzer.mods.dice_and_delish.crop.TriplePlantBlock;
import com.panzer.mods.dice_and_delish.datagen.util.DatagenUtils;
import com.panzer.mods.dice_and_delish.registry.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
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
import java.util.function.Function;

@SuppressWarnings("SameParameterValue")
public class ModBlockStateProvider extends BlockStateProvider {

    private static final int AGE_3 = 4;
    private static final int VISUAL_AGE_5 = 5;
    private static final int VISUAL_AGE_6 = 6;
    private static final int POLE_THRESHOLD = 2;

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DiceAndDelish.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Blocks
        litModel(ModBlocks.GRILL_TABLE, "appliance/grill_table_lit", "appliance/grill_table_unlit", false);
        litModel(ModBlocks.GRILL_TABLE_SOUL, "appliance/grill_table_soul_lit", "appliance/grill_table_unlit", false);
        litModel(ModBlocks.GRILL_TABLE_UNLIT, "appliance/grill_table_lit", "appliance/grill_table_unlit", false);
        litModel(ModBlocks.GRILL_TABLE_SOUL_UNLIT, "appliance/grill_table_soul_lit", "appliance/grill_table_unlit", false);
        trackGrillTableLodModels();

        applianceLits(ModBlocks.SKILLET);
        applianceHorizontals(ModBlocks.CUTTING_BOARD);
        trackCuttingBoardLodModel();

        applianceSimple(ModBlocks.ORGANIC_SOIL);
        fertileFarmland(ModBlocks.FERTILE_FARMLAND);

        // Wild Crops
        wildCrops(ModBlocks.WILD_TOMATO, ModBlocks.WILD_LETTUCE, ModBlocks.WILD_STRAWBERRY);

        wildCropTintable(ModBlocks.WILD_PURPLE_ONION);
        wildCropMulti(ModBlocks.WILD_RICE, TriplePlantBlock.HALF, TripleBlockHalf.values());

        // Crops
        existingCrop(ModBlocks.STRAWBERRY_CROP, BlockStateProperties.AGE_3, AGE_3,
                st -> "crop/strawberry/strawberry_crop_stage" + st);

        mixedLettuceCrop();

        setupCrop(ModBlocks.PURPLE_ONION_CROP, BlockStateProperties.AGE_3, AGE_3, (st, tex) ->
                cropModel("purple_onion/purple_onion_crop_stage" + st, tex, false));

        setupCrop(ModBlocks.TOMATO_CROP, BlockStateProperties.AGE_4, VISUAL_AGE_5, (st, tex) ->
                cropModel("tomato/tomato_crop_stage" + st, tex, true));

        crossCropMulti(ModBlocks.RICE_CROP, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.values(),
                BlockStateProperties.AGE_3, AGE_3, null);

        tomatoCropPole(ModBlocks.TOMATO_CROP_POLE);
    }

    private ResourceLocation blockLoc(String relativePath) {
        return modLoc("block/" + relativePath);
    }

    private ModelFile getExistingModel(String relativePath) {
        DatagenUtils.trackModel(models().existingFileHelper, "block/" + relativePath);
        return models().getExistingFile(blockLoc(relativePath));
    }

    private void trackGrillTableLodModels() {
        getExistingModel("appliance/grill_table_lit_lod1");
        getExistingModel("appliance/grill_table_soul_lit_lod1");
        getExistingModel("appliance/grill_table_unlit_lod1");
    }

    private void trackCuttingBoardLodModel() {
        getExistingModel("appliance/cutting_board_lod1");
    }

    private ModelFile cropModel(String name, ResourceLocation tex, boolean cross) {
        String cropName = "crop/" + name;
        return (cross ? models().cross(cropName, tex) : models().crop(cropName, tex))
                .renderType("minecraft:cutout");
    }

    // block/appliance/

    private void applianceSimple(DeferredBlock<?> block) {
        String name = block.getId().getPath();
        DatagenUtils.trackTexture(models().existingFileHelper, "block/appliance/" + name);
        var model = models().cubeAll(name, blockLoc("appliance/" + name));
        simpleBlock(block.get(), model);
    }

    private void applianceHorizontals(DeferredBlock<?>... blocks) {
        for (DeferredBlock<?> block : blocks) {
            String path = "appliance/" + block.getId().getPath();
            horizontalBlock(block.get(), getExistingModel(path));
        }
    }

    private void applianceLits(DeferredBlock<?>... blocks) {
        for (DeferredBlock<?> block : blocks) {
            String basePath = "appliance/" + block.getId().getPath();
            litModel(block, basePath + "_lit", basePath + "_unlit", false);
        }
    }

    private void litModel(DeferredBlock<?> block, String litPath, String unlitPath, boolean ignoreLit) {
        ModelFile lit = getExistingModel(litPath);
        ModelFile unlit = unlitPath != null ? getExistingModel(unlitPath) : lit;

        Property<?>[] ignored = ignoreLit
                ? new Property<?>[]{BlockStateProperties.WATERLOGGED, BlockStateProperties.LIT}
                : new Property<?>[]{BlockStateProperties.WATERLOGGED};

        getVariantBuilder(block.get()).forAllStatesExcept(st -> {
            boolean isLit = !ignoreLit && st.getValue(BlockStateProperties.LIT);
            int yRot = ((int) st.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 360) % 360;
            return ConfiguredModel.builder().modelFile(isLit ? lit : unlit).rotationY(yRot).build();
        }, ignored);
    }

    private void fertileFarmland(DeferredBlock<?> block) {
        String name = block.getId().getPath();
        ModelFile[] models = new ModelFile[FertileFarmlandBlock.MAX_FERTILITY + 1];

        for (int i = 0; i <= FertileFarmlandBlock.MAX_FERTILITY; i++) {
            String topTexture = "appliance/" + (i > 0 ? "fertile_farmland_moist" : "fertile_farmland");
            DatagenUtils.trackTexture(models().existingFileHelper, "block/" + topTexture);

            models[i] = models().withExistingParent(name + "_" + i, mcLoc("block/template_farmland"))
                    .texture("dirt", mcLoc("minecraft:block/dirt"))
                    .texture("top", blockLoc(topTexture))
                    .renderType("minecraft:cutout");
        }

        getVariantBuilder(block.get()).forAllStates(st -> ConfiguredModel.builder()
                .modelFile(models[st.getValue(FertileFarmlandBlock.FERTILITY)]).build());
    }

    // block/wild/

    private void wildCrops(DeferredBlock<?>... blocks) {
        for (DeferredBlock<?> block : blocks) {
            simpleBlock(block.get(), getExistingModel("wild/" + block.getId().getPath()));
        }
    }

    private void wildCropTintable(DeferredBlock<?> block) {
        String name = block.getId().getPath();
        var builder = getMultipartBuilder(block.get());

        DatagenUtils.trackTexture(models().existingFileHelper, "block/wild/" + name + "_tint");
        DatagenUtils.trackTexture(models().existingFileHelper, "block/wild/" + name + "_overlay");

        ModelFile tintModel = models().withExistingParent("wild/" + name + "_tint", mcLoc("block/tinted_cross"))
                .texture("cross", blockLoc("wild/" + name + "_tint"))
                .renderType("minecraft:cutout");

        ModelFile overlayModel = models().cross("wild/" + name + "_overlay", blockLoc("wild/" + name + "_overlay"))
                .renderType("minecraft:cutout");

        builder.part().modelFile(tintModel).addModel().end();
        builder.part().modelFile(overlayModel).addModel().end();
    }

    private <E extends Enum<E> & StringRepresentable> void wildCropMulti(
            DeferredBlock<?> block, Property<E> halfProp, E[] values) {
        var builder = getMultipartBuilder(block.get());
        for (E half : values) {
            String texName = block.getId().getPath() + "_" + half.getSerializedName();
            DatagenUtils.trackTexture(models().existingFileHelper, "block/wild/" + texName);

            ModelFile model = models().singleTexture("wild/" + texName, mcLoc("block/cross"), "cross", blockLoc("wild/" + texName))
                    .renderType("minecraft:cutout");
            builder.part().modelFile(model).addModel().condition(halfProp, half).end();
        }
    }

    /**
     * Crop whose models are ALL hand-made. Only generates the blockstate mapping.
     */
    private void existingCrop(DeferredBlock<?> block, IntegerProperty ageProp, int visualStages,
                              Function<Integer, String> modelPath) {
        int maxAge = ageProp.getPossibleValues().size() - 1;
        ModelFile[] models = new ModelFile[visualStages];

        for (int i = 0; i < visualStages; i++) {
            models[i] = getExistingModel(modelPath.apply(i));
        }

        getVariantBuilder(block.get()).forAllStates(state -> {
            int stage = Math.min(visualStages - 1, (state.getValue(ageProp) * visualStages) / Math.max(1, maxAge));
            return ConfiguredModel.builder().modelFile(models[stage]).build();
        });
    }

    /**
     * Lettuce is mixed: stages 0-1 generated by datagen, stages 2-3 are hand-made.
     * If you later make stage0/1 by hand, replace this call with existingCrop().
     */
    private void mixedLettuceCrop() {
        DeferredBlock<?> block = ModBlocks.LETTUCE_CROP;
        IntegerProperty ageProp = BlockStateProperties.AGE_3;
        int visualStages = AGE_3; // 4
        int maxAge = ageProp.getPossibleValues().size() - 1;
        ModelFile[] models = new ModelFile[visualStages];

        // Generated stages 0-1
        for (int i = 0; i < 2; i++) {
            ResourceLocation tex = blockLoc("crop/lettuce/lettuce_crop_stage" + i);
            DatagenUtils.trackTexture(models().existingFileHelper, tex.getPath());
            models[i] = cropModel("lettuce/lettuce_crop_stage" + i, tex, true);
        }

        // Hand-made stages 2-3
        models[2] = getExistingModel("crop/lettuce/lettuce_crop_stage2");
        models[3] = getExistingModel("crop/lettuce/lettuce_crop_stage3");

        getVariantBuilder(block.get()).forAllStates(state -> {
            int stage = Math.min(visualStages - 1, (state.getValue(ageProp) * visualStages) / Math.max(1, maxAge));
            return ConfiguredModel.builder().modelFile(models[stage]).build();
        });
    }

    private void setupCrop(DeferredBlock<?> block, IntegerProperty ageProp, int visualStages,
                           BiFunction<Integer, ResourceLocation, ModelFile> provider) {
        String name = block.getId().getPath().replace("_crop", "");
        setupCrop(block, ageProp, visualStages, provider,
                st -> blockLoc("crop/" + name + "/" + name + "_crop_stage" + st));
    }

    private void setupCrop(DeferredBlock<?> block, IntegerProperty ageProp, int visualStages,
                           BiFunction<Integer, ResourceLocation, ModelFile> provider,
                           Function<Integer, ResourceLocation> textureMapper) {
        int maxAge = ageProp.getPossibleValues().size() - 1;
        ModelFile[] models = new ModelFile[visualStages];

        for (int i = 0; i < visualStages; i++) {
            ResourceLocation texLoc = textureMapper.apply(i);
            DatagenUtils.trackTexture(models().existingFileHelper, texLoc.getPath());
            models[i] = provider.apply(i, texLoc);
        }

        getVariantBuilder(block.get()).forAllStates(state -> {
            int stage = Math.min(visualStages - 1, (state.getValue(ageProp) * visualStages) / Math.max(1, maxAge));
            return ConfiguredModel.builder().modelFile(models[stage]).build();
        });
    }

    private <E extends Enum<E> & StringRepresentable> void crossCropMulti(
            DeferredBlock<?> block, Property<E> halfProp, E[] halfValues,
            IntegerProperty ageProp, int visualStages, BiFunction<Integer, E, Boolean> skip) {
        crossCropMulti(getMultipartBuilder(block.get()), block, halfProp, halfValues, ageProp, visualStages, skip);
    }

    private <E extends Enum<E> & StringRepresentable> void crossCropMulti(
            MultiPartBlockStateBuilder builder, DeferredBlock<?> block, Property<E> halfProp, E[] halfValues,
            IntegerProperty ageProp, int visualStages, BiFunction<Integer, E, Boolean> skip) {

        String base = block.getId().getPath().replace("_crop", "");
        int maxAge = ageProp.getPossibleValues().size() - 1;

        for (int age : ageProp.getPossibleValues()) {
            int stage = Math.min(visualStages - 1, (age * visualStages) / Math.max(1, maxAge));

            for (E half : halfValues) {
                if (skip != null && skip.apply(stage, half)) continue;

                String texName = base + "_crop_" + half.getSerializedName() + "_stage" + stage;
                String relativeTex = "crop/" + base + "/" + texName;
                DatagenUtils.trackTexture(models().existingFileHelper, "block/" + relativeTex);

                ModelFile model = models().singleTexture("crop/" + base + "/" + texName,
                                mcLoc("block/cross"), "cross", blockLoc(relativeTex))
                        .renderType("minecraft:cutout");
                builder.part().modelFile(model).addModel()
                        .condition(ageProp, age).condition(halfProp, half).end();
            }
        }
    }

    private void tomatoCropPole(DeferredBlock<?> block) {
        var builder = getMultipartBuilder(block.get());
        crossCropMulti(builder, block, TomatoCropPoleBlock.HALF, DoubleBlockHalf.values(),
                BlockStateProperties.AGE_5, VISUAL_AGE_6,
                (stage, half) -> half == DoubleBlockHalf.UPPER && stage < POLE_THRESHOLD);

        Integer[] validAges = BlockStateProperties.AGE_5.getPossibleValues().stream()
                .filter(age -> Math.min(VISUAL_AGE_6 - 1, (age * VISUAL_AGE_6) / 5) >= POLE_THRESHOLD)
                .toArray(Integer[]::new);

        builder.part().modelFile(getExistingModel("crop/pole_crop_lower_template")).addModel()
                .condition(TomatoCropPoleBlock.HALF, DoubleBlockHalf.LOWER)
                .condition(BlockStateProperties.AGE_5, validAges).end();

        builder.part().modelFile(getExistingModel("crop/pole_crop_upper_template")).addModel()
                .condition(TomatoCropPoleBlock.HALF, DoubleBlockHalf.UPPER)
                .condition(BlockStateProperties.AGE_5, validAges).end();
    }
}
