package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.blocks.tiles.TallSkullTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.IArmorVanishable;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class TallSkullBlock extends ContainerBlock implements IArmorVanishable {
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 10.0D, 12.0D);

    public TallSkullBlock() {
        super(Properties.of(Material.DECORATION)
                .strength(1.0F)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0));
    }

    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE;
    }

    public VoxelShape getOcclusionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos) {
        return VoxelShapes.empty();
    }

    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(ROTATION, Integer.valueOf(MathHelper.floor((double)(pContext.getRotation() * 16.0F / 360.0F) + 0.5D) & 15));
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(ROTATION, Integer.valueOf(pRotation.rotate(pState.getValue(ROTATION), 16)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.setValue(ROTATION, Integer.valueOf(pMirror.mirror(pState.getValue(ROTATION), 16)));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ROTATION);
    }

    public boolean isPathfindable(BlockState pState, IBlockReader pLevel, BlockPos pPos, PathType pType) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new TallSkullTileEntity();
    }
}
