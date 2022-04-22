package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.lichdom.ILichdom;
import com.Polarice3.Goety.common.lichdom.LichImp;
import com.Polarice3.Goety.common.lichdom.LichProvider;
import com.Polarice3.Goety.common.lichdom.LichUpdatePacket;
import com.Polarice3.Goety.common.network.ModNetwork;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class LichdomHelper {
    public static ILichdom getCapability(PlayerEntity player) {
        return player.getCapability(LichProvider.CAPABILITY).orElse(new LichImp());
    }

    public static boolean isLich(PlayerEntity player) {
        return getCapability(player).getLichdom();
    }

    public static void setLichdom(PlayerEntity player, boolean lichdom) {
        getCapability(player).setLichdom(lichdom);
    }

    public static BlockPos getArcaBlock(PlayerEntity player){
        return getCapability(player).getArcaBlock();
    }

    public static void setArcaBlock(PlayerEntity player, BlockPos blockPos){
        getCapability(player).setArcaBlock(blockPos);
    }

    public static void sendLichUpdatePacket(PlayerEntity player) {
        ModNetwork.sendTo(player, new LichUpdatePacket(player));
    }
}
