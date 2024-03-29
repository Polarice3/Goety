package com.Polarice3.Goety.common.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class TreasurePouchItem extends ItemBase {

    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        int random = worldIn.random.nextInt(4);
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
            case 3:
                enchantment = Enchantments.MENDING;
                break;
        }
        int i = enchantment.getMaxLevel();
        ItemStack itemstack2 = EnchantedBookItem.createForEnchantment(new EnchantmentData(enchantment, i));
        if (!playerIn.addItem(itemstack2)) {
            playerIn.drop(itemstack2, false);
        }
        playerIn.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 1.0F, 1.0F);
        itemstack.shrink(1);
        return ActionResult.pass(playerIn.getItemInHand(handIn));
    }
}
