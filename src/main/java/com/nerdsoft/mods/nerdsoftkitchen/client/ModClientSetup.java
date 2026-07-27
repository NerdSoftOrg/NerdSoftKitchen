package com.nerdsoft.mods.nerdsoftkitchen.client;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.client.renderer.GrillTableBlockEntityRenderer;
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.registry.blockentity.ModBlockEntities;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = NerdSoftKitchen.MOD_ID, value = Dist.CLIENT)
public final class ModClientSetup {

    private ModClientSetup() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(ModItems.IRON_CUP.get(),
                ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "content"), (stack, level, entity,
                                                                                           seed) -> {
                    IronCupContent content = IronCupItem.contentOf(stack);
                    return content == null ? 0.0F : content.modelIndex() + 1;
                }));
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GRILL_TABLE.get(), GrillTableBlockEntityRenderer::new);
    }
}