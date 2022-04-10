package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.tileentities.TempWebTileEntity;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TempWebBlock extends ContainerBlock implements net.minecraftforge.common.IForgeShearable{
    public TempWebBlock() {
        super(AbstractBlock.Properties.of
                (Material.WEB)
                .noCollission()
                .requiresCorrectToolForDrops()
                .strength(4.0F)
        );
    }

    public void entityInside(BlockState pState, World pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof LivingEntity){
            LivingEntity livingEntity = (LivingEntity) pEntity;
            if (RobeArmorFinder.FindArachnoArmor(livingEntity)){
                pEntity.makeStuckInBlock(pState, new Vector3d(1.5D, 1.05D, 1.5D));
            } else {
                pEntity.makeStuckInBlock(pState, new Vector3d(0.25D, (double)0.05F, 0.25D));
            }
        } else {
            pEntity.makeStuckInBlock(pState, new Vector3d(0.25D, (double)0.05F, 0.25D));
        }
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new TempWebTileEntity();
    }
}
