package com.Polarice3.Goety.common.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeBlock;

public abstract class TotemHeadBlock extends ContainerBlock implements IForgeBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public TotemHeadBlock() {
        super(Properties.of(Material.STONE)
                .strength(5.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.FALSE));
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader level, BlockPos pos, int fortune, int silktouch) {
        return 15 + RANDOM.nextInt(15) + RANDOM.nextInt(15);
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
