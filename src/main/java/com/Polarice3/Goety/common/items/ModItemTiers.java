package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum ModItemTiers implements IItemTier {
    FROST(MainConfig.FrostTierLevel.get(),
            MainConfig.FrostTierDurability.get(),
            MainConfig.FrostTierMiningSpeed.get().floatValue(),
            MainConfig.FrostTierDamage.get().floatValue(),
            MainConfig.FrostTierEnchantability.get(), () -> {
        return Ingredient.of(ModItems.FROST_INGOT.get());
    }),
    DEATH(4,
            MainConfig.DeathScytheDurability.get(),
            12.0F,
            MainConfig.DeathScytheDamage.get().floatValue(),
            MainConfig.DeathScytheEnchantability.get(), () -> {
        return Ingredient.of(Items.BONE);
    });

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyValue<Ingredient> repairIngredient;

    ModItemTiers(int pLevel, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.level = pLevel;
        this.uses = pUses;
        this.speed = pSpeed;
        this.damage = pDamage;
        this.enchantmentValue = pEnchantmentValue;
        this.repairIngredient = new LazyValue<>(pRepairIngredient);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

}
