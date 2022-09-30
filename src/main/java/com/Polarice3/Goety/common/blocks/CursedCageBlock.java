package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.tileentities.CursedCageTileEntity;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeBlock;

import javax.annotation.Nullable;

public class CursedCageBlock extends ContainerBlock implements IForgeBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public CursedCageBlock() {
        super(Properties.of(Material.STONE)
                .strength(5.0F)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
                .noOcclusion()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.FALSE));
    }

    public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        CompoundNBT compoundnbt = pStack.getOrCreateTag();
        if (compoundnbt.contains("BlockEntityTag")) {
            CompoundNBT compoundnbt1 = compoundnbt.getCompound("BlockEntityTag");
            if (compoundnbt1.contains("item")) {
                pLevel.setBlock(pPos, pState.setValue(POWERED, Boolean.TRUE), 2);
            }
        }

    }

    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        if (pState.getValue(POWERED) && pPlayer.getItemInHand(pHand).isEmpty()) {
            this.dropItem(pLevel, pPos);
            pState = pState.setValue(POWERED, Boolean.FALSE);
            pLevel.setBlock(pPos, pState, 2);
            return ActionResultType.sidedSuccess(pLevel.isClientSide);
        } else {
            return ActionResultType.PASS;
        }
    }

    public void setItem(IWorld pLevel, BlockPos pPos, BlockState pState, ItemStack pStack) {
        TileEntity tileentity = pLevel.getBlockEntity(pPos);
        if (tileentity instanceof CursedCageTileEntity) {
            ((CursedCageTileEntity)tileentity).setItem(pStack.copy());
            pLevel.setBlock(pPos, pState.setValue(POWERED, Boolean.TRUE), 2);
        }
    }

    public void dropItem(World pLevel, BlockPos pPos) {
        if (!pLevel.isClientSide) {
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof CursedCageTileEntity) {
                CursedCageTileEntity cageTileEntity = (CursedCageTileEntity)tileentity;
                ItemStack itemstack = cageTileEntity.getItem();
                if (!itemstack.isEmpty()) {
                    pLevel.levelEvent(1010, pPos, 0);
                    cageTileEntity.clearContent();
                    float f = 0.7F;
                    double d0 = (double)(pLevel.random.nextFloat() * 0.7F) + (double)0.15F;
                    double d1 = (double)(pLevel.random.nextFloat() * 0.7F) + (double)0.060000002F + 0.6D;
                    double d2 = (double)(pLevel.random.nextFloat() * 0.7F) + (double)0.15F;
                    ItemStack itemstack1 = itemstack.copy();
                    ItemEntity itementity = new ItemEntity(pLevel, (double)pPos.getX() + d0, (double)pPos.getY() + d1, (double)pPos.getZ() + d2, itemstack1);
                    itementity.setDefaultPickUpDelay();
                    pLevel.addFreshEntity(itementity);
                }
            }
        }
    }

    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            this.dropItem(pLevel, pPos);
            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(POWERED);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new CursedCageTileEntity();
    }
}
