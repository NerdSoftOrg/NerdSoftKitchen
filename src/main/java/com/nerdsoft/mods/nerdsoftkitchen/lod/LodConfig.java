package com.nerdsoft.mods.nerdsoftkitchen.lod;

public final class LodConfig {

    public static final int CHECK_INTERVAL_TICKS = 20;

    public static final Entry CUTTING_BOARD = new Entry(new int[]{2});
    public static final Entry GRILL_TABLE = new Entry(new int[]{2});
    public static final Entry WILD_CROP = new Entry(new int[]{2});

    private LodConfig() {
    }

    public record Entry(int[] tierDistancesChunks) {

        public int maxTier() {
            return tierDistancesChunks.length;
        }

        public int distanceChunksForTier(int tier) {
            return tierDistancesChunks[tier];
        }
    }
}