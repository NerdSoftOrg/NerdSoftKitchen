package com.nerdsoft.mods.nerdsoftkitchen.client;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.client.renderer.GrillTableBlockEntityRenderer;
import com.nerdsoft.mods.nerdsoftkitchen.client.renderer.SkilletBlockEntityRenderer;
//? if <1.21.2 {
import com.nerdsoft.mods.nerdsoftkitchen.compat.jei.client.JeiCategorySorter;
//?}
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.lod.LodDiagnostics;
import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
import com.nerdsoft.mods.nerdsoftkitchen.registry.blockentity.ModBlockEntities;
import com.nerdsoft.mods.nerdsoftkitchen.registry.item.ModItems;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GrassColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.Map;

// Compatibility for 1.21
@SuppressWarnings("removal")
@EventBusSubscriber(modid = NerdSoftKitchen.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ModClientSetup {

    private ModClientSetup() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(
                ModItems.IRON_CUP.get(),
                ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "content"),
                (stack, level, entity, seed) -> {
                    IronCupContent content = IronCupItem.contentOf(stack);
                    return content == null ? 0.0F : content.modelIndex() + 1;
                }
        ));

        LodDiagnostics.checkCullDistanceAgainstLod("CuttingBoard", 48.0, ModBlocks.CUTTING_BOARD.get());
        LodDiagnostics.checkCullDistanceAgainstLod("GrillTable", 48.0, ModBlocks.GRILL_TABLE.get());
    }

    //? if <1.21.2 {
    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(JeiCategorySorter::forceCategoriesOrder);
    }
    //?}

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GRILL_TABLE.get(), GrillTableBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SKILLET.get(), SkilletBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, level, pos, tintIndex) ->
                        level != null && pos != null
                                ? BiomeColors.getAverageGrassColor(level, pos)
                                : GrassColor.getDefaultColor(),
                ModBlocks.WILD_PURPLE_ONION.get(),
                ModBlocks.WILD_STRAWBERRY.get(),
                ModBlocks.WILD_TOMATO.get(),
                ModBlocks.WILD_LETTUCE.get()
        );
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        Map<Item, KnifeColors> knifeColors = Map.of(
                ModItems.STONE_KNIFE.get(),    new KnifeColors(0xFFB1AFAD, 0xFFB1AFAD),
                ModItems.IRON_KNIFE.get(),     new KnifeColors(0xFFECF5F5, 0xFFECF5F5),
                ModItems.GOLDEN_KNIFE.get(),   new KnifeColors(0xFFFAF25E, 0xFFFFFCEB),
                ModItems.DIAMOND_KNIFE.get(),  new KnifeColors(0xFF72F7E4, 0xFFC9FFF8),
                ModItems.OBSIDIAN_KNIFE.get(), new KnifeColors(0xFF865FBF, 0xFFD2BCF7)
        );

        event.register((stack, tintIndex) -> {
            KnifeColors colors = knifeColors.get(stack.getItem());
            if (colors == null) return -1;

            return switch (tintIndex) {
                case 1 -> colors.blade();
                case 2 -> colors.highlight();
                default -> -1; // layer0 (no tint)
            };
        }, knifeColors.keySet().toArray(new Item[0]));
    }

    private record KnifeColors(int blade, int highlight) {}
}
