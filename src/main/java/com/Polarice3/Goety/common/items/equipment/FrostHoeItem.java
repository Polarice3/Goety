package com.Polarice3.Goety.common.items.equipment;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.items.ModItemTiers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FrostHoeItem extends HoeItem {

    public FrostHoeItem() {
        super(ModItemTiers.FROST, -2, -1.0F, (new Item.Properties()).tab(Goety.TAB));
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) && !(enchantment instanceof FireAspectEnchantment)
                && !enchantment.getDescriptionId().contains("smelt") && !enchantment.getDescriptionId().contains("heat");
    }
}
