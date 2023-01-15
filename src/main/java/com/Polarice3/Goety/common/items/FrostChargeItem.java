package com.Polarice3.Goety.common.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FrostChargeItem extends ItemBase{

    public ActionResultType useOn(ItemUseContext pContext) {
        World world = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        boolean flag = false;
        if (CampfireBlock.isLitCampfire(blockstate)) {
            this.playSound(world, blockpos);
            world.setBlockAndUpdate(blockpos, blockstate.setValue(CampfireBlock.LIT, Boolean.FALSE));
            flag = true;
        } else {
            blockpos = blockpos.relative(pContext.getClickedFace());
            BlockState snow = Blocks.SNOW.defaultBlockState();
            if (snow.canSurvive(world, blockpos)) {
                this.playSound(world, blockpos);
                world.setBlockAndUpdate(blockpos, snow);
                flag = true;
            }
        }

        if (flag) {
            pContext.getItemInHand().shrink(1);
            return ActionResultType.sidedSuccess(world.isClientSide);
        } else {
            return ActionResultType.FAIL;
        }
    }

    private void playSound(World pLevel, BlockPos pPos) {
        pLevel.playSound((PlayerEntity)null, pPos, SoundEvents.SNOW_PLACE, SoundCategory.BLOCKS, 1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
    }
}
