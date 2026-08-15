package com.nerdsoft.mods.nerdsoftkitchen.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nerdsoft.mods.nerdsoftkitchen.block.GrillTableBlock;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.GrillTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class GrillTableBlockEntityRenderer implements BlockEntityRenderer<GrillTableBlockEntity> {

    private static final float ITEM_SCALE = 0.375F;
    private static final double GRILL_Y = 1.03125;
    private static final double GRILL_BASE_OFFSET = 0.25;
    private static final double CAMPFIRE_Y = 0.44921875;
    private static final double CAMPFIRE_ITEM_OFFSET = 0.3125;
    private static final double CAMPFIRE_LATERAL_OFFSET = 0.25;
    private static final int CAMPFIRE_SEED_SPLIT = 4;

    private static final double[] CORNER_X = new double[4];
    private static final double[] CORNER_Z = new double[4];
    private static final float[] CORNER_YAW = new float[4];
    private static final double[] CAMPFIRE_WIDTH = {CAMPFIRE_ITEM_OFFSET, CAMPFIRE_LATERAL_OFFSET, CAMPFIRE_ITEM_OFFSET, CAMPFIRE_LATERAL_OFFSET};

    static {
        for (int i = 0; i < 4; i++) {
            Direction dir = Direction.from2DDataValue(i);
            Direction cw = dir.getClockWise();
            CORNER_X[i] = -dir.getStepX() * GRILL_BASE_OFFSET + cw.getStepX() * GRILL_BASE_OFFSET;
            CORNER_Z[i] = -dir.getStepZ() * GRILL_BASE_OFFSET + cw.getStepZ() * GRILL_BASE_OFFSET;
            CORNER_YAW[i] = -dir.toYRot();
        }
    }

    private final ItemRenderer itemRenderer;

    public GrillTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull GrillTableBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        int facing2D = state.getValue(GrillTableBlock.FACING).get2DDataValue();
        int seedBase = blockEntity.getRenderSeedBase();
        ClientLevel renderLevel = Minecraft.getInstance().level;

        for (int slot = 0; slot < GrillTableBlockEntity.GRILL_SLOTS_COUNT; slot++) {
            ItemStack stack = blockEntity.getItem(GrillTableBlockEntity.GRILL_SLOTS_START + slot);
            if (!stack.isEmpty()) {
                renderGrillSlot(blockEntity, slot, stack, facing2D, seedBase, poseStack, bufferSource, packedLight, packedOverlay, renderLevel);
            }
        }
        for (int slot = 0; slot < GrillTableBlockEntity.CAMPFIRE_SLOTS_COUNT; slot++) {
            ItemStack stack = blockEntity.getItem(GrillTableBlockEntity.CAMPFIRE_SLOTS_START + slot);
            if (!stack.isEmpty()) {
                renderCampfireSlot(slot, stack, facing2D, seedBase, poseStack, bufferSource, packedLight, packedOverlay, renderLevel);
            }
        }
    }

    private void renderGrillSlot(GrillTableBlockEntity blockEntity, int slot, ItemStack stack, int facing2D,
                                 int seedBase, PoseStack poseStack, MultiBufferSource bufferSource,
                                 int packedLight, int packedOverlay, ClientLevel renderLevel) {
        int dirIndex = (slot + facing2D) & 3;
        double x = 0.5 + CORNER_X[dirIndex] + blockEntity.getGrillOffsetX(slot);
        double z = 0.5 + CORNER_Z[dirIndex] + blockEntity.getGrillOffsetZ(slot);
        float rotation = CORNER_YAW[dirIndex] + blockEntity.getGrillRotation(slot);

        poseStack.pushPose();
        poseStack.translate(x, GRILL_Y, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, renderLevel, seedBase + slot);
        poseStack.popPose();
    }

    private void renderCampfireSlot(int slot, ItemStack stack, int facing2D, int seedBase, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, ClientLevel renderLevel) {
        int dirIndex = (slot + facing2D) & 3;
        float yRot = CORNER_YAW[dirIndex];
        double widthOffset = CAMPFIRE_WIDTH[slot];

        poseStack.pushPose();
        poseStack.translate(0.5, CAMPFIRE_Y, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.translate(-CAMPFIRE_ITEM_OFFSET, -widthOffset, 0.0);
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, renderLevel, seedBase + slot + CAMPFIRE_SEED_SPLIT);
        poseStack.popPose();
    }
}