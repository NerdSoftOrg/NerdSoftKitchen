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
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SkilletBlockEntityRenderer implements BlockEntityRenderer<SkilletBlockEntity> {

    private static final int SLOT_COUNT = SkilletBlockEntity.PAN_SLOTS_COUNT;

    // Below this AABB height (in pixels, 0-16 scale) items lie flat on the surface;
    // at or above it, items are rotated 90 degrees to stand against the taller shape.
    private static final double ROTATE_THRESHOLD_PIXELS = 8.0;

    private static final float ITEM_SCALE = 0.3F;
    private static final double SLOT_RADIUS = 0.19;
    private static final float SLOT_TILT_DEGREES = 8.0F;
    private static final double SLOT_ANGLE_OFFSET_DEGREES = 45.0;
    private static final double SLOT_ANGLE_STEP_DEGREES = 360.0 / SLOT_COUNT;

    private static final float PILE_HORIZONTAL_OFFSET = 0.045F;
    private static final float PILE_Y_LIFT_STEP = 0.012F;
    private static final float PILE_YAW_JITTER_DEGREES = 15.0F;

    private static final int LIQUID_EGG_ARGB = 0xF0F2D34A;
    private static final float EGG_POOL_Y_OFFSET = 0.05F;
    private static final float EGG_POOL_SIZE = 0.5f;

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

        AABB shapeBounds = surfaceBounds(state, renderLevel, blockEntity.getBlockPos());
        boolean rotateUpright = shapeBounds.maxY * 16.0 >= ROTATE_THRESHOLD_PIXELS;
        float surfaceY = (float) shapeBounds.maxY;

        boolean hasEgg = false;

        for (int slot = 0; slot < SLOT_COUNT; slot++) {
            ItemStack stack = blockEntity.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(Items.EGG)) {
                hasEgg = true;
                continue;
            }
            renderSlot(blockEntity, slot, stack, seedBase, surfaceY, rotateUpright,
                    poseStack, bufferSource, packedLight, packedOverlay, renderLevel);
        }

        if (hasEgg) {
            renderEggPool(surfaceY, poseStack, bufferSource, packedLight);
        }
    }

    private void renderEggPool(float surfaceY, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.5, surfaceY - EGG_POOL_Y_OFFSET, 0.5);
        FlatLiquidQuadRenderer.renderFlatPool(poseStack, bufferSource, LIQUID_EGG_ARGB, EGG_POOL_SIZE, EGG_POOL_SIZE, packedLight);
        poseStack.popPose();
    }

    private AABB surfaceBounds(BlockState state, ClientLevel level, BlockPos pos) {
        if (level == null) {
            return new AABB(0, 0, 0, 1, 0, 1);
        }
        VoxelShape shape = state.getShape(level, pos, CollisionContext.empty());
        return shape.isEmpty() ? new AABB(0, 0, 0, 1, 0, 1) : shape.bounds();
    }

    private void renderSlot(SkilletBlockEntity blockEntity, int slot, ItemStack stack, int seedBase,
                             float surfaceY, boolean rotateUpright,
                             PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay,
                             ClientLevel renderLevel) {
        double baseX = 0.5 + SLOT_X[slot] + blockEntity.getPanOffsetX(slot);
        double baseZ = 0.5 + SLOT_Z[slot] + blockEntity.getPanOffsetZ(slot);
        float rotation = blockEntity.getPanRotation(slot);

        int itemCount = StackedItemCount.countFor(stack);
        RandomSource random = RandomSource.create(seedBase + slot);

        for (int i = 0; i < itemCount; i++) {
            poseStack.pushPose();

            double x = baseX;
            double z = baseZ;
            float yLift = 0.0F;
            float yawJitter = 0.0F;

            if (i > 0) {
                float angle = random.nextFloat() * 360.0F;
                float radius = random.nextFloat() * PILE_HORIZONTAL_OFFSET;
                x += Math.cos(Math.toRadians(angle)) * radius;
                z += Math.sin(Math.toRadians(angle)) * radius;
                yLift = i * PILE_Y_LIFT_STEP;
                yawJitter = (random.nextFloat() * 2.0F - 1.0F) * PILE_YAW_JITTER_DEGREES;
            }

            poseStack.translate(x, surfaceY + yLift, z);
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation + yawJitter));
            if (rotateUpright) {
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F + SLOT_TILT_DEGREES));
            }
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

            int seed = seedBase + slot * 31 + i;
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, packedLight, packedOverlay,
                    poseStack, bufferSource, renderLevel, seed);

            poseStack.popPose();
        }
    }
}
