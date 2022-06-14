package com.Polarice3.Goety.common.enchantments;

import com.Polarice3.Goety.MainConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class PotencyEnchantment extends FocusEnchantments {
    public PotencyEnchantment(Enchantment.Rarity pRarity, EquipmentSlotType... pApplicableSlots) {
        super(pRarity, pApplicableSlots);
    }

    public int getMinCost(int pEnchantmentLevel) {
        return 1 + (pEnchantmentLevel - 1) * 10;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return this.getMinCost(pEnchantmentLevel) + 15;
    }

    public int getMaxLevel() {
        return MainConfig.MaxPotencyLevel.get();
    }

    public boolean checkCompatibility(Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && pEnch != ModEnchantments.ABSORB.get();
    }

}
