package com.tba.lite.stubs.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class StubDisplayBlock extends BaseEntityBlock {

    public static final EnumProperty<DisplayAppearance> APPEARANCE =
            EnumProperty.create("appearance", DisplayAppearance.class);

    public static final BooleanProperty STREAMING = BooleanProperty.create("streaming");

    public static final MapCodec<StubDisplayBlock> CODEC = simpleCodec(StubDisplayBlock::new);

    public StubDisplayBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(APPEARANCE, DisplayAppearance.FLOWER_POT)
                .setValue(STREAMING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(APPEARANCE, STREAMING);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StubDisplayBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(STREAMING)) {
            level.setBlock(pos, state.setValue(STREAMING, false), Block.UPDATE_CLIENTS);
        }
    }
}
