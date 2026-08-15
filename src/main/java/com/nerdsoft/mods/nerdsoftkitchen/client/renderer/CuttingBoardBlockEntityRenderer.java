package com.nerdsoft.mods.nerdsoftkitchen.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nerdsoft.mods.nerdsoftkitchen.block.CuttingBoardBlock;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.CuttingBoardBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CuttingBoardBlockEntityRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity> {

    private static final float BOARD_SURFACE_Y = 0.0625F;
    private static final float BASE_SCALE = 0.4F;

    private static final float FLAT_ITEM_HALF_DEPTH = BOARD_SURFACE_Y / 2.0F;
    private static final float BLOCK_ITEM_HALF_DEPTH = BOARD_SURFACE_Y * 2 / 2.0F;

    private static final float STACK_FRACTION_PER_EXTRA_ITEM = 0.2F;
    private static final int MAX_RENDERED_ITEMS = 6;

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
    public void render(@NotNull CuttingBoardBlockEntity board, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        ItemStack stack = board.getStoredItem();
        if (stack.isEmpty()) return;

        Direction facing = board.getBlockState().getValue(CuttingBoardBlock.FACING);
        float boardYRot = facing.toYRot() + 180.0F;
        float halfDepth = halfDepthFor(stack);

        int itemCount = renderedItemCountFor(stack);
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

            this.itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    bufferSource,
                    board.getLevel(),
                    (int) (pos.asLong() + i)
            );

            poseStack.popPose();
        }
    }

    private float halfDepthFor(ItemStack stack) {
        return stack.getItem() instanceof BlockItem ? BLOCK_ITEM_HALF_DEPTH : FLAT_ITEM_HALF_DEPTH;
    }

    private int renderedItemCountFor(ItemStack stack) {
        int maxStackSize = stack.getMaxStackSize();
        if (maxStackSize <= 1) {
            return 1;
        }
        float fraction = (float) stack.getCount() / maxStackSize;
        int extra = (int) (fraction / STACK_FRACTION_PER_EXTRA_ITEM);
        return 1 + Math.min(extra, MAX_RENDERED_ITEMS - 1);
    }

    private long seedFor(BlockPos pos) {
        return pos.asLong();
    }
}