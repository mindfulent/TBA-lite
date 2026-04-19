package com.tba.lite.stubs.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class StubPoolBlockRegistration {

    private static final String NAMESPACE = "shapecraft";
    private static final int POOL_SIZE = 64;

    public static final Block[] POOL_BLOCKS = new Block[POOL_SIZE];
    public static final BlockItem[] POOL_ITEMS = new BlockItem[POOL_SIZE];
    public static final BlockEntityType<StubPoolBlockEntity> POOL_BLOCK_ENTITY;

    static {
        for (int i = 0; i < POOL_SIZE; i++) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(NAMESPACE, "custom_" + i);
            StubPoolBlock block = new StubPoolBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .strength(1.5f, 6.0f)
                    .dynamicShape()
                    .noOcclusion());
            POOL_BLOCKS[i] = Registry.register(BuiltInRegistries.BLOCK, id, block);
            POOL_ITEMS[i] = Registry.register(BuiltInRegistries.ITEM, id,
                    new BlockItem(POOL_BLOCKS[i], new Item.Properties()));
        }

        POOL_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(NAMESPACE, "pool_block_entity"),
                FabricBlockEntityTypeBuilder.create(StubPoolBlockEntity::new, POOL_BLOCKS).build());
    }

    private StubPoolBlockRegistration() {}

    public static void initialize() {
        // Static initializer runs on class load
    }
}
