package com.nerdsoft.mods.nerdsoftkitchen.lod;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public final class LodProperty {

    public static IntegerProperty create(int maxTier) {
        return IntegerProperty.create("lod", 0, maxTier);
    }

    private LodProperty() {
    }
}