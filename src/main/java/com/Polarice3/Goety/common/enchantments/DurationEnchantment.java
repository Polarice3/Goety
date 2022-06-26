package com.Polarice3.Goety.common.enchantments;

import com.Polarice3.Goety.MainConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class DurationEnchantment extends FocusEnchantments{
    public DurationEnchantment(Enchantment.Rarity pRarity, EquipmentSlotType... pApplicableSlots) {
        super(pRarity, pApplicableSlots);
    }

    public int getMinCost(int pEnchantmentLevel) {
        return 20;
    }

    public int getMaxCost(int pEnchantmentLevel) {
        return 50;
    }

    public int getMaxLevel() {
        return MainConfig.MaxDurationLevel.get();
    }
}
