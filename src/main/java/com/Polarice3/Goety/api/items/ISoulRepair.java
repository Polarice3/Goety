package com.Polarice3.Goety.api.items;

import com.Polarice3.Goety.utils.ItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public interface ISoulRepair {

    default void repairTick(ItemStack stack, Entity entityIn, boolean isSelected){
        ItemHelper.repairTick(stack, entityIn, isSelected);
    }
}
