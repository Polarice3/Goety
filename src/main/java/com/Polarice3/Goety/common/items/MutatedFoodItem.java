package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.utils.EffectsUtil;
import com.Polarice3.Goety.utils.ItemHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MutatedFoodItem extends Item {
    public int foodLevel;
    public float saturation;

    public MutatedFoodItem(int foodLevel, float saturation) {
        super(new Item.Properties()
                .tab(Goety.TAB)
                .durability(8)
        );
        this.foodLevel = foodLevel;
        this.saturation = saturation;
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        int random = worldIn.random.nextInt(16);
        if (random == 0) {
            EffectInstance effectinstance1 = entityLiving.getEffect(Effects.HUNGER);
            if (effectinstance1 == null) {
                EffectInstance effectinstance = new EffectInstance(Effects.HUNGER, 600, 0);
                entityLiving.addEffect(effectinstance);
            } else {
                EffectsUtil.amplifyEffect(entityLiving, Effects.HUNGER, 600);
            }
        }
        PlayerEntity playerentity = (PlayerEntity) entityLiving;
        playerentity.getFoodData().eat(foodLevel, saturation);
        ItemHelper.hurtAndRemove(stack, 1, playerentity);
        return stack;
    }

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.startUsingItem(handIn);
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        return ActionResult.consume(itemstack);
    }


    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.EAT;
    }
}
