package com.Polarice3.Goety.client.armors;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class IllusionBootsArmor extends WanderBootsArmor{
    public IllusionBootsArmor(IArmorMaterial pMaterial, EquipmentSlotType pSlot, Properties pProperties) {
        super(pMaterial, pSlot, pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if (stack.getItem() instanceof ArmorItem){
            if (!entity.isInvisible()){
                return "goety:textures/models/armor/illusionrobearmor.png";
            } else {
                return "goety:textures/models/armor/invisiblerobearmor.png";
            }
        } else {
            return null;
        }
    }
}
