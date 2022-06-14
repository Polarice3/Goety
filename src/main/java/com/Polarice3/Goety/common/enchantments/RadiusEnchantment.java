package com.Polarice3.Goety.common.enchantments;

import com.Polarice3.Goety.MainConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class RadiusEnchantment extends FocusEnchantments {
    public RadiusEnchantment(Enchantment.Rarity pRarity, EquipmentSlotType... pApplicableSlots) {
        super(pRarity, pApplicableSlots);
    }

    public int getMinCost(int pEnchantmentLevel) {
        return 12 + (pEnchantmentLevel - 1) * 20;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return this.getMinCost(pEnchantmentLevel) + 25;
    }

    public int getMaxLevel() {
        return MainConfig.MaxRadiusLevel.get();
    }
}
