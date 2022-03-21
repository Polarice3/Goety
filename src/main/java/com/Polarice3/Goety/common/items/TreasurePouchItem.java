package com.Polarice3.Goety.common.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TreasurePouchItem extends ItemBase {

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        int random = worldIn.random.nextInt(3);
        Enchantment enchantment = null;
        switch (random){
            case 0:
                enchantment = Enchantments.BLOCK_FORTUNE;
                break;
            case 1:
                enchantment = Enchantments.MOB_LOOTING;
                break;
            case 2:
                enchantment = Enchantments.SILK_TOUCH;
                break;
        }
        int i = enchantment.getMaxLevel();
        ItemStack itemstack2 = EnchantedBookItem.createForEnchantment(new EnchantmentData(enchantment, i));
        playerIn.addItem(itemstack2);
        itemstack.shrink(1);
        return ActionResult.pass(playerIn.getItemInHand(handIn));
    }
}
