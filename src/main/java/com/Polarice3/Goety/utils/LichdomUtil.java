package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.lichdom.ILichdom;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class LichdomUtil {
    public static boolean isLich(PlayerEntity player){
        ILichdom lichdom = LichdomHelper.getCapability(player);
        return lichdom.getLichdom();
    }

}
