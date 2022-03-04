package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.client.armors.WanderBootsArmor;
import com.Polarice3.Goety.init.ModRegistryHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class RobeArmorFinder {
    public static boolean FindHelm(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistryHandler.DARKHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistryHandler.DARKARMOREDHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistryHandler.NECROHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistryHandler.NECROARMOREDHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistryHandler.ARACHNOHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistryHandler.ARACHNOARMOREDHELM.get();
    }

    public static boolean FindArmor(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistryHandler.DARKROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistryHandler.DARKARMOREDROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistryHandler.NECROROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistryHandler.NECROARMOREDROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistryHandler.ARACHNOROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistryHandler.ARACHNOARMOREDROBE.get();
    }

    public static boolean FindLeggings(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.LEGS).getItem() == ModRegistryHandler.DARKLEGGINGS.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.LEGS).getItem() == ModRegistryHandler.DARKARMOREDLEGGINGS.get();
    }

    public static boolean FindNecroHelm(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistryHandler.NECROHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistryHandler.NECROARMOREDHELM.get();
    }

    public static boolean FindNecroArmor(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistryHandler.NECROROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistryHandler.NECROARMOREDROBE.get();
    }

    public static boolean FindArachnoHelm(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistryHandler.ARACHNOHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModRegistryHandler.ARACHNOARMOREDHELM.get();
    }

    public static boolean FindArachnoArmor(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistryHandler.ARACHNOROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModRegistryHandler.ARACHNOARMOREDROBE.get();
    }

    public static boolean FindBootsofWander(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.FEET).getItem() instanceof WanderBootsArmor;
    }

    public static boolean FindArachnoBootsofWander(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.FEET).getItem() == ModRegistryHandler.ARACHNOBOOTSOFWANDER.get();
    }
}
