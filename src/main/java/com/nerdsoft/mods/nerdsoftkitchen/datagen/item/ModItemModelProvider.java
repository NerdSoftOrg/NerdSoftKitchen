package com.nerdsoft.mods.nerdsoftkitchen.datagen.item;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.util.DatagenUtils;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NerdSoftKitchen.MOD_ID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("InvariantValue")
    protected void registerModels() {
        // Block Items
        blockItemModel(ModItems.GRILL_TABLE, "grill_table_lit");
        blockItemModel(ModItems.GRILL_TABLE_SOUL, "grill_table_soul_lit");

        blockItemModel(ModItems.CUTTING_BOARD);
        blockItemModel(ModItems.ORGANIC_SOIL);
        blockItemModel(ModItems.FERTILE_FARMLAND, "fertile_farmland_3");
        blockItemModel(ModItems.SKILLET, "skillet_unlit");

        // 2D Items
        customItem2D(
                ModItems.WILD_PURPLE_ONION, ModItems.WILD_LETTUCE, ModItems.WILD_TOMATO, ModItems.WILD_STRAWBERRY,
                ModItems.STRAWBERRY_SEEDS, ModItems.TOMATO_SEEDS, ModItems.LETTUCE_SEEDS, ModItems.PURPLE_ONION_SEEDS,
                ModItems.STRAWBERRY, ModItems.TOMATO, ModItems.LETTUCE, ModItems.PURPLE_ONION,
                ModItems.RAW_CHICKEN_PIECES, ModItems.COOKED_CHICKEN_PIECES, ModItems.FRIED_EGG, ModItems.SALAD,
                ModItems.CHEESE, ModItems.CHEESE_SLICE, ModItems.GRILLED_CHEESE, ModItems.SCRAMBLED_EGGS,
                ModItems.ORGANIC_MIXTURE,
                ModItems.RAW_SANDWICH_BREAD, ModItems.TOASTED_SANDWICH_BREAD,
                ModItems.OBSIDIAN_KNIFE, ModItems.NETHERITE_KNIFE
        );

        // SandwichItems (3 layers)
        sandwichItemPair(ModItems.CHEESE_RAW_SANDWICH, ModItems.CHEESE_TOASTED_SANDWICH, "cheese_sandwich_content");

        // Tintable Handheld
        tintableKnives(
                ModItems.STONE_KNIFE, ModItems.IRON_KNIFE, ModItems.GOLD_KNIFE, ModItems.DIAMOND_KNIFE
        );

        ironCup();
    }

    @SuppressWarnings("SameParameterValue")
    private void tintableKnives(String handleTexture, String bladeTexture, String hihglightTexture, DeferredItem<?>... items) {
        String handlePath = "item/" + handleTexture;
        String bladePath = "item/" + bladeTexture;
        String hihglightPath = "item/" + hihglightTexture;

        DatagenUtils.trackTexture(existingFileHelper, handlePath);
        DatagenUtils.trackTexture(existingFileHelper, bladePath);
        DatagenUtils.trackTexture(existingFileHelper, hihglightPath);

        for (DeferredItem<?> item : items) {
            withExistingParent(item.getId().getPath(), mcLoc("item/generated"))
                    .texture("layer0", modLoc(handlePath))
                    .texture("layer1", modLoc(bladePath))
                    .texture("layer2", modLoc(hihglightPath));
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void tintableKnives(DeferredItem<?>... items) {
        tintableKnives("knife_handle", "knife_blade", "knife_highlight", items);
    }

    private void customItem2D(DeferredItem<?>... items) {
        for (DeferredItem<?> item : items) {
            String path = "item/" + item.getId().getPath();
            DatagenUtils.trackTexture(existingFileHelper, path);

            withExistingParent(item.getId().getPath(), mcLoc("item/generated"))
                    .texture("layer0", modLoc(path));
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void sandwichItemPair(DeferredItem<?> rawItem, DeferredItem<?> toastedItem, String breadName, String contentTexture) {
        sandwichItem(rawItem.getId().getPath(), "raw_" + breadName, contentTexture);
        sandwichItem(toastedItem.getId().getPath(), "toasted_" + breadName, contentTexture);
    }

    @SuppressWarnings("SameParameterValue")
    private void sandwichItemPair(DeferredItem<?> rawItem, DeferredItem<?> toastedItem, String contentTexture) {
        sandwichItemPair(rawItem, toastedItem, "sandwich_bread", contentTexture);
    }

    @SuppressWarnings("UnusedReturnValue")
    private ItemModelBuilder sandwichItem(String modelName, String breadStagePrefix, String contentTexture) {
        String lowerPath = "item/" + breadStagePrefix + "_lower";
        String contentPath = "item/" + contentTexture;
        String upperPath = "item/" + breadStagePrefix + "_upper";

        DatagenUtils.trackTexture(existingFileHelper, lowerPath);
        DatagenUtils.trackTexture(existingFileHelper, contentPath);
        DatagenUtils.trackTexture(existingFileHelper, upperPath);

        return withExistingParent(modelName, mcLoc("item/generated"))
                .texture("layer0", modLoc(lowerPath))
                .texture("layer1", modLoc(contentPath))
                .texture("layer2", modLoc(upperPath));
    }

    @SuppressWarnings("SameParameterValue")
    private void blockItemModel(DeferredItem<?> item) {
        blockItemModel(item, item.getId().getPath());
    }

    private void blockItemModel(DeferredItem<?> item, String blockPath) {
        withExistingParent(item.getId().getPath(), modLoc("block/" + blockPath));
    }

    private void ironCup() {
        customItem2D(ModItems.IRON_CUP);

        ItemModelBuilder cupBuilder = getBuilder(ModItems.IRON_CUP.getId().getPath());
        for (IronCupContent content : IronCupContent.values()) {
            String name = "iron_cup_" + content.getSerializedName();
            String path = "item/" + name;

            DatagenUtils.trackTexture(existingFileHelper, path);
            DatagenUtils.trackModel(existingFileHelper, "item/" + name);

            withExistingParent(name, mcLoc("item/generated")).texture("layer0", modLoc(path));

            cupBuilder.override()
                    .predicate(modLoc("content"), content.modelIndex() + 1)
                    .model(getExistingFile(modLoc(path)))
                    .end();
        }
    }
}