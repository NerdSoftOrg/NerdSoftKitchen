package com.nerdsoft.mods.nerdsoftkitchen.client.renderer;

@SuppressWarnings("unused")
public final class BillboardConfig {

    private static boolean enabled = true;

    private BillboardConfig() {
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }
}