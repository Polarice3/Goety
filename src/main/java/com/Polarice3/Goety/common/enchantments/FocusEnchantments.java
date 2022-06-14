package com.Polarice3.Goety.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public abstract class FocusEnchantments extends Enchantment {
    public FocusEnchantments(Enchantment.Rarity pRarity, EquipmentSlotType... pApplicableSlots) {
        super(pRarity, ModEnchantments.FOCUS, pApplicableSlots);
    }

    public boolean isAllowedOnBooks() {
        return false;
    }
}
