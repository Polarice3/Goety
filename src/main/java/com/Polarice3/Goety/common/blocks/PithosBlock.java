package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.entities.hostile.SkullLordEntity;
import com.Polarice3.Goety.common.tileentities.PithosTileEntity;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class PithosBlock extends ContainerBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty LOCKED = BlockStateProperties.LOCKED;
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public PithosBlock() {
        super(AbstractBlock.Properties.of(Material.STONE)
                .strength(2.5F, 3600000.0F)
                .sound(SoundType.BONE_BLOCK));
        this.registerDefaultState(this.stateDefinition.any().setValue(OPEN, Boolean.FALSE).setValue(LOCKED, Boolean.TRUE).setValue(TRIGGERED, Boolean.FALSE));
    }

    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        if (pLevel.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            if (!pState.getValue(LOCKED)) {
                TileEntity tileentity = pLevel.getBlockEntity(pPos);
                if (tileentity instanceof PithosTileEntity) {
                    pPlayer.openMenu((PithosTileEntity) tileentity);
                }
            } else {
                if (!pState.getValue(TRIGGERED)){
                    SkullLordEntity skullLord = ModEntityType.SKULL_LORD.get().create(pLevel);
                    if (skullLord != null){
                        skullLord.setBoundOrigin(pPos);
                        skullLord.setPos(pPos.getX(), pPos.getY() + 1.0F, pPos.getZ());
                        pLevel.playSound(null, skullLord.blockPosition() ,SoundEvents.WITHER_SPAWN, SoundCategory.HOSTILE, 1.0F, 1.0F);
                        pLevel.addFreshEntity(skullLord);
                    }
                    pLevel.setBlock(pPos, pState.setValue(TRIGGERED, Boolean.TRUE), 4);
                }
            }

            return ActionResultType.CONSUME;
        }
    }

    public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof IInventory) {
                InventoryHelper.dropContents(pLevel, pPos, (IInventory)tileentity);
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    public void tick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRand) {
        TileEntity tileentity = pLevel.getBlockEntity(pPos);
        if (tileentity instanceof PithosTileEntity) {
            ((PithosTileEntity)tileentity).recheckOpen();
        }
        if (pLevel.getDifficulty() == Difficulty.PEACEFUL){
            if (pState.getValue(LOCKED)){
                pLevel.setBlock(pPos, pState.setValue(LOCKED, Boolean.FALSE).setValue(TRIGGERED, Boolean.TRUE), 4);
            }
        }

    }

    @Nullable
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new PithosTileEntity();
    }

    public BlockRenderType getRenderShape(BlockState pState) {
        return BlockRenderType.MODEL;
    }

    public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (pStack.hasCustomHoverName()) {
            TileEntity tileentity = pLevel.getBlockEntity(pPos);
            if (tileentity instanceof PithosTileEntity) {
                ((PithosTileEntity)tileentity).setCustomName(pStack.getHoverName());
            }
        }

    }

    public float getDestroyProgress(BlockState pState, PlayerEntity pPlayer, IBlockReader pLevel, BlockPos pPos) {
        return !pState.getValue(LOCKED) ? 2.5F : -1.0F;
    }

    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState pBlockState, World pLevel, BlockPos pPos) {
        return Container.getRedstoneSignalFromBlockEntity(pLevel.getBlockEntity(pPos));
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(OPEN, LOCKED, TRIGGERED);
    }

    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        return this.defaultBlockState().setValue(LOCKED, Boolean.FALSE);
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState pState, World pLevel, BlockPos pPos, Random pRand) {
        if (!pState.getValue(TRIGGERED)) {
            double d0 = (double) pPos.getX() + 0.5D;
            double d1 = (double) pPos.getY() + 0.75D;
            double d2 = (double) pPos.getZ() + 0.5D;
            pLevel.addParticle(ParticleTypes.ENCHANT, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
}
