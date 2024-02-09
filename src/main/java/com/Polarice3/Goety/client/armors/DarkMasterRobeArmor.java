package com.Polarice3.Goety.client.armors;

import com.Polarice3.Goety.ItemConfig;
import com.Polarice3.Goety.api.items.IRobeArmor;
import com.Polarice3.Goety.client.model.MasterRobeModel;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.SEHelper;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class DarkMasterRobeArmor extends ArmorItem implements IRobeArmor {

    public DarkMasterRobeArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Properties builderIn) {
        super(materialIn, slot, builderIn);
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        super.onArmorTick(stack, world, player);
        if (ItemConfig.SoulRepair.get()) {
            if (stack.isDamaged()){
                if (SEHelper.getSoulsContainer(player)){
                    if (SEHelper.getSoulsAmount(player, ItemConfig.DarkArmoredRobeRepairAmount.get())){
                        if (player.tickCount % 20 == 0) {
                            stack.setDamageValue(stack.getDamageValue() - 1);
                            SEHelper.decreaseSouls(player, ItemConfig.DarkArmoredRobeRepairAmount.get());
                        }
                    }
                }
            }
        }
    }

    public boolean isFoil(ItemStack pStack) {
        return false;
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return stack.getItem() == ModItems.DARK_MASTER_LEGGINGS.get();
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        MasterRobeModel model = new MasterRobeModel(1.0F);
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

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
        return super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return "goety:textures/models/armor/darkmasterrobe.png";
    }
}
