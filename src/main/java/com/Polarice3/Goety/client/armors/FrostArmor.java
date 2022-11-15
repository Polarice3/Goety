package com.Polarice3.Goety.client.armors;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class FrostArmor extends ArmorItem {

    public FrostArmor(EquipmentSlotType slot, Properties builderIn) {
        super(ModArmorMaterial.FROST, slot, builderIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if (slot == EquipmentSlotType.LEGS){
            return "goety:textures/models/armor/frost_layer_2.png";
        } else {
            return "goety:textures/models/armor/frost_layer_1.png";
        }
    }
}
