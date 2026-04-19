package com.tba.lite.stubs.block;

import net.minecraft.util.StringRepresentable;

public enum StubBlockHalf implements StringRepresentable {
    LOWER("lower"),
    UPPER("upper");

    private final String name;

    StubBlockHalf(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
