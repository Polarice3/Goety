package com.Polarice3.Goety.common.items;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallOrFloorItem;

import javax.annotation.Nullable;

public class TallSkullItem extends WallOrFloorItem {
    public TallSkullItem(Block pStandingBlock, Block pWallBlock, Properties pProperties) {
        super(pStandingBlock, pWallBlock, pProperties);
    }


    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
        return armorType == EquipmentSlotType.HEAD;
    }

    @Override
    @Nullable
    public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
        return EquipmentSlotType.HEAD;
    }
}
