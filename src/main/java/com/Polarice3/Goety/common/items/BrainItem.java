package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class BrainItem extends Item {
    public BrainItem() {
        super(new Properties()
                .tab(Goety.TAB)
                .food(new Food.Builder()
                        .nutrition(4)
                        .saturationMod(0.1F)
                        .alwaysEat()
                        .meat()
                        .effect(new EffectInstance(Effects.POISON, 300), 1.0F)
                        .effect(new EffectInstance(Effects.HUNGER, 300), 1.0F)
                        .build()
                )
        );
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        super.finishUsingItem(stack, worldIn, entityLiving);
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            player.giveExperiencePoints(20);
        }
        return stack;
    }

    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.EAT;
    }

}
