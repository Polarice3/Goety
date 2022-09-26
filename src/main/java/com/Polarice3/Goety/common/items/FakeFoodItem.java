package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.utils.ItemHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class FakeFoodItem extends Item {
    public int foodLevel;
    public float saturation;

    public FakeFoodItem(int foodLevel, float saturation) {
        super(new Item.Properties()
                .tab(Goety.TAB)
                .durability(8)
        );
        this.foodLevel = foodLevel;
        this.saturation = saturation;
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        PlayerEntity playerentity = (PlayerEntity) entityLiving;
        playerentity.getFoodData().eat(foodLevel, saturation);
        playerentity.playSound(SoundEvents.PLAYER_BURP, 1.0F, 1.0F);
        ItemHelper.hurtAndRemove(stack, 1, playerentity);
        return stack;
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (playerIn.getFoodData().needsFood()) {
            playerIn.startUsingItem(handIn);
            return ActionResult.consume(itemstack);
        } else {
            return ActionResult.fail(itemstack);
        }
    }


    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.EAT;
    }
}
