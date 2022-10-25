package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class SoulFangTotemItem extends BlockItem {

    public SoulFangTotemItem(Block blockIn) {
        super(blockIn, new Properties().tab(Goety.TAB));
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return stack.getCount() == 1 && (enchantment == ModEnchantments.POTENCY.get()
                || enchantment == ModEnchantments.RANGE.get()
                || enchantment == ModEnchantments.BURNING.get()
                || enchantment == ModEnchantments.SOULEATER.get());
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 50;
    }
}
