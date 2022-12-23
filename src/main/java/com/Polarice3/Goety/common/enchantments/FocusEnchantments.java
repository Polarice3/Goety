package com.Polarice3.Goety.common.enchantments;

import com.Polarice3.Goety.common.items.magic.MagicFocusItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public abstract class FocusEnchantments extends Enchantment {
    public FocusEnchantments(Enchantment.Rarity pRarity, EquipmentSlotType... pApplicableSlots) {
        super(pRarity, ModEnchantments.FOCUS, pApplicableSlots);
    }

    public boolean isAllowedOnBooks() {
        return false;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return super.canApplyAtEnchantingTable(stack) && stack.getItem() instanceof MagicFocusItem;
    }
}
