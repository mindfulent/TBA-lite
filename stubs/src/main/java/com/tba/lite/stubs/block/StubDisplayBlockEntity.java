package com.tba.lite.stubs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class StubDisplayBlockEntity extends BlockEntity {

    public StubDisplayBlockEntity(BlockPos pos, BlockState state) {
        super(StubDisplayBlockRegistration.DISPLAY_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        // Reset streaming state on chunk load (server-side) to prevent stale STREAMING=true.
        // Uses scheduleTick to defer until after chunk loading completes — avoids C2ME deadlock.
        if (!level.isClientSide()) {
            BlockState state = getBlockState();
            if (state.getValue(StubDisplayBlock.STREAMING)) {
                level.scheduleTick(worldPosition, state.getBlock(), 1);
            }
        }
    }
}
