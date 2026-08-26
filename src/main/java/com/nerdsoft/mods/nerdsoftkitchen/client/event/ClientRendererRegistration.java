package com.nerdsoft.mods.nerdsoftkitchen.client.event;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.client.renderer.BlockLodModelCache;
import com.nerdsoft.mods.nerdsoftkitchen.client.renderer.CuttingBoardBlockEntityRenderer;
import com.nerdsoft.mods.nerdsoftkitchen.client.renderer.SkilletBlockEntityRenderer;
import com.nerdsoft.mods.nerdsoftkitchen.registry.blockentity.ModBlockEntities;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = NerdSoftKitchen.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientRendererRegistration {

    public static final ModelResourceLocation PROGRESS_TORTILLA_MODEL_LOC =
            ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "block/display/progress_tortilla"));

    public static final ModelResourceLocation FINISHED_TORTILLA_MODEL_LOC =
            ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "block/display/finished_tortilla"));

    private ClientRendererRegistration() {
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CUTTING_BOARD.get(), CuttingBoardBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SKILLET.get(), SkilletBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(PROGRESS_TORTILLA_MODEL_LOC);
        event.register(FINISHED_TORTILLA_MODEL_LOC);
        BlockLodModelCache.registerAdditional(event);
    }

    @SubscribeEvent
    public static void onModelBakingCompleted(ModelEvent.BakingCompleted event) {
        BlockLodModelCache.onBakingCompleted(event);
    }
}
