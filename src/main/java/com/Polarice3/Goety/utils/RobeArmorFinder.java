package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.client.armors.DarkArmoredRobeArmor;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class RobeArmorFinder {
    public static boolean FindAnySet(LivingEntity livingEntity){
        return FindHelm(livingEntity) && FindArmor(livingEntity);
    }

    public static boolean FindHelm(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.DARKHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.DARKARMOREDHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.NECROHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.NECROARMOREDHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.FELHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.FELARMOREDHELM.get();
    }

    public static boolean FindArmor(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.DARKROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.DARKARMOREDROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.NECROROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.NECROARMOREDROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.FELROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.FELARMOREDROBE.get();
    }

    public static boolean FindLeggings(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.LEGS).getItem() == ModItems.DARKLEGGINGS.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.LEGS).getItem() == ModItems.DARKARMOREDLEGGINGS.get();
    }

    public static boolean FindNecroHelm(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.NECROHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.NECROARMOREDHELM.get();
    }

    public static boolean FindNecroArmor(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.NECROROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.NECROARMOREDROBE.get();
    }

    public static boolean FindNecroSet(LivingEntity livingEntity){
        return FindNecroHelm(livingEntity) && FindNecroArmor(livingEntity);
    }

    public static boolean FindFelHelm(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.FELHELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.FELARMOREDHELM.get();
    }

    public static boolean FindFelArmor(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.FELROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.FELARMOREDROBE.get();
    }

    public static boolean FindFelSet(LivingEntity livingEntity){
        return FindFelHelm(livingEntity) && FindFelArmor(livingEntity);
    }

    public static boolean FindBootsofWander(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.FEET).getItem() instanceof DarkArmoredRobeArmor;
    }

    public static boolean FindNecroBootsofWander(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.FEET).getItem() == ModItems.NECROBOOTSOFWANDER.get();
    }

    public static boolean FindFelBootsofWander(LivingEntity livingEntity){
        return livingEntity.getItemBySlot(EquipmentSlotType.FEET).getItem() == ModItems.FELBOOTSOFWANDER.get();
    }
}
