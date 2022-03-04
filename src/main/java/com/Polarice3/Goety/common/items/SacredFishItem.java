package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class SacredFishItem extends Item {
    public SacredFishItem() {
        super(new Properties().tab(Goety.TAB)
                .stacksTo(1)
                .food(new Food.Builder()
                        .nutrition(5)
                        .saturationMod(0.6F)
                        .alwaysEat()
                        .build())
        );
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        if (entityLiving instanceof PlayerEntity) {
            ((PlayerEntity)entityLiving).getCooldowns().addCooldown(this, 120);
        }
        return new ItemStack(this);
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.EAT;
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_EAT;
    }
}
