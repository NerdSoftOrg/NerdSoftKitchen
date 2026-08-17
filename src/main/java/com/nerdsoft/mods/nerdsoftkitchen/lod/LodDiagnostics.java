package com.nerdsoft.mods.nerdsoftkitchen.lod;

import com.nerdsoft.mods.nerdsoftkitchen.util.NerdSoftKitchenLogger;
import net.minecraft.core.BlockPos;

public final class LodDiagnostics {

    private LodDiagnostics() {
    }

    public static void tierChanged(String blockName, BlockPos pos, int fromTier, int toTier) {
        NerdSoftKitchenLogger.info("LOD {} at {} changed tier {} -> {}", blockName, pos, fromTier, toTier);
    }

    public static void checkCullDistanceAgainstLod(String blockName, double cullDistanceBlocks, LodBlock lodBlock) {
        int maxTier = lodBlock.maxLodTier();
        if (maxTier <= 0) {
            return;
        }
        double lod1DistanceBlocks = lodBlock.tierDistanceChunks(0) * 16.0;
        if (cullDistanceBlocks < lod1DistanceBlocks) {
            NerdSoftKitchenLogger.warn(
                    "LOD {}: cull distance ({} blocks) is closer than its LOD1 threshold ({} blocks) - "
                            + "content will be hidden entirely before it ever reaches the simplified LOD model. "
                            + "Raise the cull distance above the LOD1 threshold.",
                    blockName, cullDistanceBlocks, lod1DistanceBlocks);
        }
    }
}