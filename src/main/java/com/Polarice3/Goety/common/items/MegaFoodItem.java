package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.utils.ItemHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class MegaFoodItem extends Item {

    public MegaFoodItem(int foodLevel, float saturation) {
        super(new Item.Properties()
                .tab(Goety.TAB)
                .durability(8)
                .food(new Food.Builder()
                        .nutrition(foodLevel)
                        .saturationMod(saturation)
                        .alwaysEat()
                        .build())
        );
    }

    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        ItemHelper.hurtAndRemove(stack, 1, entityLiving);
        worldIn.playSound((PlayerEntity)null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), entityLiving.getEatingSound(stack), SoundCategory.NEUTRAL, 1.0F, 1.0F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.4F);
        return stack;
    }

}
