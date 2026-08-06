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
    protected void registerModels() {
        // Block Items
        blockItemModel(ModItems.GRILL_TABLE, "grill_table_lit");
        blockItemModel(ModItems.GRILL_TABLE_SOUL, "grill_table_soul_lit");

        blockItemModel(ModItems.CUTTING_BOARD);

        // 2D Items
        customItem2D(
                ModItems.WILD_PURPLE_ONION, ModItems.WILD_LETTUCE, ModItems.WILD_TOMATO, ModItems.WILD_STRAWBERRY,
                ModItems.STRAWBERRY_SEEDS, ModItems.TOMATO_SEEDS, ModItems.LETTUCE_SEEDS, ModItems.PURPLE_ONION_SEEDS,
                ModItems.STRAWBERRY, ModItems.TOMATO, ModItems.LETTUCE, ModItems.PURPLE_ONION,
                ModItems.RAW_CHICKEN_PIECES, ModItems.COOKED_CHICKEN_PIECES, ModItems.FRIED_EGG, ModItems.SALAD,
                ModItems.CHEESE, ModItems.CHEESE_SLICE, ModItems.CHEESE_SANDWICH, ModItems.GRILLED_CHEESE
        );

        // Handheld 2D
        handheldItem2D(
                ModItems.STONE_KNIFE, ModItems.IRON_KNIFE, ModItems.GOLD_KNIFE,
                ModItems.DIAMOND_KNIFE, ModItems.OBSIDIAN_KNIFE
        );

        ironCup();
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
    private void handheldItem2D(DeferredItem<?>... items) {
        for (DeferredItem<?> item : items) {
            String path = "item/" + item.getId().getPath();
            DatagenUtils.trackTexture(existingFileHelper, path);

            withExistingParent(item.getId().getPath(), mcLoc("item/handheld"))
                    .texture("layer0", modLoc(path));
        }
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