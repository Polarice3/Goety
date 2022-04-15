package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.init.ModBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class DarkCloudBlock extends Block {
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    public DarkCloudBlock() {
        super(Properties.of(Material.WEB)
                .sound(SoundType.WOOL)
                .randomTicks()
                .instabreak()
                .noCollission()
                .isValidSpawn(ModBlocks::never)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(PERSISTENT, Boolean.FALSE));
    }

    public boolean skipRendering(BlockState pState, BlockState pAdjacentBlockState, Direction pSide) {
        return pAdjacentBlockState.is(this) || super.skipRendering(pState, pAdjacentBlockState, pSide);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }

    @Override
    public int getLightBlock (BlockState state, IBlockReader world, BlockPos pos) {
        return world.getMaxLightLevel();
    }

    public boolean isRandomlyTicking(BlockState pState) {
        return !pState.getValue(PERSISTENT);
    }

    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        if (!pState.getValue(PERSISTENT)) {
            this.checkDeadBlock(pLevel, pPos);
        }
    }

    public void checkDeadBlock(ServerWorld pLevel, BlockPos pPos){
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(pPos.getX(), pPos.getY(), pPos.getZ());

        while(blockpos$mutable.getY() > 0 && !(pLevel.getBlockState(blockpos$mutable).getBlock() instanceof IDeadBlock)) {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockState = pLevel.getBlockState(blockpos$mutable);
        if (!(blockState.getBlock() instanceof IDeadBlock)){
            pLevel.removeBlock(pPos, false);
        }
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(PERSISTENT);
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
