package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.entities.hostile.HuskarlEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.Property;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.common.ToolType;

public class GraveWallBlock extends WallBlock {
    private static final VoxelShape POST_TEST = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
    private static final VoxelShape NORTH_TEST = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 9.0D);
    private static final VoxelShape SOUTH_TEST = Block.box(7.0D, 0.0D, 7.0D, 9.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST_TEST = Block.box(0.0D, 0.0D, 7.0D, 9.0D, 16.0D, 9.0D);
    private static final VoxelShape EAST_TEST = Block.box(7.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D);

    public GraveWallBlock() {
        super(Properties.of(Material.STONE)
                .strength(3.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
        );
    }

    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        IWorldReader iworldreader = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        BlockPos blockpos1 = blockpos.north();
        BlockPos blockpos2 = blockpos.east();
        BlockPos blockpos3 = blockpos.south();
        BlockPos blockpos4 = blockpos.west();
        BlockPos blockpos5 = blockpos.above();
        BlockState blockstate = iworldreader.getBlockState(blockpos1);
        BlockState blockstate1 = iworldreader.getBlockState(blockpos2);
        BlockState blockstate2 = iworldreader.getBlockState(blockpos3);
        BlockState blockstate3 = iworldreader.getBlockState(blockpos4);
        BlockState blockstate4 = iworldreader.getBlockState(blockpos5);
        boolean flag = this.connectsTo(blockstate, blockstate.isFaceSturdy(iworldreader, blockpos1, Direction.SOUTH), Direction.SOUTH);
        boolean flag1 = this.connectsTo(blockstate1, blockstate1.isFaceSturdy(iworldreader, blockpos2, Direction.WEST), Direction.WEST);
        boolean flag2 = this.connectsTo(blockstate2, blockstate2.isFaceSturdy(iworldreader, blockpos3, Direction.NORTH), Direction.NORTH);
        boolean flag3 = this.connectsTo(blockstate3, blockstate3.isFaceSturdy(iworldreader, blockpos4, Direction.EAST), Direction.EAST);
        BlockState blockstate5 = this.defaultBlockState().setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
        return this.updateShape(iworldreader, blockstate5, blockpos5, blockstate4, flag, flag1, flag2, flag3);
    }

    private boolean connectsTo(BlockState pState, boolean pSideSolid, Direction pDirection) {
        Block block = pState.getBlock();
        boolean flag = block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(pState, pDirection);
        return pState.is(BlockTags.WALLS) || !isExceptionForConnection(block) && pSideSolid || block instanceof PaneBlock || flag || pState.getBlock() instanceof GraveWallBlock;
    }

    private BlockState updateShape(IWorldReader pLevel, BlockState p_235626_2_, BlockPos p_235626_3_, BlockState p_235626_4_, boolean p_235626_5_, boolean p_235626_6_, boolean p_235626_7_, boolean p_235626_8_) {
        VoxelShape voxelshape = p_235626_4_.getCollisionShape(pLevel, p_235626_3_).getFaceShape(Direction.DOWN);
        BlockState blockstate = this.updateSides(p_235626_2_, p_235626_5_, p_235626_6_, p_235626_7_, p_235626_8_, voxelshape);
        return blockstate.setValue(UP, this.shouldRaisePost(blockstate, p_235626_4_, voxelshape));
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.getLiquidTicks().scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        if (pFacing == Direction.DOWN) {
            return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        } else {
            return pFacing == Direction.UP ? this.topUpdate(pLevel, pState, pFacingPos, pFacingState) : this.sideUpdate(pLevel, pCurrentPos, pState, pFacingPos, pFacingState, pFacing);
        }
    }

    private BlockState topUpdate(IWorldReader pLevel, BlockState p_235625_2_, BlockPos p_235625_3_, BlockState p_235625_4_) {
        boolean flag = isConnected(p_235625_2_, NORTH_WALL);
        boolean flag1 = isConnected(p_235625_2_, EAST_WALL);
        boolean flag2 = isConnected(p_235625_2_, SOUTH_WALL);
        boolean flag3 = isConnected(p_235625_2_, WEST_WALL);
        return this.updateShape(pLevel, p_235625_2_, p_235625_3_, p_235625_4_, flag, flag1, flag2, flag3);
    }

    private static boolean isConnected(BlockState pState, Property<WallHeight> pHeightProperty) {
        return pState.getValue(pHeightProperty) != WallHeight.NONE;
    }

    private BlockState sideUpdate(IWorldReader pLevel, BlockPos p_235627_2_, BlockState p_235627_3_, BlockPos p_235627_4_, BlockState p_235627_5_, Direction p_235627_6_) {
        Direction direction = p_235627_6_.getOpposite();
        boolean flag = p_235627_6_ == Direction.NORTH ? this.connectsTo(p_235627_5_, p_235627_5_.isFaceSturdy(pLevel, p_235627_4_, direction), direction) : isConnected(p_235627_3_, NORTH_WALL);
        boolean flag1 = p_235627_6_ == Direction.EAST ? this.connectsTo(p_235627_5_, p_235627_5_.isFaceSturdy(pLevel, p_235627_4_, direction), direction) : isConnected(p_235627_3_, EAST_WALL);
        boolean flag2 = p_235627_6_ == Direction.SOUTH ? this.connectsTo(p_235627_5_, p_235627_5_.isFaceSturdy(pLevel, p_235627_4_, direction), direction) : isConnected(p_235627_3_, SOUTH_WALL);
        boolean flag3 = p_235627_6_ == Direction.WEST ? this.connectsTo(p_235627_5_, p_235627_5_.isFaceSturdy(pLevel, p_235627_4_, direction), direction) : isConnected(p_235627_3_, WEST_WALL);
        BlockPos blockpos = p_235627_2_.above();
        BlockState blockstate = pLevel.getBlockState(blockpos);
        return this.updateShape(pLevel, p_235627_3_, blockpos, blockstate, flag, flag1, flag2, flag3);
    }

    private BlockState updateSides(BlockState p_235630_1_, boolean p_235630_2_, boolean p_235630_3_, boolean p_235630_4_, boolean p_235630_5_, VoxelShape p_235630_6_) {
        return p_235630_1_.setValue(NORTH_WALL, this.makeWallState(p_235630_2_, p_235630_6_, NORTH_TEST)).setValue(EAST_WALL, this.makeWallState(p_235630_3_, p_235630_6_, EAST_TEST)).setValue(SOUTH_WALL, this.makeWallState(p_235630_4_, p_235630_6_, SOUTH_TEST)).setValue(WEST_WALL, this.makeWallState(p_235630_5_, p_235630_6_, WEST_TEST));
    }

    private WallHeight makeWallState(boolean p_235633_1_, VoxelShape p_235633_2_, VoxelShape p_235633_3_) {
        if (p_235633_1_) {
            return isCovered(p_235633_2_, p_235633_3_) ? WallHeight.TALL : WallHeight.LOW;
        } else {
            return WallHeight.NONE;
        }
    }

    private boolean shouldRaisePost(BlockState p_235628_1_, BlockState p_235628_2_, VoxelShape p_235628_3_) {
        boolean flag = p_235628_2_.getBlock() instanceof WallBlock && p_235628_2_.getValue(UP);
        if (flag) {
            return true;
        } else {
            WallHeight wallheight = p_235628_1_.getValue(NORTH_WALL);
            WallHeight wallheight1 = p_235628_1_.getValue(SOUTH_WALL);
            WallHeight wallheight2 = p_235628_1_.getValue(EAST_WALL);
            WallHeight wallheight3 = p_235628_1_.getValue(WEST_WALL);
            boolean flag1 = wallheight1 == WallHeight.NONE;
            boolean flag2 = wallheight3 == WallHeight.NONE;
            boolean flag3 = wallheight2 == WallHeight.NONE;
            boolean flag4 = wallheight == WallHeight.NONE;
            boolean flag5 = flag4 && flag1 && flag2 && flag3 || flag4 != flag1 || flag2 != flag3;
            if (flag5) {
                return true;
            } else {
                boolean flag6 = wallheight == WallHeight.TALL && wallheight1 == WallHeight.TALL || wallheight2 == WallHeight.TALL && wallheight3 == WallHeight.TALL;
                if (flag6) {
                    return false;
                } else {
                    return p_235628_2_.getBlock().is(BlockTags.WALL_POST_OVERRIDE) || isCovered(p_235628_3_, POST_TEST);
                }
            }
        }
    }

    private static boolean isCovered(VoxelShape pShape1, VoxelShape pShape2) {
        return !VoxelShapes.joinIsNotEmpty(pShape2, pShape1, IBooleanFunction.ONLY_FIRST);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        if (context instanceof EntitySelectionContext && context.getEntity() instanceof HuskarlEntity){
            HuskarlEntity huskarlEntity = (HuskarlEntity) context.getEntity();
            if (huskarlEntity.isAggressive()){
                return VoxelShapes.empty();
            }
        }
        return super.getCollisionShape(state, world, pos, context);
    }

    @Override
    public boolean isPathfindable(BlockState pState, IBlockReader pLevel, BlockPos pPos, PathType pType) {
        return true;
    }
}
