package com.Polarice3.Goety.common.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class AbsorbEnchantment extends FocusEnchantments {
    public AbsorbEnchantment(Rarity pRarity, EquipmentSlotType... pApplicableSlots) {
        super(pRarity, pApplicableSlots);
    }

    public int getMinCost(int pEnchantmentLevel) {
        return pEnchantmentLevel * 25;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return this.getMinCost(pEnchantmentLevel) + 50;
    }

    public int getMaxLevel() {
        return 1;
    }

    public boolean checkCompatibility(Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && pEnch != ModEnchantments.POTENCY.get();
    }
}
