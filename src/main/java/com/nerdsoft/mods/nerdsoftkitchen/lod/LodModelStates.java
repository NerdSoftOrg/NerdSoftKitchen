package com.nerdsoft.mods.nerdsoftkitchen.lod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

public final class LodModelStates {

    private LodModelStates() {
    }

    public static BakedModel[] forBlock(String modId, String baseName, int maxTier, String suffix) {
        BakedModel[] models = new BakedModel[maxTier + 1];
        for (int tier = 0; tier <= maxTier; tier++) {
            models[tier] = tierModel(modId, baseName, tier, suffix);
        }
        return models;
    }

    private static BakedModel tierModel(String modId, String baseName, int tier, String suffix) {
        String name = LodModelSet.modelName(baseName, tier, suffix);
        ResourceLocation blockModelId = ResourceLocation.fromNamespaceAndPath(modId, "block/" + name);
        return Minecraft.getInstance().getModelManager()
                .getModel(ModelResourceLocation.standalone(blockModelId));
    }
}