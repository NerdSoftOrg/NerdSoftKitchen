package com.panzer.mods.dice_and_delish.client.renderer;

public final class LodConfig {

    public static final double CULL_DISTANCE_BLOCKS = 32.0;
    public static final double CULL_DISTANCE_SQR = CULL_DISTANCE_BLOCKS * CULL_DISTANCE_BLOCKS;

    public static final double REFERENCE_DISTANCE_BLOCKS = 32.0;

    public static final double ITEM_RADIUS_BLOCKS = 0.3;
    public static final double ITEM_RADIUS_SQR = ITEM_RADIUS_BLOCKS * ITEM_RADIUS_BLOCKS;
    public static final double ITEM_LOD_COVERAGE_THRESHOLD =
            ScreenSpaceLod.thresholdForDistance(ITEM_RADIUS_SQR, REFERENCE_DISTANCE_BLOCKS);

    public static final int MAX_RENDERED_ITEMS_HARD_CAP = 24;

    private LodConfig() {
    }
}
