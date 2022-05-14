package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.tileentities.ArcaTileEntity;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeBlock;

import javax.annotation.Nullable;

public class ArcaBlock extends ContainerBlock implements IForgeBlock {

    public ArcaBlock() {
        super(AbstractBlock.Properties.of(Material.STONE)
                .strength(100.0F, 2400.0F)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
                .noOcclusion()
        );
    }

    public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        TileEntity tileentity = pLevel.getBlockEntity(pPos);
        if (pPlacer instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) pPlacer;
            ISoulEnergy soulEnergy = SEHelper.getCapability(player);
            soulEnergy.setArcaBlock(pPos);
            soulEnergy.setArcaBlockDimension(pLevel.dimension());
            soulEnergy.setSEActive(true);
            if (tileentity instanceof ArcaTileEntity){
                ArcaTileEntity arcaTile = (ArcaTileEntity) tileentity;
                arcaTile.setOwnerId(pPlacer.getUUID());
            }
            if (!pLevel.isClientSide()){
                SEHelper.sendSEUpdatePacket(player);
            }
        }
    }

    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        if (pState.hasTileEntity()) {
            TileEntity tileEntity = pLevel.getBlockEntity(pPos);
            if (pPlayer.getMainHandItem().isEmpty()) {
                if (tileEntity instanceof ArcaTileEntity) {
                    ArcaTileEntity arcaTileEntity = (ArcaTileEntity) tileEntity;
                    if (arcaTileEntity.getPlayer() == pPlayer) {
                        ISoulEnergy soulEnergy = SEHelper.getCapability(arcaTileEntity.getPlayer());
                        if (!pPlayer.isCrouching()){
                            if (soulEnergy.getArcaBlock() == null){
                                soulEnergy.setArcaBlock(arcaTileEntity.getBlockPos());
                                soulEnergy.setArcaBlockDimension(pLevel.dimension());
                                if (!soulEnergy.getSEActive()){
                                    soulEnergy.setSEActive(true);
                                }
                                SEHelper.sendSEUpdatePacket(arcaTileEntity.getPlayer());
                                return ActionResultType.SUCCESS;
                            }
                        } else {
                            pLevel.destroyBlock(pPos, true);
                            return ActionResultType.sidedSuccess(pLevel.isClientSide);
                        }
                    }
                }
            }
        }
        return ActionResultType.PASS;
    }


    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            TileEntity tileEntity = pLevel.getBlockEntity(pPos);
            if (tileEntity instanceof ArcaTileEntity){
                ArcaTileEntity arcaTileEntity = (ArcaTileEntity) tileEntity;
                if (arcaTileEntity.getPlayer() != null){
                    ISoulEnergy soulEnergy = SEHelper.getCapability(arcaTileEntity.getPlayer());
                    if (soulEnergy.getArcaBlock() == arcaTileEntity.getBlockPos()) {
                        soulEnergy.setSEActive(false);
                        soulEnergy.setArcaBlock(null);
                        soulEnergy.setArcaBlockDimension(null);
                        SEHelper.sendSEUpdatePacket(arcaTileEntity.getPlayer());
                    }
                }
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new ArcaTileEntity();
    }
}
