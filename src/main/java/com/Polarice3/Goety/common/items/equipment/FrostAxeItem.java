package com.Polarice3.Goety.common.items.equipment;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.items.ModItemTiers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FrostAxeItem extends AxeItem {

    public FrostAxeItem() {
        super(ModItemTiers.FROST, 6.0F, -3.1F, (new Item.Properties()).tab(Goety.TAB));
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) && !(enchantment instanceof FireAspectEnchantment)
                && !enchantment.getDescriptionId().contains("smelt") && !enchantment.getDescriptionId().contains("heat")
                && !enchantment.getDescriptionId().contains("fire");
    }
}
