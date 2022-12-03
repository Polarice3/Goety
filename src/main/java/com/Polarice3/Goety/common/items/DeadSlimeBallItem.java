package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.projectiles.DeadSlimeBallEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class DeadSlimeBallItem extends Item {
    public DeadSlimeBallItem() {
        super(new Properties().tab(Goety.TAB));
    }

    public ActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pLevel.playSound((PlayerEntity)null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!pLevel.isClientSide) {
            DeadSlimeBallEntity snowballentity = new DeadSlimeBallEntity(pLevel, pPlayer);
            snowballentity.setItem(itemstack);
            snowballentity.shootFromRotation(pPlayer, pPlayer.xRot, pPlayer.yRot, 0.0F, 1.5F, 1.0F);
            pLevel.addFreshEntity(snowballentity);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.abilities.instabuild) {
            itemstack.shrink(1);
        }

        return ActionResult.sidedSuccess(itemstack, pLevel.isClientSide());
    }
}
