package com.panzer.mods.dice_and_delish.client.renderer;

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
