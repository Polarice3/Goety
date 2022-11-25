package com.Polarice3.Goety.common.items.equipment;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.items.ModItemTiers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

public class FrostSwordItem extends SwordItem {

    public FrostSwordItem() {
        super(ModItemTiers.FROST, 3, -2.4F, new Properties().tab(Goety.TAB));
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) && !(enchantment instanceof FireAspectEnchantment)
                && !enchantment.getDescriptionId().contains("smelt") && !enchantment.getDescriptionId().contains("heat")
                && !enchantment.getDescriptionId().contains("fire");
    }
}
