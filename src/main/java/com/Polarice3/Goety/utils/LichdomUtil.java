package com.Polarice3.Goety.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class LichdomUtil {
    public static boolean isLich(PlayerEntity player){
        CompoundNBT playerData = player.getPersistentData();
        CompoundNBT data = playerData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        return data.getBoolean("goety:isLich");
    }

}
