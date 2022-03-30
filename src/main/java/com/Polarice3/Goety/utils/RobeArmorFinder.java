package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.client.armors.WanderBootsArmor;
import com.Polarice3.Goety.init.ModRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class RobeArmorFinder {
    public static boolean FindHelm(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistry.DARKHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistry.DARKARMOREDHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistry.NECROHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistry.NECROARMOREDHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistry.ARACHNOHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistry.ARACHNOARMOREDHELM.get();
    }

    public static boolean FindArmor(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistry.DARKROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistry.DARKARMOREDROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistry.NECROROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistry.NECROARMOREDROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistry.ARACHNOROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistry.ARACHNOARMOREDROBE.get();
    }

    public static boolean FindLeggings(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.LEGS).getItem() == ModRegistry.DARKLEGGINGS.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.LEGS).getItem() == ModRegistry.DARKARMOREDLEGGINGS.get();
    }

    public static boolean FindNecroHelm(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistry.NECROHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistry.NECROARMOREDHELM.get();
    }

    public static boolean FindNecroArmor(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistry.NECROROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistry.NECROARMOREDROBE.get();
    }

    public static boolean FindNecroSet(LivingEntity livingEntity){
        return FindNecroHelm(livingEntity) && FindNecroArmor(livingEntity);
    }

    public static boolean FindArachnoHelm(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistry.ARACHNOHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistry.ARACHNOARMOREDHELM.get();
    }

    public static boolean FindArachnoArmor(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistry.ARACHNOROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistry.ARACHNOARMOREDROBE.get();
    }

    public static boolean FindArachnoSet(LivingEntity livingEntity){
        return FindArachnoHelm(livingEntity) && FindArachnoArmor(livingEntity);
    }

    public static boolean FindBootsofWander(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.FEET).getItem() instanceof WanderBootsArmor;
    }

    public static boolean FindArachnoBootsofWander(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.FEET).getItem() == ModRegistry.ARACHNOBOOTSOFWANDER.get();
    }
}
