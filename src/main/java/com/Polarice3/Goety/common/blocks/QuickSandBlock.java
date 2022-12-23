package com.Polarice3.Goety.common.blocks;

import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.common.fluid.ModFluids;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class QuickSandBlock extends FlowingFluidBlock implements IDeadBlock {

    public QuickSandBlock() {
        super(() -> ModFluids.QUICKSAND_SOURCE.get(),
                AbstractBlock.Properties.of(Material.LAVA, MaterialColor.COLOR_MAGENTA)
                        .noCollission()
                        .randomTicks()
                        .strength(100.0F)
                        .noDrops());
    }

    public void entityInside(BlockState pState, World pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) pEntity;
            boolean flag = false;
            if (!(livingEntity instanceof IDeadMob)) {
                if (livingEntity instanceof PlayerEntity){
                    PlayerEntity player = (PlayerEntity) livingEntity;
                    if (!player.isSpectator()){
                        flag = !player.abilities.flying;
                    }
                } else {
                    flag = true;
                }
                if (flag){
                    livingEntity.setOnGround(true);
                    double jump = pState.getFluidState().isSource() ? 0.25D : 1.0D;
                    livingEntity.makeStuckInBlock(pState, new Vector3d(0.25D, jump, 0.25D));
                    if (!pLevel.isClientSide) {
                        if (MobUtil.validEntity(livingEntity)) {
                            if (!RobeArmorFinder.FindNecroBootsofWander(livingEntity)) {
                                if (livingEntity.tickCount % 100 == 0 && pLevel.random.nextFloat() <= 0.25F) {
                                    livingEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 600));
                                }
                            }
                        }
                    }
                }
            }
        }
        super.entityInside(pState, pLevel, pPos, pEntity);
    }

    public void onPlace(BlockState pState, World pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (this.shouldSpreadLiquid(pLevel, pPos)) {
            pLevel.getLiquidTicks().scheduleTick(pPos, pState.getFluidState().getType(), this.getFluid().getTickDelay(pLevel));
        }

    }

    public void neighborChanged(BlockState pState, World pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (this.shouldSpreadLiquid(pLevel, pPos)) {
            pLevel.getLiquidTicks().scheduleTick(pPos, pState.getFluidState().getType(), this.getFluid().getTickDelay(pLevel));
        }

    }

    private boolean shouldSpreadLiquid(World pLevel, BlockPos pPos) {
        for(Direction direction : Direction.values()) {
            if (direction != Direction.DOWN) {
                BlockPos blockpos = pPos.relative(direction);
                if (pLevel.getFluidState(blockpos).is(FluidTags.LAVA)) {
                    Block block = pLevel.getFluidState(pPos).isSource() ? ModBlocks.DEAD_SANDSTONE.get() : ModBlocks.DEAD_SAND.get();
                    pLevel.setBlockAndUpdate(pPos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(pLevel, pPos, pPos, block.defaultBlockState()));
                    this.fizz(pLevel, pPos);
                    return false;
                }
            }
        }

        return true;
    }

    private void fizz(IWorld pLevel, BlockPos pPos) {
        pLevel.levelEvent(1501, pPos, 0);
    }

    public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, @Nullable MobEntity entity) {
        return entity instanceof IDeadMob ? null : PathNodeType.DAMAGE_OTHER;
    }

}
