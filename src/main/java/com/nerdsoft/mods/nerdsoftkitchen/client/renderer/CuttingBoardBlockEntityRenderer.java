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
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CuttingBoardBlockEntityRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity> {

    private final ItemRenderer itemRenderer;

    public CuttingBoardBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull CuttingBoardBlockEntity board, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        ItemStack stack = board.getStoredItem();
        if (stack.isEmpty()) return;

        poseStack.pushPose();

        // 1. Centrar en la tabla (X=0.5, Z=0.5) y elevarlo sobre la tabla (Y = 1px / 16px = 0.0625)
        poseStack.translate(0.5D, 0.0625D, 0.5D);

        // 2. Rotar el objeto según la dirección del bloque
        Direction facing = board.getBlockState().getValue(CuttingBoardBlock.FACING);
        float angle = -facing.toYRot();
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));

        // 3. Tumbar el ítem horizontalmente sobre la tabla
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

        // 4. Ajustar escala para que no desborde la tabla
        poseStack.scale(0.4F, 0.4F, 0.4F);

        // 5. Renderizar el ítem
        this.itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                board.getLevel(),
                (int) board.getBlockPos().asLong()
        );

        poseStack.popPose();
    }
}