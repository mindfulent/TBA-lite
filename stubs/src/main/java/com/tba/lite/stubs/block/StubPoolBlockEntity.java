package com.tba.lite.stubs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class StubPoolBlockEntity extends BlockEntity {

    public StubPoolBlockEntity(BlockPos pos, BlockState state) {
        super(StubPoolBlockRegistration.POOL_BLOCK_ENTITY, pos, state);
    }
}
