package com.nerdsoft.mods.nerdsoftkitchen.lod;

public final class LodModelSet {

    public static final String DEFAULT_SUFFIX = "_lod";

    private LodModelSet() {
    }

    @SuppressWarnings("unused")
    public static String modelName(String baseName, int tier) {
        return modelName(baseName, tier, DEFAULT_SUFFIX);
    }

    public static String modelName(String baseName, int tier, String suffix) {
        return tier == 0 ? baseName : baseName + suffix + tier;
    }

    @SuppressWarnings("unused")
    public static String[] modelNames(String baseName, int maxTier) {
        return modelNames(baseName, maxTier, DEFAULT_SUFFIX);
    }

    public static String[] modelNames(String baseName, int maxTier, String suffix) {
        String[] names = new String[maxTier + 1];
        for (int tier = 0; tier <= maxTier; tier++) {
            names[tier] = modelName(baseName, tier, suffix);
        }
        return names;
    }
}