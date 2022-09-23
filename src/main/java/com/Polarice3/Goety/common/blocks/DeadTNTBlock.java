package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.entities.projectiles.DeadTNTEntity;
import com.Polarice3.Goety.utils.DeadSandExplosion;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class DeadTNTBlock extends Block {
    public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;

    public DeadTNTBlock() {
        super(AbstractBlock.Properties.of(Material.EXPLOSIVE)
                .instabreak()
                .sound(SoundType.GRASS));
        this.registerDefaultState(this.defaultBlockState().setValue(UNSTABLE, Boolean.FALSE));
    }

    public void catchFire(BlockState state, World pLevel, BlockPos pPos, @Nullable net.minecraft.util.Direction face, @Nullable LivingEntity pEntity) {
        if (!pLevel.isClientSide) {
            DeadTNTEntity tntentity = new DeadTNTEntity(pLevel, (double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, pEntity);
            pLevel.addFreshEntity(tntentity);
            pLevel.playSound((PlayerEntity)null, tntentity.getX(), tntentity.getY(), tntentity.getZ(), SoundEvents.TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    public void onPlace(BlockState pState, World pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock())) {
            if (pLevel.hasNeighborSignal(pPos)) {
                catchFire(pState, pLevel, pPos, null, null);
                pLevel.removeBlock(pPos, false);
            }

        }
    }

    public void neighborChanged(BlockState pState, World pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (pLevel.hasNeighborSignal(pPos)) {
            catchFire(pState, pLevel, pPos, null, null);
            pLevel.removeBlock(pPos, false);
        }

    }

    public void wasExploded(World pLevel, BlockPos pPos, Explosion pExplosion) {
        if (!pLevel.isClientSide) {
            DeadTNTEntity tntentity = new DeadTNTEntity(pLevel, (double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, pExplosion.getSourceMob());
            tntentity.setFuse((short)(pLevel.random.nextInt(tntentity.getLife() / 4) + tntentity.getLife() / 8));
            pLevel.addFreshEntity(tntentity);
        }
    }

    public static void wasDeadExploded(World pLevel, BlockPos pPos, DeadSandExplosion pExplosion) {
        if (!pLevel.isClientSide) {
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3);
            DeadTNTEntity tntentity = new DeadTNTEntity(pLevel, (double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, pExplosion.getSourceMob());
            tntentity.setFuse((short)(pLevel.random.nextInt(tntentity.getLife() / 4) + tntentity.getLife() / 8));
            pLevel.addFreshEntity(tntentity);
        }
    }

    public void playerWillDestroy(World pLevel, BlockPos pPos, BlockState pState, PlayerEntity pPlayer) {
        if (!pLevel.isClientSide() && !pPlayer.isCreative() && pState.getValue(UNSTABLE)) {
            catchFire(pState, pLevel, pPos, null, null);
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        Item item = itemstack.getItem();
        if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        } else {
            catchFire(pState, pLevel, pPos, pHit.getDirection(), pPlayer);
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 11);
            if (!pPlayer.isCreative()) {
                if (item == Items.FLINT_AND_STEEL) {
                    itemstack.hurtAndBreak(1, pPlayer, (p_220287_1_) -> {
                        p_220287_1_.broadcastBreakEvent(pHand);
                    });
                } else {
                    itemstack.shrink(1);
                }
            }

            return ActionResultType.sidedSuccess(pLevel.isClientSide);
        }
    }

    public void onProjectileHit(World pLevel, BlockState pState, BlockRayTraceResult pHit, ProjectileEntity pProjectile) {
        if (!pLevel.isClientSide) {
            Entity entity = pProjectile.getOwner();
            if (pProjectile.isOnFire()) {
                BlockPos blockpos = pHit.getBlockPos();
                catchFire(pState, pLevel, blockpos, null, entity instanceof LivingEntity ? (LivingEntity)entity : null);
                pLevel.removeBlock(blockpos, false);
            }
        }

    }

    public boolean dropFromExplosion(Explosion pExplosion) {
        return false;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(UNSTABLE);
    }
}
