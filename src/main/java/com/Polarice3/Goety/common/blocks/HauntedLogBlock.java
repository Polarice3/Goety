package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.utils.BlockFinder;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class HauntedLogBlock extends RotatedPillarBlock {
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
            if (pLevel.isLoaded(pPos)) {
                if (MainConfig.DeadSandSpread.get()) {
                    BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1, pRandom.nextInt(3) - 1);
                    BlockState blockState = pLevel.getBlockState(blockpos);
                    if (BlockFinder.NotDeadSandImmune(blockState)) {
                        if (blockState.is(BlockTags.LOGS)) {
                            pLevel.destroyBlock(blockpos, false);
                            pLevel.setBlockAndUpdate(blockpos, ModBlocks.HAUNTED_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, blockState.getValue(RotatedPillarBlock.AXIS)));
                        }
                        if (blockState.getBlock() instanceof LeavesBlock) {
                            pLevel.removeBlock(blockpos, false);
                        }
                    }
                }
            }
        }
    }
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ENABLED, AXIS);
    }

    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(ENABLED, Boolean.FALSE).setValue(AXIS, pContext.getClickedFace().getAxis());
    }
}
