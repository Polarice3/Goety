package com.Polarice3.Goety.client.armors;

import com.Polarice3.Goety.ItemConfig;
import com.Polarice3.Goety.api.items.IRobeArmor;
import com.Polarice3.Goety.client.model.RobeModel;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class DarkRobeArmor extends ArmorItem implements IRobeArmor {

    public DarkRobeArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        super.onArmorTick(stack, world, player);
        if (ItemConfig.SoulRepair.get()) {
            if (stack.isDamaged()){
                if (SEHelper.getSoulsContainer(player)){
                    if (SEHelper.getSoulsAmount(player, 0)){
                        if (player.tickCount % 20 == 0) {
                            stack.setDamageValue(stack.getDamageValue() - 1);
                            SEHelper.decreaseSouls(player, 1);
                        }
                    }
                }
            }
        }
    }

    public boolean isFoil(ItemStack pStack) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        RobeModel model = new RobeModel(1.0F);
        model.hat.visible = armorSlot == EquipmentSlotType.HEAD;
        model.Body.visible = armorSlot == EquipmentSlotType.CHEST;
        model.RightArm.visible = armorSlot == EquipmentSlotType.CHEST;
        model.LeftArm.visible = armorSlot == EquipmentSlotType.CHEST;
        model.Pants.visible = armorSlot == EquipmentSlotType.LEGS;
        model.RightLeg.visible = armorSlot == EquipmentSlotType.LEGS;
        model.LeftLeg.visible = armorSlot == EquipmentSlotType.LEGS;
        model.RightFeet.visible = false;
        model.LeftFeet.visible = false;

        model.young = _default.young;
        model.crouching = _default.crouching;
        model.riding = _default.riding;
        model.rightArmPose = _default.rightArmPose;
        model.leftArmPose = _default.leftArmPose;

        return (A) model;
    }

    public IArmorMaterial getArmorMaterial(ArmorItem armorItem){
        return armorItem.getMaterial();
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if (stack.getItem() instanceof ArmorItem){
            ArmorItem armorItem = (ArmorItem) stack.getItem();
            if (getArmorMaterial(armorItem) == ModArmorMaterial.DARKMAGE){
                return "goety:textures/models/armor/darkrobearmor.png";
            } else if (getArmorMaterial(armorItem) == ModArmorMaterial.NECROTURGE){
                return "goety:textures/models/armor/necrorobearmor.png";
            } else if (getArmorMaterial(armorItem) == ModArmorMaterial.FELTURGE){
                return "goety:textures/models/armor/felrobearmor.png";
            } else {
                return "goety:textures/models/armor/darkrobearmor.png";
            }
        } else {
            return null;
        }
    }
}
