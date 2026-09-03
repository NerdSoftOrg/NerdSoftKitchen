package com.panzer.mods.dice_and_delish.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.panzer.mods.dice_and_delish.DiceAndDelish;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public final class FlatLiquidQuadRenderer {

    public static final ResourceLocation DEFAULT_LIQUID_SPRITE =
            ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, "block/display/skillet_pool");

    private static TextureAtlasSprite cachedDefaultSprite;

    private FlatLiquidQuadRenderer() {
    }

    public static void renderFlatPool(PoseStack poseStack, MultiBufferSource bufferSource,
                                      ResourceLocation sprite, int argbColor,
                                      float halfWidth, float halfDepth, int packedLight) {
        TextureAtlasSprite atlasSprite = resolveSprite(sprite);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.translucent());
        PoseStack.Pose pose = poseStack.last();

        float a = ((argbColor >>> 24) & 0xFF) / 255.0F;
        float r = ((argbColor >>> 16) & 0xFF) / 255.0F;
        float g = ((argbColor >>> 8) & 0xFF) / 255.0F;
        float b = (argbColor & 0xFF) / 255.0F;

        float u0 = atlasSprite.getU0();
        float u1 = atlasSprite.getU1();
        float v0 = atlasSprite.getV0();
        float v1 = atlasSprite.getV1();

        addVertex(consumer, pose, -halfWidth, 0.0F, -halfDepth, r, g, b, a, u0, v0, packedLight, Direction.UP);
        addVertex(consumer, pose, -halfWidth, 0.0F, halfDepth, r, g, b, a, u0, v1, packedLight, Direction.UP);
        addVertex(consumer, pose, halfWidth, 0.0F, halfDepth, r, g, b, a, u1, v1, packedLight, Direction.UP);
        addVertex(consumer, pose, halfWidth, 0.0F, -halfDepth, r, g, b, a, u1, v0, packedLight, Direction.UP);
    }

    public static void renderFlatPool(PoseStack poseStack, MultiBufferSource bufferSource,
                                      int argbColor, float halfWidth, float halfDepth, int packedLight) {
        renderFlatPool(poseStack, bufferSource, DEFAULT_LIQUID_SPRITE, argbColor, halfWidth, halfDepth, packedLight);
    }

    private static TextureAtlasSprite resolveSprite(ResourceLocation sprite) {
        if (sprite.equals(DEFAULT_LIQUID_SPRITE)) {
            if (cachedDefaultSprite == null) {
                ModelManager modelManager = Minecraft.getInstance().getModelManager();
                cachedDefaultSprite = modelManager.getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(sprite);
            }
            return cachedDefaultSprite;
        }
        ModelManager modelManager = Minecraft.getInstance().getModelManager();
        return modelManager.getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(sprite);
    }

    public static void clearCache() {
        cachedDefaultSprite = null;
    }

    @SuppressWarnings("SameParameterValue")
    private static void addVertex(VertexConsumer consumer, PoseStack.Pose pose,
                                  float x, float y, float z,
                                  float r, float g, float b, float a,
                                  float u, float v, int packedLight, Direction normal) {
        consumer.addVertex(pose, x, y, z)
                .setColor(r, g, b, a)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, normal.getStepX(), normal.getStepY(), normal.getStepZ());
    }
}
