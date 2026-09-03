package com.panzer.mods.dice_and_delish.client.event;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.client.renderer.CuttingBoardBlockEntityRenderer;
import com.panzer.mods.dice_and_delish.client.renderer.SkilletBlockEntityRenderer;
import com.panzer.mods.dice_and_delish.registry.blockentity.ModBlockEntities;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = DiceAndDelish.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientRendererRegistration {

    public static final ModelResourceLocation PROGRESS_TORTILLA_MODEL_LOC =
            ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, "block/display/progress_tortilla"));

    public static final ModelResourceLocation FINISHED_TORTILLA_MODEL_LOC =
            ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, "block/display/finished_tortilla"));

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
    }
}
