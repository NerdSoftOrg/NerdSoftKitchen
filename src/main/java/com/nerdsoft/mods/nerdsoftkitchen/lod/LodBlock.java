package com.nerdsoft.mods.nerdsoftkitchen.lod;

public interface LodBlock {

    int tierDistanceChunks(int tier);

    int maxLodTier();

    default boolean useSimpleCollisionShape(double distanceSqr) {
        int maxTier = maxLodTier();
        if (maxTier <= 0) {
            return false;
        }
        double thresholdBlocks = tierDistanceChunks(maxTier - 1) * 16.0;
        return distanceSqr > thresholdBlocks * thresholdBlocks;
    }
}