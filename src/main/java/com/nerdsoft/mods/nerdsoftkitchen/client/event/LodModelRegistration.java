package com.nerdsoft.mods.nerdsoftkitchen.client.event;

import com.nerdsoft.mods.nerdsoftkitchen.NerdSoftKitchen;
import com.nerdsoft.mods.nerdsoftkitchen.lod.LodBlock;
import com.nerdsoft.mods.nerdsoftkitchen.lod.LodModelSet;
import com.nerdsoft.mods.nerdsoftkitchen.registry.block.ModBlocks;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = NerdSoftKitchen.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class LodModelRegistration {

    private LodModelRegistration() {
    }

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        registerTiers(event, "cutting_board", resolveLodBlock(ModBlocks.CUTTING_BOARD.get()));
        // Additional LodBlock implementer as theyre added
        // registerTiers(event, "block", resolveLodBlock(ModBlocks.BLOCK.get()));
    }

    @SuppressWarnings("SameParameterValue")
    private static void registerTiers(ModelEvent.RegisterAdditional event, String baseName, LodBlock lodBlock) {
        for (int tier = 1; tier <= lodBlock.maxLodTier(); tier++) {
            String name = LodModelSet.modelName(baseName, tier, LodModelSet.DEFAULT_SUFFIX);

            event.register(ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(NerdSoftKitchen.MOD_ID, "block/" + name)
            ));
        }
    }

    private static LodBlock resolveLodBlock(Object block) {
        if (block instanceof LodBlock lodBlock) {
            return lodBlock;
        }
        throw new IllegalArgumentException(block + " is not a LodBlock");
    }
}