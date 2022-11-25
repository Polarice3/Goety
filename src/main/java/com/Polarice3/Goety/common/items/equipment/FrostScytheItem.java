package com.Polarice3.Goety.common.items.equipment;

import com.Polarice3.Goety.common.items.ModItemTiers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.item.ItemStack;

public class FrostScytheItem extends DarkScytheItem{

    public FrostScytheItem() {
        super(ModItemTiers.FROST);
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) && !(enchantment instanceof FireAspectEnchantment)
                && !enchantment.getDescriptionId().contains("smelt") && !enchantment.getDescriptionId().contains("heat")
                && !enchantment.getDescriptionId().contains("fire");
    }
}
