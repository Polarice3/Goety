package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.client.armors.IRobeArmor;
import com.Polarice3.Goety.client.armors.WanderBootsArmor;
import com.Polarice3.Goety.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class RobeArmorFinder {
    public static boolean FindAnySet(LivingEntity livingEntity){
        return livingEntity != null && FindHelm(livingEntity) && FindArmor(livingEntity);
    }

    public static boolean FindHelm(LivingEntity livingEntity){
        return livingEntity != null && livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() instanceof IRobeArmor;
    }

    public static boolean FindArmor(LivingEntity livingEntity){
        return livingEntity != null && livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() instanceof IRobeArmor;
    }

    public static boolean FindLeggings(LivingEntity livingEntity){
        return livingEntity != null && livingEntity.getItemBySlot(EquipmentSlotType.LEGS).getItem() instanceof IRobeArmor;
    }

    public static boolean FindIllusionLeggings(LivingEntity livingEntity){
        return livingEntity != null && livingEntity.getItemBySlot(EquipmentSlotType.LEGS).getItem() == ModItems.ILLUSION_LEGGINGS.get();
    }

    public static boolean FindNecroHelm(LivingEntity livingEntity){
        return livingEntity != null && (livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.NECRO_HELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.NECRO_ARMORED_HELM.get());
    }

    public static boolean FindNecroArmor(LivingEntity livingEntity){
        return livingEntity != null && (livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.NECRO_ROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.NECRO_ARMORED_ROBE.get());
    }

    public static boolean FindNecroSet(LivingEntity livingEntity){
        return FindNecroHelm(livingEntity) && FindNecroArmor(livingEntity);
    }

    public static boolean FindFelHelm(LivingEntity livingEntity){
        return livingEntity != null && (livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.FEL_HELM.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.FEL_ARMORED_HELM.get());
    }

    public static boolean FindFelArmor(LivingEntity livingEntity){
        return livingEntity != null && (livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.FEL_ROBE.get() ||
                livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.FEL_ARMORED_ROBE.get());
    }

    public static boolean FindFelSet(LivingEntity livingEntity){
        return FindFelHelm(livingEntity) && FindFelArmor(livingEntity);
    }

    public static boolean FindIllusionHelm(LivingEntity livingEntity){
        return livingEntity != null && (livingEntity.getItemBySlot(EquipmentSlotType.HEAD).getItem() == ModItems.ILLUSION_HELM.get());
    }

    public static boolean FindIllusionArmor(LivingEntity livingEntity){
        return livingEntity != null && (livingEntity.getItemBySlot(EquipmentSlotType.CHEST).getItem() == ModItems.ILLUSION_ROBE.get());
    }

    public static boolean FindIllusionSet(LivingEntity livingEntity){
        return FindIllusionHelm(livingEntity) && FindIllusionArmor(livingEntity) && FindIllusionLeggings(livingEntity) && FindIllusionBootsofWander(livingEntity);
    }

    public static boolean FindBootsofWander(LivingEntity livingEntity){
        return livingEntity != null && livingEntity.getItemBySlot(EquipmentSlotType.FEET).getItem() instanceof WanderBootsArmor;
    }

    public static boolean FindNecroBootsofWander(LivingEntity livingEntity){
        return livingEntity != null && livingEntity.getItemBySlot(EquipmentSlotType.FEET).getItem() == ModItems.NECRO_BOOTS_OF_WANDER.get();
    }

    public static boolean FindFelBootsofWander(LivingEntity livingEntity){
        return livingEntity != null && livingEntity.getItemBySlot(EquipmentSlotType.FEET).getItem() == ModItems.FEL_BOOTS_OF_WANDER.get();
    }

    public static boolean FindIllusionBootsofWander(LivingEntity livingEntity){
        return livingEntity != null && livingEntity.getItemBySlot(EquipmentSlotType.FEET).getItem() == ModItems.ILLUSION_BOOTS_OF_WANDER.get();
    }
}
