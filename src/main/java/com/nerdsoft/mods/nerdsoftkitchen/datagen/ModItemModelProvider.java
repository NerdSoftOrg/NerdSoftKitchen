package com.nerdsoft.mods.nerdsoftkitchen.datagen;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModItems;
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

        customBlockItem2D(ModItems.WILD_PURPLE_ONION);
        customBlockItem2D(ModItems.WILD_LETTUCE);

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
            basicItem((Item) item.get());
        }

        ironCup();
    }

    private void customItem2D(DeferredItem<?> item) {
        withExistingParent(item.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + item.getId().getPath()));
    }

    private void customBlockItem2D(DeferredItem<?> item) {
        withExistingParent(item.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("block/" + item.getId().getPath()));
    }

    private void blockItemModel(DeferredItem<?> item, String blockPath) {
        withExistingParent(item.getId().getPath(), modLoc(blockPath));
    }

    private void ironCup() {
        basicItem(ModItems.IRON_CUP.get());

        ItemModelBuilder cupBuilder = getBuilder(ModItems.IRON_CUP.getId().getPath());
        for (IronCupContent content : IronCupContent.values()) {
            String name = "iron_cup_" + content.getSerializedName();
            withExistingParent(name, mcLoc("item/generated")).texture("layer0", modLoc("item/" + name));

            cupBuilder.override().predicate(modLoc("content"), content.modelIndex() + 1)
                    .model(getExistingFile(modLoc("item/" + name))).end();
        }
    }
}