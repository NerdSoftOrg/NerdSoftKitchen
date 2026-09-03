package com.panzer.mods.dice_and_delish.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.panzer.mods.dice_and_delish.block.CuttingBoardBlock;
import com.panzer.mods.dice_and_delish.blockentity.CuttingBoardBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CuttingBoardBlockEntityRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity> {

    private static final float BOARD_SURFACE_Y = 0.0625F;
    private static final float BASE_SCALE = 0.4F;

    private static final float FLAT_ITEM_HALF_DEPTH = BOARD_SURFACE_Y / 2.0F;
    private static final float BLOCK_ITEM_HALF_DEPTH = BOARD_SURFACE_Y * 2 / 2.0F;

    private static final float MIN_HORIZONTAL_OFFSET = 0.05F;
    private static final float MAX_HORIZONTAL_OFFSET = 0.16F;

    private static final float MIN_EXTRA_Y_LIFT = 0.0F;
    private static final float MAX_EXTRA_Y_LIFT = 0.03F;

    private static final float MAX_YAW_JITTER_DEGREES = 25.0F;

    private final ItemRenderer itemRenderer;

    public CuttingBoardBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public boolean shouldRender(@NotNull CuttingBoardBlockEntity board, @NotNull Vec3 cameraPos) {
        return !board.getStoredItem().isEmpty();
    }

    @Override
    public void render(@NotNull CuttingBoardBlockEntity board, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        double distanceSqr = distanceToPlayerSqr(board.getBlockPos());

        if (distanceSqr > LodConfig.CULL_DISTANCE_SQR) {
            return;
        }

        renderStoredItems(board, poseStack, bufferSource, packedLight, distanceSqr);
    }

    private void renderStoredItems(CuttingBoardBlockEntity board, PoseStack poseStack,
                                   MultiBufferSource bufferSource, int packedLight, double playerDistanceSqr) {
        ItemStack stack = board.getStoredItem();
        if (stack.isEmpty()) return;

        BlockPos pos = board.getBlockPos();
        boolean pastLod1 = ScreenSpaceLod.isPastThreshold(pos.asLong(), LodConfig.ITEM_RADIUS_SQR, playerDistanceSqr, LodConfig.ITEM_LOD_COVERAGE_THRESHOLD);

        Direction facing = board.getBlockState().getValue(CuttingBoardBlock.FACING);
        float boardYRot = facing.toYRot() + 180.0F;
        float halfDepth = (!pastLod1 && stack.getItem() instanceof BlockItem) ? BLOCK_ITEM_HALF_DEPTH : FLAT_ITEM_HALF_DEPTH;

        int itemCount = ItemLodRenderer.renderedItemCountFor(stack, pastLod1, playerDistanceSqr);
        RandomSource random = RandomSource.create(seedFor(pos));

        for (int i = 0; i < itemCount; i++) {
            int seed = (int) (pos.asLong() + i);

            poseStack.pushPose();

            float offsetX = 0.0F;
            float offsetZ = 0.0F;
            float extraLift = 0.0F;
            float yawJitter = 0.0F;

            if (i > 0) {
                float angle = random.nextFloat() * 360.0F;
                float radius = MIN_HORIZONTAL_OFFSET + random.nextFloat() * (MAX_HORIZONTAL_OFFSET - MIN_HORIZONTAL_OFFSET);
                offsetX = (float) (Math.cos(Math.toRadians(angle)) * radius);
                offsetZ = (float) (Math.sin(Math.toRadians(angle)) * radius);
                extraLift = MIN_EXTRA_Y_LIFT + random.nextFloat() * (MAX_EXTRA_Y_LIFT - MIN_EXTRA_Y_LIFT);
                yawJitter = (random.nextFloat() * 2.0F - 1.0F) * MAX_YAW_JITTER_DEGREES;
            }

            poseStack.translate(0.5D + offsetX, BOARD_SURFACE_Y + extraLift + halfDepth, 0.5D + offsetZ);
            poseStack.mulPose(Axis.YP.rotationDegrees(boardYRot + yawJitter));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(BASE_SCALE, BASE_SCALE, BASE_SCALE);

            ItemLodRenderer.renderItem(this.itemRenderer, stack, ItemDisplayContext.FIXED, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, (ClientLevel) board.getLevel(), seed, pastLod1);

            poseStack.popPose();
        }
    }

    private static double distanceToPlayerSqr(BlockPos pos) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return 0.0;
        }
        return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    private long seedFor(BlockPos pos) {
        return pos.asLong();
    }
}
