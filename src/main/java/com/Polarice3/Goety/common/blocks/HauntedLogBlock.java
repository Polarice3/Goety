package com.Polarice3.Goety.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class HauntedLogBlock extends RotatedPillarBlock implements IDeadBlock{
    public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public HauntedLogBlock() {
        super(Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .randomTicks()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(ENABLED, Boolean.TRUE).setValue(AXIS, Direction.Axis.Y));
    }

    public boolean isRandomlyTicking(BlockState pState) {
        return pState.getValue(ENABLED);
    }

    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        if (pState.getValue(ENABLED)) {
            this.spreadHaunt(pLevel, pPos, pRandom);
        }
    }
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ENABLED, AXIS);
    }

    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(ENABLED, Boolean.FALSE).setValue(AXIS, pContext.getClickedFace().getAxis());
    }
}
