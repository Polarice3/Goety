package com.Polarice3.Goety.common.items.curios;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AmuletItem extends Item {
    public AmuletItem() {
        super(new Item.Properties().tab(Goety.TAB).stacksTo(1));
    }

    public boolean isEnchantable(ItemStack pStack) {
        return pStack.getCount() == 1;
    }

    public int getEnchantmentValue() {
        return 15;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        if (stack.getItem() == ModItems.EMERALD_AMULET.get()) {
            return enchantment == Enchantments.SHARPNESS;
        }
        if (stack.getItem() == ModItems.SKULL_AMULET.get()){
            return enchantment == Enchantments.POWER_ARROWS || enchantment == Enchantments.PUNCH_ARROWS || enchantment == Enchantments.FLAMING_ARROWS;
        }
        return false;
    }
}
