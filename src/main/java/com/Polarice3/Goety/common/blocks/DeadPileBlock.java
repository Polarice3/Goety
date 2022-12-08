package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.utils.BlockFinder;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;

public class DeadPileBlock extends Block implements IDeadBlock{
    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
    protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[]{VoxelShapes.empty(), net.minecraft.block.Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), net.minecraft.block.Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), net.minecraft.block.Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), net.minecraft.block.Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), net.minecraft.block.Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), net.minecraft.block.Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), net.minecraft.block.Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), net.minecraft.block.Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public DeadPileBlock() {
        super(AbstractBlock.Properties.of(Material.TOP_SNOW, MaterialColor.COLOR_PURPLE)
                .randomTicks()
                .strength(0.1F)
                .harvestLevel(0)
                .harvestTool(ToolType.SHOVEL)
                .sound(SoundType.SAND));
        this.registerDefaultState(this.stateDefinition.any().setValue(LAYERS, Integer.valueOf(1)));
    }

    public boolean isPathfindable(BlockState pState, IBlockReader pLevel, BlockPos pPos, PathType pType) {
        switch(pType) {
            case LAND:
                return pState.getValue(LAYERS) < 5;
            case WATER:
                return false;
            case AIR:
                return false;
            default:
                return false;
        }
    }

    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE_BY_LAYER[pState.getValue(LAYERS)];
    }

    public VoxelShape getCollisionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE_BY_LAYER[pState.getValue(LAYERS) - 1];
    }

    public VoxelShape getBlockSupportShape(BlockState pState, IBlockReader pReader, BlockPos pPos) {
        return SHAPE_BY_LAYER[pState.getValue(LAYERS)];
    }

    public VoxelShape getVisualShape(BlockState pState, IBlockReader pReader, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE_BY_LAYER[pState.getValue(LAYERS)];
    }

    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }

    public boolean canSurvive(BlockState pState, IWorldReader pLevel, BlockPos pPos) {
        BlockState blockstate = pLevel.getBlockState(pPos.below());
        if (!blockstate.is(Blocks.ICE) && !blockstate.is(Blocks.PACKED_ICE) && !blockstate.is(Blocks.BARRIER)) {
            if (!blockstate.is(Blocks.HONEY_BLOCK) && !blockstate.is(Blocks.SOUL_SAND)) {
                return Block.isFaceFull(blockstate.getCollisionShape(pLevel, pPos.below()), Direction.UP) || blockstate.getBlock() == this && blockstate.getValue(LAYERS) == 8;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        BlockState blockstate = pLevel.getBlockState(pPos.below());
        int i = pState.getValue(LAYERS);
        if (i == 8){
            pLevel.setBlockAndUpdate(pPos, ModBlocks.DEAD_SAND.get().defaultBlockState());
        }
        if (BlockFinder.isInRain(pLevel, pPos) && !(blockstate.getBlock() instanceof IDeadBlock)){
            if (i > 1){
                pLevel.setBlockAndUpdate(pPos, pState.setValue(LAYERS, i - 1));
            } else {
                pLevel.removeBlock(pPos, false);
            }
        } else {
            spreadSand(pLevel, pPos, pRandom);
        }
    }

    public boolean canBeReplaced(BlockState pState, BlockItemUseContext pUseContext) {
        int i = pState.getValue(LAYERS);
        if (pUseContext.getItemInHand().getItem() == this.asItem() && i < 8) {
            if (pUseContext.replacingClickedOnBlock()) {
                return pUseContext.getClickedFace() == Direction.UP;
            } else {
                return true;
            }
        } else {
            return i == 1;
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos());
        if (blockstate.is(this)) {
            int i = blockstate.getValue(LAYERS);
            return blockstate.setValue(LAYERS, Integer.valueOf(Math.min(8, i + 1)));
        } else {
            return super.getStateForPlacement(pContext);
        }
    }

    protected void createBlockStateDefinition(StateContainer.Builder<net.minecraft.block.Block, BlockState> pBuilder) {
        pBuilder.add(LAYERS);
    }
}
