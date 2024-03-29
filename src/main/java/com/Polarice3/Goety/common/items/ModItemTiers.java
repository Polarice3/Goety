package com.Polarice3.Goety.common.items;

import com.Polarice3.Goety.ItemConfig;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum ModItemTiers implements IItemTier {
    FROST(ItemConfig.FrostTierLevel.get(),
            ItemConfig.FrostTierDurability.get(),
            ItemConfig.FrostTierMiningSpeed.get().floatValue(),
            ItemConfig.FrostTierDamage.get().floatValue(),
            ItemConfig.FrostTierEnchantability.get(), () -> {
        return Ingredient.of(ModItems.FROST_INGOT.get());
    }),
    DEATH(4,
            ItemConfig.DeathScytheDurability.get(),
            12.0F,
            ItemConfig.DeathScytheDamage.get().floatValue(),
            ItemConfig.DeathScytheEnchantability.get(), () -> {
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
