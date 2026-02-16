package com.tba.lite.stubs.block;

import net.minecraft.util.StringRepresentable;

public enum DisplayAppearance implements StringRepresentable {
    FLOWER_POT("flower_pot", "Flower Pot"),
    COBBLESTONE("cobblestone", "Cobblestone"),
    STONE("stone", "Stone"),
    OAK_PLANKS("oak_planks", "Oak Planks"),
    SPRUCE_PLANKS("spruce_planks", "Spruce Planks"),
    DARK_OAK_PLANKS("dark_oak_planks", "Dark Oak Planks"),
    IRON_BLOCK("iron_block", "Iron Block"),
    BOOKSHELF("bookshelf", "Bookshelf"),
    BRICKS("bricks", "Bricks"),
    STONE_BRICKS("stone_bricks", "Stone Bricks"),
    SMOOTH_STONE("smooth_stone", "Smooth Stone");

    public static final StringRepresentable.EnumCodec<DisplayAppearance> CODEC =
            StringRepresentable.fromEnum(DisplayAppearance::values);

    private final String serializedName;
    private final String displayName;

    DisplayAppearance(String serializedName, String displayName) {
        this.serializedName = serializedName;
        this.displayName = displayName;
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
