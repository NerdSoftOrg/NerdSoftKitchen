package com.nerdsoft.mods.nerdsoftkitchen.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.nerdsoft.mods.nerdsoftkitchen.block.SkilletBlock;
import com.nerdsoft.mods.nerdsoftkitchen.blockentity.SkilletBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SkilletBlockEntityRenderer implements BlockEntityRenderer<SkilletBlockEntity> {

    private static final int SLOT_COUNT = SkilletBlockEntity.PAN_SLOTS_COUNT;

    private static final float ITEM_SCALE = 0.3F;
    private static final double PAN_SURFACE_Y = 0.140625;
    private static final double SLOT_RADIUS = 0.19;
    private static final float SLOT_TILT_DEGREES = 8.0F;
    private static final double SLOT_ANGLE_OFFSET_DEGREES = 45.0;
    private static final double SLOT_ANGLE_STEP_DEGREES = 360.0 / SLOT_COUNT;

    private static final double[] SLOT_X = new double[SLOT_COUNT];
    private static final double[] SLOT_Z = new double[SLOT_COUNT];

    static {
        for (int i = 0; i < SLOT_COUNT; i++) {
            double angle = Math.toRadians(SLOT_ANGLE_OFFSET_DEGREES + i * SLOT_ANGLE_STEP_DEGREES);
            SLOT_X[i] = Math.cos(angle) * SLOT_RADIUS;
            SLOT_Z[i] = Math.sin(angle) * SLOT_RADIUS;
        }
    }

    private final ItemRenderer itemRenderer;

    public SkilletBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull SkilletBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack,
                        @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        if (!state.hasProperty(SkilletBlock.FACING)) {
            return;
        }

        int seedBase = blockEntity.getRenderSeedBase();
        ClientLevel renderLevel = Minecraft.getInstance().level;

        for (int slot = 0; slot < SLOT_COUNT; slot++) {
            ItemStack stack = blockEntity.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            renderSlot(blockEntity, slot, stack, seedBase, poseStack, bufferSource, packedLight, packedOverlay, renderLevel);
        }
    }

    private void renderSlot(SkilletBlockEntity blockEntity, int slot, ItemStack stack, int seedBase,
                             PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay,
                             ClientLevel renderLevel) {
        double x = 0.5 + SLOT_X[slot] + blockEntity.getPanOffsetX(slot);
        double z = 0.5 + SLOT_Z[slot] + blockEntity.getPanOffsetZ(slot);
        float rotation = blockEntity.getPanRotation(slot);

        poseStack.pushPose();
        poseStack.translate(x, PAN_SURFACE_Y, z);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F + SLOT_TILT_DEGREES));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, renderLevel, seedBase + slot);
        poseStack.popPose();
    }
}