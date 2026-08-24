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

@SuppressWarnings("SameParameterValue")
public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NerdSoftKitchen.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Blocks
        applianceLit(ModItems.GRILL_TABLE, ModItems.GRILL_TABLE_SOUL, ModItems.SKILLET);
        applianceBlock(ModItems.CUTTING_BOARD, ModItems.GRILL_TABLE_UNLIT);
        applianceBlockGenerated(ModItems.ORGANIC_SOIL);
        applianceBlockSuffixGenerated("_0", ModItems.FERTILE_FARMLAND);

        // 2D Items
        item2D("wild", ModItems.WILD_PURPLE_ONION, ModItems.WILD_LETTUCE, ModItems.WILD_TOMATO, ModItems.WILD_STRAWBERRY,
                ModItems.WILD_RICE);
        item2D("seed", ModItems.STRAWBERRY_SEEDS, ModItems.TOMATO_SEEDS, ModItems.LETTUCE_SEEDS, ModItems.PURPLE_ONION_SEEDS);
        item2D("food",
                ModItems.RAW_CHICKEN_PIECES, ModItems.COOKED_CHICKEN_PIECES,
                ModItems.FRIED_EGG, ModItems.SALAD, ModItems.CHEESE, ModItems.CHEESE_SLICE, ModItems.GRILLED_CHEESE,
                ModItems.TORTILLA, ModItems.STRAWBERRY, ModItems.TOMATO, ModItems.LETTUCE, ModItems.PURPLE_ONION,
                ModItems.CUT_POTATO, ModItems.CUT_PURPLE_ONION, ModItems.ORGANIC_MIXTURE, ModItems.POTATO_TORTILLA,
                ModItems.RICE_BOWL, ModItems.RICE_SEEDS, ModItems.ONION_TORTILLA, ModItems.RICE, ModItems.COOKED_RICE
        );

        item2D("sandwich/raw", ModItems.RAW_SANDWICH_BREAD);
        item2D("sandwich/toasted", ModItems.TOASTED_SANDWICH_BREAD);

        // Tools & Extras
        item2D("knife/custom", ModItems.NETHERITE_KNIFE);
        sandwichPair(ModItems.CHEESE_RAW_SANDWICH, ModItems.CHEESE_TOASTED_SANDWICH, "cheese_sandwich_content");
        tintableKnives(ModItems.STONE_KNIFE, ModItems.IRON_KNIFE, ModItems.GOLDEN_KNIFE, ModItems.DIAMOND_KNIFE, ModItems.OBSIDIAN_KNIFE);
        ironCup();
    }

    // block/appliance/<itemId>_lit
    private void applianceLit(DeferredItem<?>... items) {
        for (DeferredItem<?> item : items) {
            String itemId = item.getId().getPath();
            blockItemModel(itemId, "appliance/" + itemId + "_lit");
        }
    }

    // block/appliance/<itemId>
    private void applianceBlock(DeferredItem<?>... items) {
        for (DeferredItem<?> item : items) {
            String itemId = item.getId().getPath();
            blockItemModel(itemId, "appliance/" + itemId);
        }
    }

    // generated/**/block/<itemId>
    private void applianceBlockGenerated(DeferredItem<?>... items) {
        for (DeferredItem<?> item : items) {
            String itemId = item.getId().getPath();
            blockItemModel(itemId, itemId);
        }
    }

    //generated/**/block/<itemId><suffix>
    private void applianceBlockSuffixGenerated(String suffix, DeferredItem<?>... items) {
        for (DeferredItem<?> item : items) {
            String itemId = item.getId().getPath();
            blockItemModel(itemId, itemId + suffix);
        }
    }

    private void blockItemModel(String itemPath, String blockPath) {
        DatagenUtils.trackModel(existingFileHelper, "block/" + blockPath);
        withExistingParent(itemPath, modLoc("block/" + blockPath));
    }

    private void item2D(String folder, DeferredItem<?>... items) {
        for (DeferredItem<?> item : items) {
            generatedLayers(item.getId().getPath(), folder + "/" + item.getId().getPath());
        }
    }

    private void sandwichPair(DeferredItem<?> rawItem, DeferredItem<?> toastedItem, String contentTex) {
        sandwichItem(rawItem.getId().getPath(), "raw/raw_sandwich_bread", contentTex);
        sandwichItem(toastedItem.getId().getPath(), "toasted/toasted_sandwich_bread", contentTex);
    }

    private void sandwichItem(String modelName, String breadPrefix, String contentTex) {
        generatedLayers(modelName,
                "sandwich/" + breadPrefix + "_lower",
                "sandwich/content/" + contentTex,
                "sandwich/" + breadPrefix + "_upper"
        );
    }

    private void tintableKnives(DeferredItem<?>... items) {
        for (DeferredItem<?> item : items) {
            generatedLayers(item.getId().getPath(), "knife/knife_handle", "knife/knife_blade", "knife/knife_highlight");
        }
    }

    private void ironCup() {
        String baseName = ModItems.IRON_CUP.getId().getPath();
        String baseTexture = baseName + "/" + baseName;

        generatedLayers(baseName, baseTexture);

        ItemModelBuilder cupBuilder = getBuilder(baseName);

        for (IronCupContent content : IronCupContent.values()) {
            String name = baseName + "_" + content.getSerializedName();
            DatagenUtils.trackModel(existingFileHelper, "item/" + name);

            generatedLayers(name, baseTexture, baseName + "/" + name);

            cupBuilder.override()
                    .predicate(modLoc("content"), content.modelIndex() + 1)
                    .model(getExistingFile(modLoc("item/" + name)))
                    .end();
        }
    }

    private void generatedLayers(String modelName, String... relativeTextures) {
        ItemModelBuilder builder = withExistingParent(modelName, mcLoc("item/generated"));
        for (int i = 0; i < relativeTextures.length; i++) {
            String fullPath = "item/" + relativeTextures[i];
            DatagenUtils.trackTexture(existingFileHelper, fullPath);
            builder.texture("layer" + i, modLoc(fullPath));
        }
    }
}
