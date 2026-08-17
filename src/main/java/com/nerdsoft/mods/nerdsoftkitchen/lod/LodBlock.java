package com.nerdsoft.mods.nerdsoftkitchen.lod;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface LodBlock {

    IntegerProperty lodProperty();

    int tierDistanceChunks(int tier);

    int maxLodTier();

    @SuppressWarnings("unused")
    default boolean useSimpleCollisionShape(double distanceSqr) {
        int maxTier = maxLodTier();
        if (maxTier <= 0) {
            return false;
        }
        double thresholdBlocks = tierDistanceChunks(maxTier - 1) * 16.0;
        return distanceSqr > thresholdBlocks * thresholdBlocks;
    }
}