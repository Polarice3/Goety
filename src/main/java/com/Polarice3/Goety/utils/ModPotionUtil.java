package com.Polarice3.Goety.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;

public class ModPotionUtil {

    public static ItemStack setPotion(Potion pPotion) {
        ItemStack stack = new ItemStack(Items.POTION);
        return PotionUtils.setPotion(stack, pPotion);
    }

    public static ItemStack setSplashPotion(Potion pPotion) {
        ItemStack stack = new ItemStack(Items.SPLASH_POTION);
        return PotionUtils.setPotion(stack, pPotion);
    }
}
