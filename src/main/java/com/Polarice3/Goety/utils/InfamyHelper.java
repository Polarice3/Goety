package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.infamy.IInfamy;
import com.Polarice3.Goety.common.infamy.InfamyImp;
import com.Polarice3.Goety.common.infamy.InfamyProvider;
import com.Polarice3.Goety.common.infamy.InfamyUpdatePacket;
import com.Polarice3.Goety.common.network.ModNetwork;
import net.minecraft.entity.player.PlayerEntity;

public class InfamyHelper {

    public static IInfamy getCapability(PlayerEntity player) {
        return player.getCapability(InfamyProvider.CAPABILITY).orElse(new InfamyImp());
    }

    public static int getInfamy(PlayerEntity player) {
        return getCapability(player).getInfamy();
    }

    public static void increaseInfamy(PlayerEntity player, int amount) {
        getCapability(player).increaseInfamy(amount);
    }

    public static void sendInfamyUpdatePacket(PlayerEntity player) {
        ModNetwork.sendTo(player, new InfamyUpdatePacket(player));
    }
}
