package com.Polarice3.Goety.client.armors;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.model.RobeModel;
import com.Polarice3.Goety.utils.SEHelper;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.UUID;

public class WanderBootsArmor extends ArmorItem {
    private static final UUID BOOTS_UUID = UUID.fromString("f46dd333-63a3-4c3b-a5d3-065de1e226cd");
    private static final AttributeModifier BOOTS_SPEED_MODIFIER = new AttributeModifier(BOOTS_UUID, "Wander Boots Speed bonus", 0.15D, AttributeModifier.Operation.MULTIPLY_TOTAL);
    private static final String COOL = "Cool";
    private final Multimap<Attribute, AttributeModifier> bootsModifier;

    public WanderBootsArmor(IArmorMaterial pMaterial, EquipmentSlotType pSlot, Properties pProperties) {
        super(pMaterial, pSlot, pProperties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ARMOR, new AttributeModifier(BOOTS_UUID, "Armor modifier", (double) this.getDefense(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(BOOTS_UUID, "Armor toughness", (double) this.getToughness(), AttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(BOOTS_UUID, "Armor knockback resistance", (double)this.knockbackResistance, AttributeModifier.Operation.ADDITION));
        }
        builder.put(Attributes.MOVEMENT_SPEED, BOOTS_SPEED_MODIFIER);
        this.bootsModifier = builder.build();
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        super.onArmorTick(stack, world, player);
        if (MainConfig.SoulRepair.get()) {
            if (stack.getTag() == null) {
                CompoundNBT compound = stack.getOrCreateTag();
                compound.putInt(COOL, 0);
            }
            if (stack.isDamaged()) {
                if (SEHelper.getSoulsContainer(player)){
                    if (SEHelper.getSoulsAmount(player, MainConfig.DarkArmoredRobeRepairAmount.get())){
                        stack.getTag().putInt(COOL, stack.getTag().getInt(COOL) + 1);
                        if (stack.getTag().getInt(COOL) > 20) {
                            stack.getTag().putInt(COOL, 0);
                            stack.setDamageValue(stack.getDamageValue() - 1);
                            SEHelper.decreaseSouls(player, MainConfig.DarkArmoredRobeRepairAmount.get());
                        }
                    }
                }
            }
        }
    }

    public IArmorMaterial getArmorMaterial(ArmorItem armorItem){
        return armorItem.getMaterial();
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlotType.FEET ? bootsModifier : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
        RobeModel model = new RobeModel(1.0F);
        model.RightLeg.visible = false;
        model.LeftLeg.visible = false;
        model.RightFeet.visible = armorSlot == EquipmentSlotType.FEET;
        model.LeftFeet.visible = armorSlot == EquipmentSlotType.FEET;

        model.young = _default.young;
        model.crouching = _default.crouching;
        model.riding = _default.riding;

        return (A) model;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        if (stack.getItem() instanceof ArmorItem){
            ArmorItem armorItem = (ArmorItem) stack.getItem();
            if (getArmorMaterial(armorItem) == ModArmorMaterial.DARKARMOREDMAGE){
                return "goety:textures/models/armor/darkarmoredrobearmor.png";
            } else if (getArmorMaterial(armorItem) == ModArmorMaterial.ARMOREDNECROTURGE){
                return "goety:textures/models/armor/necroarmoredrobearmor.png";
            } else if (getArmorMaterial(armorItem) == ModArmorMaterial.ARMOREDFELTURGE){
                return "goety:textures/models/armor/felarmoredrobearmor.png";
            } else {
                return "goety:textures/models/armor/darkarmoredrobearmor.png";
            }
        } else {
            return null;
        }
    }

}
