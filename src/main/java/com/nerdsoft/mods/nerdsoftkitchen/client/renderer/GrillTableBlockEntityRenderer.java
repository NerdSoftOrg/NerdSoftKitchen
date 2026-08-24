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
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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

    private static final double CULL_DISTANCE_BLOCKS = 48.0;
    private static final double CULL_DISTANCE_SQR = CULL_DISTANCE_BLOCKS * CULL_DISTANCE_BLOCKS;

    private static final double ITEM_RADIUS_BLOCKS = 0.3;
    private static final double ITEM_RADIUS_SQR = ITEM_RADIUS_BLOCKS * ITEM_RADIUS_BLOCKS;

    private static final double LOD_COVERAGE_THRESHOLD = ScreenSpaceLod.thresholdForDistance(ITEM_RADIUS_SQR, 32.0);

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
    public boolean shouldRender(@NotNull GrillTableBlockEntity blockEntity, @NotNull Vec3 cameraPos) {
        return BlockEntityRenderer.super.shouldRender(blockEntity, cameraPos)
                && withinCullDistance(blockEntity.getBlockPos().getX() + 0.5,
                                       blockEntity.getBlockPos().getY() + 0.5,
                                       blockEntity.getBlockPos().getZ() + 0.5);
    }

    @Override
    public void render(@NotNull GrillTableBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        int facing2D = state.getValue(GrillTableBlock.FACING).get2DDataValue();
        int seedBase = blockEntity.getRenderSeedBase();
        ClientLevel renderLevel = Minecraft.getInstance().level;
        boolean pastLod1 = isPastLodDistance(blockEntity);

        for (int slot = 0; slot < GrillTableBlockEntity.GRILL_SLOTS_COUNT; slot++) {
            ItemStack stack = blockEntity.getItem(GrillTableBlockEntity.GRILL_SLOTS_START + slot);
            if (!stack.isEmpty()) {
                renderGrillSlot(blockEntity, slot, stack, facing2D, seedBase, poseStack, bufferSource, packedLight, packedOverlay, renderLevel, pastLod1);
            }
        }
        for (int slot = 0; slot < GrillTableBlockEntity.CAMPFIRE_SLOTS_COUNT; slot++) {
            ItemStack stack = blockEntity.getItem(GrillTableBlockEntity.CAMPFIRE_SLOTS_START + slot);
            if (!stack.isEmpty()) {
                renderCampfireSlot(slot, stack, facing2D, seedBase, poseStack, bufferSource, packedLight, packedOverlay, renderLevel, pastLod1);
            }
        }
    }

    private static boolean withinCullDistance(double x, double y, double z) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return true;
        }
        return player.distanceToSqr(x, y, z) <= CULL_DISTANCE_SQR;
    }

    private static boolean isPastLodDistance(GrillTableBlockEntity blockEntity) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return false;
        }
        double x = blockEntity.getBlockPos().getX() + 0.5;
        double y = blockEntity.getBlockPos().getY() + 0.5;
        double z = blockEntity.getBlockPos().getZ() + 0.5;
        double distanceSqr = player.distanceToSqr(x, y, z);
        return ScreenSpaceLod.isPastThreshold(blockEntity.getBlockPos().asLong(), ITEM_RADIUS_SQR, distanceSqr, LOD_COVERAGE_THRESHOLD);
    }

    private void renderGrillSlot(GrillTableBlockEntity blockEntity, int slot, ItemStack stack, int facing2D,
                                 int seedBase, PoseStack poseStack, MultiBufferSource bufferSource,
                                 int packedLight, int packedOverlay, ClientLevel renderLevel, boolean pastLod1) {
        int dirIndex = (slot + facing2D) & 3;
        double x = 0.5 + CORNER_X[dirIndex] + blockEntity.getGrillOffsetX(slot);
        double z = 0.5 + CORNER_Z[dirIndex] + blockEntity.getGrillOffsetZ(slot);
        float rotation = CORNER_YAW[dirIndex] + blockEntity.getGrillRotation(slot);

        poseStack.pushPose();
        poseStack.translate(x, GRILL_Y, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        renderItem(stack, poseStack, bufferSource, packedLight, packedOverlay, renderLevel, seedBase + slot, pastLod1);
        poseStack.popPose();
    }

    private void renderCampfireSlot(int slot, ItemStack stack, int facing2D, int seedBase, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, ClientLevel renderLevel, boolean pastLod1) {
        int dirIndex = (slot + facing2D) & 3;
        float yRot = CORNER_YAW[dirIndex];
        double widthOffset = CAMPFIRE_WIDTH[slot];

        poseStack.pushPose();
        poseStack.translate(0.5, CAMPFIRE_Y, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.translate(-CAMPFIRE_ITEM_OFFSET, -widthOffset, 0.0);
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        renderItem(stack, poseStack, bufferSource, packedLight, packedOverlay, renderLevel, seedBase + slot + CAMPFIRE_SEED_SPLIT, pastLod1);
        poseStack.popPose();
    }

    private void renderItem(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource,
                             int packedLight, int packedOverlay, ClientLevel renderLevel, int seed, boolean pastLod1) {
        if (!pastLod1) {
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, renderLevel, seed);
            return;
        }

        BakedModel normalModel = itemRenderer.getModel(stack, renderLevel, null, seed);
        BakedModel flatModel = LodItemModelCache.singleQuad(normalModel);
        itemRenderer.render(stack, ItemDisplayContext.FIXED, false, poseStack, bufferSource, packedLight, packedOverlay, flatModel);
    }
}
