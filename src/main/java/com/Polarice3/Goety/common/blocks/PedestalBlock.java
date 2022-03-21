package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.tileentities.PedestalTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeBlock;

import javax.annotation.Nullable;

public class PedestalBlock extends ContainerBlock implements IForgeBlock {
    public static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 13.0D, 13.0D, 13.0D);

    public PedestalBlock() {
        super(Properties.of(Material.STONE)
                .strength(3.0F, 9.0F)
                .sound(SoundType.STONE)
                .harvestLevel(0)
                .harvestTool(ToolType.PICKAXE)
                .noOcclusion()
        );
    }

    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        TileEntity tileentity = pLevel.getBlockEntity(pPos);
        if (tileentity instanceof PedestalTileEntity) {
            return ((PedestalTileEntity) tileentity).onUse(pPlayer, pHand);
        }

        return ActionResultType.PASS;
    }

    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            this.dropItem(pLevel, pPos);
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    public void dropItem(World pLevel, BlockPos pPos) {
        if (!pLevel.isClientSide) {
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof PedestalTileEntity) {
                PedestalTileEntity pedestalTileEntity = (PedestalTileEntity)tileentity;
                ItemStack itemstack = pedestalTileEntity.getItem();
                if (!itemstack.isEmpty()) {
                    pLevel.levelEvent(1010, pPos, 0);
                    pedestalTileEntity.clearContent();
                    ItemStack itemstack1 = itemstack.copy();
                    ItemEntity itementity = new ItemEntity(pLevel, pPos.getX(), pPos.getY(), pPos.getZ(), itemstack1);
                    itementity.setDefaultPickUpDelay();
                    pLevel.addFreshEntity(itementity);
                }
            }
        }
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public VoxelShape getOcclusionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos) {
        return SHAPE;
    }

    public VoxelShape getCollisionShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE;
    }

    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPE;
    }

    public boolean isPathfindable(BlockState pState, IBlockReader pLevel, BlockPos pPos, PathType pType) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new PedestalTileEntity();
    }
}
