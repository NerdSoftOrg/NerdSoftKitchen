package com.nerdsoft.mods.nerdsoftkitchen.datagen.item;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.datagen.DatagenUtils;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
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
        blockItemModel(ModItems.GRILL_TABLE, "block/grill_table_lit");
        blockItemModel(ModItems.GRILL_TABLE_SOUL, "block/grill_table_soul_lit");

        customItem2D(ModItems.WILD_PURPLE_ONION);
        customItem2D(ModItems.WILD_LETTUCE);
        customItem2D(ModItems.WILD_TOMATO);
        customItem2D(ModItems.WILD_STRAWBERRY);

        customItem2D(ModItems.STRAWBERRY_SEEDS);
        customItem2D(ModItems.TOMATO_SEEDS);
        customItem2D(ModItems.LETTUCE_SEEDS);
        customItem2D(ModItems.PURPLE_ONION_SEEDS);

        for (var item : new DeferredItem[]{
                ModItems.STRAWBERRY, ModItems.TOMATO, ModItems.LETTUCE, ModItems.PURPLE_ONION,
                ModItems.RAW_CHICKEN_PIECES, ModItems.COOKED_CHICKEN_PIECES, ModItems.FRIED_EGG, ModItems.SALAD
        }) {
            customItem2D(item);
        }

        ironCup();
    }

    private void customItem2D(DeferredItem<?> item) {
        String path = "item/" + item.getId().getPath();
        DatagenUtils.trackTexture(existingFileHelper, path);

        withExistingParent(item.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc(path));
    }

    @SuppressWarnings("unused")
    private void customBlockItem2D(DeferredItem<?> item) {
        String path = "block/" + item.getId().getPath();
        DatagenUtils.trackTexture(existingFileHelper, path);

        withExistingParent(item.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc(path));
    }

    private void blockItemModel(DeferredItem<?> item, String blockPath) {
        withExistingParent(item.getId().getPath(), modLoc(blockPath));
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

            cupBuilder.override().predicate(modLoc("content"), content.modelIndex() + 1)
                    .model(getExistingFile(modLoc(path))).end();
        }
    }
}