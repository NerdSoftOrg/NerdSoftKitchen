package com.nerdsoft.mods.nerdsoftkitchen.crop;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum TripleBlockHalf implements StringRepresentable {
    LOWER("lower"),
    MIDDLE("middle"),
    UPPER("upper");

    private final String name;

    TripleBlockHalf(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
