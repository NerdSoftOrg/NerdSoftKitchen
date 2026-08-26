package com.nerdsoft.mods.nerdsoftkitchen.client.renderer;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.EnumMap;
import java.util.Map;

public final class BlockLodModelCache {

    public enum Variant {
        GRILL_TABLE_LIT("block/appliance/grill_table_lit_lod1"),
        GRILL_TABLE_SOUL_LIT("block/appliance/grill_table_soul_lit_lod1"),
        GRILL_TABLE_UNLIT("block/appliance/grill_table_unlit_lod1"),
        CUTTING_BOARD("block/appliance/cutting_board_lod1");

        final ResourceLocation modelLocation;

        Variant(String path) {
            this.modelLocation = ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, path);
        }
    }

    private static final Map<Variant, BakedModel> MODELS = new EnumMap<>(Variant.class);

    private BlockLodModelCache() {
    }

    public static void registerAdditional(ModelEvent.RegisterAdditional event) {
        for (Variant variant : Variant.values()) {
            event.register(ModelResourceLocation.standalone(variant.modelLocation));
        }
    }

    public static void onBakingCompleted(ModelEvent.BakingCompleted event) {
        MODELS.clear();
        Map<ModelResourceLocation, BakedModel> baked = event.getModels();
        for (Variant variant : Variant.values()) {
            BakedModel model = baked.get(ModelResourceLocation.standalone(variant.modelLocation));
            if (model != null) {
                MODELS.put(variant, model);
            }
        }
    }

    public static BakedModel get(Variant variant) {
        return MODELS.get(variant);
    }
}
