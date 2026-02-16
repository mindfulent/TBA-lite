package com.tba.lite.stubs.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class StubDisplayBlockRegistration {

    private static final String NAMESPACE = "streamcraft-server";

    private static final ResourceKey<Block> DISPLAY_BLOCK_KEY = ResourceKey.create(
            Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(NAMESPACE, "display_block"));

    public static final Block DISPLAY_BLOCK = Registry.register(
            BuiltInRegistries.BLOCK, DISPLAY_BLOCK_KEY,
            new StubDisplayBlock(BlockBehaviour.Properties.of()
                    .strength(1.5f)
                    .noOcclusion()
                    .lightLevel(state -> state.getValue(StubDisplayBlock.STREAMING) ? 12 : 0)));

    private static final ResourceKey<Item> DISPLAY_ITEM_KEY = ResourceKey.create(
            Registries.ITEM, ResourceLocation.fromNamespaceAndPath(NAMESPACE, "display_block"));

    public static final Item DISPLAY_BLOCK_ITEM = Registry.register(
            BuiltInRegistries.ITEM, DISPLAY_ITEM_KEY,
            new BlockItem(DISPLAY_BLOCK, new Item.Properties()));

    public static final BlockEntityType<StubDisplayBlockEntity> DISPLAY_BLOCK_ENTITY =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(NAMESPACE, "display_block"),
                    BlockEntityType.Builder.of(StubDisplayBlockEntity::new, DISPLAY_BLOCK).build());

    private StubDisplayBlockRegistration() {}

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS)
                .register(entries -> entries.accept(DISPLAY_BLOCK_ITEM));
    }
}
