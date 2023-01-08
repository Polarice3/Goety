package com.Polarice3.Goety.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class RottenPumpkinBlock extends HorizontalBlock {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public RottenPumpkinBlock() {
        super(Properties.of(Material.VEGETABLE, MaterialColor.TERRACOTTA_GREEN).strength(1.0F).sound(SoundType.WOOD));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }
}
