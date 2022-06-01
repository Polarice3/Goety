package com.Polarice3.Goety.common.enchantments;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.LootBonusEnchantment;
import net.minecraft.inventory.EquipmentSlotType;

public class LootingEnchantment extends LootBonusEnchantment {
    public LootingEnchantment(Rarity p_i46726_1_, EnchantmentType p_i46726_2_, EquipmentSlotType... p_i46726_3_) {
        super(p_i46726_1_, p_i46726_2_, p_i46726_3_);
    }

    public boolean isAllowedOnBooks() {
        return false;
    }

}
