package com.nerdsoft.mods.nerdsoftkitchen.client;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.client.renderer.CuttingBoardBlockEntityRenderer;
import com.nerdsoft.mods.nerdsoftkitchen.client.renderer.GrillTableBlockEntityRenderer;
import com.nerdsoft.mods.nerdsoftkitchen.client.renderer.SkilletBlockEntityRenderer;
//? if <1.21.2 {
import com.nerdsoft.mods.nerdsoftkitchen.compat.jei.client.JeiCategorySorter;
//?}
import com.nerdsoft.mods.nerdsoftkitchen.item.IronCupItem;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
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
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

// Compatibility for 1.21
@SuppressWarnings("removal")
@EventBusSubscriber(modid = NerdSoftKitchen.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
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
        //? if <1.21.2 {
        JeiCategorySorter.forceGrillAfterCampfire();
        //?}
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GRILL_TABLE.get(), GrillTableBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CUTTING_BOARD.get(), CuttingBoardBlockEntityRenderer::new);
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
    @SuppressWarnings("InvariantValue")
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
                    if (tintIndex == 1) { // layer1 = blade
                        Item item = stack.getItem();
                        if (item == ModItems.STONE_KNIFE.get()) return 0xFFB1AFAD;    // Gray
                        // A prepare for copper age knife
                        //? if > 1.21.8 {
                        // if (item == ModItems.COPPER_KNIFE.get()) return 0xFF9C4E31;   // Orange
                        //?}
                        if (item == ModItems.IRON_KNIFE.get()) return 0xFFECF5F5;     // White
                        if (item == ModItems.GOLD_KNIFE.get()) return 0xFFFFF540;     // Yellow
                        if (item == ModItems.DIAMOND_KNIFE.get()) return 0xFF72F7E4;  // Cyan
                        if (item == ModItems.OBSIDIAN_KNIFE.get()) return 0xFF865FBF; // Purple
                        if (item == ModItems.NETHERITE_KNIFE.get()) return 0xFF252433;// Black
                    }
                    if (tintIndex == 2) { // layer1 = highlight
                        Item item = stack.getItem();
                        if (item == ModItems.STONE_KNIFE.get()) return 0xFFB1AFAD;    // Gray
                        // A prepare for copper age knife
                        //? if > 1.21.8 {
                        // if (item == ModItems.COPPER_KNIFE.get()) return 0xFF9C4E31;   // Orange
                        //?}
                        if (item == ModItems.IRON_KNIFE.get()) return 0xFFECF5F5;     // White
                        if (item == ModItems.GOLD_KNIFE.get()) return 0xFFFFFCEB;     // Yellow
                        if (item == ModItems.DIAMOND_KNIFE.get()) return 0xFFC9FFF8;  // Cyan
                        if (item == ModItems.OBSIDIAN_KNIFE.get()) return 0xFFD2BCF7; // Purple
                        if (item == ModItems.NETHERITE_KNIFE.get()) return 0xFFA6979F;// Black
                    }
                    return -1; // layer0 = handle without tint
                },
                ModItems.STONE_KNIFE.get(),
                //? if > 1.21.8 {
                // ModItems.COPPER_KNIFE.get(),
                //?}
                ModItems.IRON_KNIFE.get(),
                ModItems.GOLD_KNIFE.get(),
                ModItems.DIAMOND_KNIFE.get(),
                ModItems.OBSIDIAN_KNIFE.get(),
                ModItems.NETHERITE_KNIFE.get());
    }
}