package com.nerdsoft.mods.nerdsoftkitchen.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nerdsoft.mods.nerdsoftkitchen.block.CuttingBoardBlock;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.CuttingBoardBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
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

    private static final int MAX_RENDERED_ITEMS_HARD_CAP = 24;

    private static final float MIN_HORIZONTAL_OFFSET = 0.05F;
    private static final float MAX_HORIZONTAL_OFFSET = 0.16F;

    private static final float MIN_EXTRA_Y_LIFT = 0.0F;
    private static final float MAX_EXTRA_Y_LIFT = 0.03F;

    private static final float MAX_YAW_JITTER_DEGREES = 25.0F;

    private static final double CULL_DISTANCE_BLOCKS = 48.0;
    private static final double CULL_DISTANCE_SQR = CULL_DISTANCE_BLOCKS * CULL_DISTANCE_BLOCKS;

    private static final double ITEM_DROP_STEP_BLOCKS = 16.0;

    private final ItemRenderer itemRenderer;

    public CuttingBoardBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public boolean shouldRender(@NotNull CuttingBoardBlockEntity board, @NotNull Vec3 cameraPos) {
        // vanilla default already culls at 2304 (48^2) via closerThanCenter;
        // we tighten that to our own 24-block radius on top of it.
        return BlockEntityRenderer.super.shouldRender(board, cameraPos)
                && withinCullDistance(board.getBlockPos());
    }

    @Override
    public void render(@NotNull CuttingBoardBlockEntity board, float partialTick, @NotNull PoseStack poseStack,
                        @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        renderStoredItems(board, poseStack, bufferSource, packedLight);
    }

    private static boolean withinCullDistance(BlockPos pos) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return true;
        }
        return player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= CULL_DISTANCE_SQR;
    }

    private void renderStoredItems(CuttingBoardBlockEntity board, PoseStack poseStack,
                                    MultiBufferSource bufferSource, int packedLight) {
        ItemStack stack = board.getStoredItem();
        if (stack.isEmpty()) return;

        int lodTier = board.getBlockState().getValue(CuttingBoardBlock.LOD);
        double playerDistance = distanceToPlayer(board.getBlockPos());
        boolean pastLod1 = lodTier >= 1;

        Direction facing = board.getBlockState().getValue(CuttingBoardBlock.FACING);
        float boardYRot = facing.toYRot() + 180.0F;
        float halfDepth = halfDepthFor(stack);

        int itemCount = renderedItemCountFor(stack, pastLod1, playerDistance);
        BlockPos pos = board.getBlockPos();
        RandomSource random = RandomSource.create(seedFor(pos));

        for (int i = 0; i < itemCount; i++) {
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

            renderItem(stack, poseStack, bufferSource, packedLight, board, pos, i, pastLod1);

            poseStack.popPose();
        }
    }

    private void renderItem(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                             CuttingBoardBlockEntity board, BlockPos pos, int index, boolean pastLod1) {
        int seed = (int) (pos.asLong() + index);
        boolean eligibleForFlatLod = pastLod1 && !(stack.getItem() instanceof BlockItem);

        if (!eligibleForFlatLod) {
            this.itemRenderer.renderStatic(
                    stack, ItemDisplayContext.FIXED, packedLight, OverlayTexture.NO_OVERLAY,
                    poseStack, bufferSource, board.getLevel(), seed
            );
            return;
        }

        BakedModel normalModel = itemRenderer.getModel(stack, board.getLevel(), null, seed);
        BakedModel flatModel = LodItemModelCache.singleQuad(normalModel);
        this.itemRenderer.render(
                stack, ItemDisplayContext.FIXED, false, poseStack, bufferSource,
                packedLight, OverlayTexture.NO_OVERLAY, flatModel
        );
    }

    private static double distanceToPlayer(BlockPos pos) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return 0.0;
        }
        return Math.sqrt(player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }

    private float halfDepthFor(ItemStack stack) {
        return stack.getItem() instanceof BlockItem ? BLOCK_ITEM_HALF_DEPTH : FLAT_ITEM_HALF_DEPTH;
    }

    private int renderedItemCountFor(ItemStack stack, boolean pastLod1, double playerDistance) {
        int baseCount = StackedItemCount.countFor(stack);
        baseCount = Math.min(baseCount, MAX_RENDERED_ITEMS_HARD_CAP);

        if (!pastLod1) {
            return baseCount;
        }

        int stepsPast = (int) (playerDistance / ITEM_DROP_STEP_BLOCKS);
        return Math.max(1, baseCount - stepsPast);
    }

    private long seedFor(BlockPos pos) {
        return pos.asLong();
    }

    @Override
    public boolean shouldRenderOffScreen(@NotNull CuttingBoardBlockEntity board) {
        return false;
    }

    @Override
    public int getViewDistance() {
        // Bounding box for the game's own frustum check; shouldRender() above
        // does the real 24-block cull. This just needs to be >= that.
        return 32;
    }
}
