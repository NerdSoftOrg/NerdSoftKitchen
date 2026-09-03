package com.panzer.mods.dice_and_delish.client;

import com.panzer.mods.dice_and_delish.DiceAndDelish;
import com.panzer.mods.dice_and_delish.client.renderer.FlatItemModelCache;
import com.panzer.mods.dice_and_delish.client.renderer.GrillTableBlockEntityRenderer;
//? if <1.21.2 {
import com.panzer.mods.dice_and_delish.compat.jei.client.JeiCategorySorter;
//?}
import com.panzer.mods.dice_and_delish.item.IronCupItem;
import com.panzer.mods.dice_and_delish.item.component.IronCupContent;
import com.panzer.mods.dice_and_delish.registry.block.ModBlocks;
import com.panzer.mods.dice_and_delish.registry.blockentity.ModBlockEntities;
import com.panzer.mods.dice_and_delish.registry.item.ModItems;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GrassColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

// Compatibility for 1.21
@SuppressWarnings("removal")
@EventBusSubscriber(modid = DiceAndDelish.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class ModClientSetup {

    private static final int DEFAULT_IRON = 0xFFECF5F5;

    private ModClientSetup() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(
                ModItems.IRON_CUP.get(),
                ResourceLocation.fromNamespaceAndPath(DiceAndDelish.MOD_ID, "content"),
                (stack, level, entity, seed) -> {
                    IronCupContent content = IronCupItem.contentOf(stack);
                    return content == null ? 0.0F : content.modelIndex() + 1;
                }
        ));
    }

    //? if <1.21.2 {
    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(JeiCategorySorter::forceCategoriesOrder);
    }
    //?}

    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new SimplePreparableReloadListener<Void>() {
            @Override
            protected @NotNull Void prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
                return null;
            }

            @Override
            protected void apply(@NotNull Void unused, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
                FlatItemModelCache.clear();
            }
        });
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GRILL_TABLE.get(), GrillTableBlockEntityRenderer::new);
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
                ModItems.STONE_KNIFE.get(), KnifeColors.same(0xFFB1AFAD),
                ModItems.IRON_KNIFE.get(), KnifeColors.of(DEFAULT_IRON),
                ModItems.GOLDEN_KNIFE.get(), KnifeColors.of(0xFFFAEB0F),
                ModItems.DIAMOND_KNIFE.get(), new KnifeColors(0xFF72F7E4, 0xFFC9FFF8),
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

    private record KnifeColors(int blade, int highlight) {
        public static KnifeColors of(int blade) {
            return new KnifeColors(blade, DEFAULT_IRON);
        }

        public static KnifeColors same(int blade) {
            return new KnifeColors(blade, blade);
        }
    }
}
