package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.capabilities.lichdom.ILichdom;
import com.Polarice3.Goety.common.capabilities.lichdom.LichImp;
import com.Polarice3.Goety.common.capabilities.lichdom.LichProvider;
import com.Polarice3.Goety.common.capabilities.lichdom.LichUpdatePacket;
import com.Polarice3.Goety.common.network.ModNetwork;
import net.minecraft.entity.player.PlayerEntity;

public class LichdomHelper {
    public static ILichdom getCapability(PlayerEntity player) {
        return player.getCapability(LichProvider.CAPABILITY).orElse(new LichImp());
    }

    public static boolean isLich(PlayerEntity player) {
        return getCapability(player).getLichdom();
    }

    public static void sendLichUpdatePacket(PlayerEntity player) {
        ModNetwork.sendTo(player, new LichUpdatePacket(player));
    }
}
