package com.Polarice3.Goety.common.enchantments;

import com.Polarice3.Goety.SpellConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;

public class SoulEaterEnchantment extends Enchantment {
    public SoulEaterEnchantment(Rarity rarityIn, EnchantmentType enchantmentType, EquipmentSlotType... slots) {
        super(rarityIn, enchantmentType, slots);
    }

    public int getMinCost(int enchantmentLevel) {
        return 5 + (enchantmentLevel - 1) * 9;
    }

    public int getMaxCost(int enchantmentLevel) {
        return this.getMinCost(enchantmentLevel) + 15;
    }

    public int getMaxLevel() {
        return SpellConfig.MaxSoulEaterLevel.get();
    }

    public boolean checkCompatibility(Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && !(pEnch instanceof SoulEaterEnchantment);
    }
}
