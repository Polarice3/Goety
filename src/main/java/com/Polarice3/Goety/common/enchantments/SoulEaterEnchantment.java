package com.Polarice3.Goety.common.enchantments;

import com.Polarice3.Goety.MainConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class SoulEaterEnchantment extends Enchantment {
    public SoulEaterEnchantment(Rarity rarityIn, EquipmentSlotType... slots) {
        super(rarityIn, EnchantmentType.WEAPON, slots);
    }

    public int getMinCost(int enchantmentLevel) {
        return 5 + (enchantmentLevel - 1) * 9;
    }

    public int getMaxCost(int enchantmentLevel) {
        return this.getMinCost(enchantmentLevel) + 15;
    }

    public int getMaxLevel() {
        return MainConfig.MaxSoulEaterLevel.get();
    }
}
