package com.Polarice3.Goety.client.armors;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public enum ModArmorMaterial implements IArmorMaterial {

    DARKMAGE(Goety.MOD_ID + ":darkmage", 5, new int[] {1, 2, 2, 1}, 25,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> { return Ingredient.of(ModItems.DARKFABRIC.get());}),
    NECROTURGE(Goety.MOD_ID + ":necroturge", 5, new int[] {1, 2, 2, 1}, 25,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> { return Ingredient.of(ModItems.DARKFABRIC.get());}),
    ARACHNOTURGE(Goety.MOD_ID + ":arachnoturge", 5, new int[] {1, 2, 2, 1}, 25,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> { return Ingredient.of(ModItems.DARKFABRIC.get());}),
    DARKARMOREDMAGE(Goety.MOD_ID + ":darkarmoredmage", 15, new int[]{2, 5, 6, 2}, 25,
            SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {return Ingredient.of(ModItems.CURSED_INGOT.get());
    }),
    ARMOREDNECROTURGE(Goety.MOD_ID + ":armorednecroturge", 15, new int[]{2, 5, 6, 2}, 25,
            SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {return Ingredient.of(ModItems.CURSED_INGOT.get());
    }),
    ARMOREDARACHNOTURGE(Goety.MOD_ID + ":armoredarachnoturge", 15, new int[]{2, 5, 6, 2}, 25,
            SoundEvents.ARMOR_EQUIP_IRON, 0.0F, 0.0F, () -> {return Ingredient.of(ModItems.CURSED_INGOT.get());
    });

    private static final int[] MAX_DAMAGE_ARRAY = new int[] {13, 15, 16, 11};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantibility;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairMaterial;

    ModArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantibility, SoundEvent soundEvent,
                     float toughness, float knockbackResistance, Supplier<Ingredient> repairMaterial){
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantibility = enchantibility;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = repairMaterial;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType slotIn) {
        return this.damageReductionAmountArray[slotIn.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantibility;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
