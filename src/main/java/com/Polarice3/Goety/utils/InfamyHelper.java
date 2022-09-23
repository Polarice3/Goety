package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.common.infamy.IInfamy;
import com.Polarice3.Goety.common.infamy.InfamyImp;
import com.Polarice3.Goety.common.infamy.InfamyProvider;
import com.Polarice3.Goety.common.infamy.InfamyUpdatePacket;
import com.Polarice3.Goety.common.network.ModNetwork;
import net.minecraft.entity.Entity;
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

    public static void addInfamy(Entity killer, int infamy){
        if (killer instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) killer;
            if (!GoldTotemFinder.FindTotem(player).isEmpty() || RobeArmorFinder.FindArmor(player)){
                InfamyHelper.increaseInfamy(player, infamy);
                InfamyHelper.sendInfamyUpdatePacket(player);
            }
        }
        if (killer instanceof OwnedEntity){
            OwnedEntity summonedEntity = (OwnedEntity) killer;
            if (summonedEntity.getTrueOwner() != null) {
                if (summonedEntity.getTrueOwner() instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) summonedEntity.getTrueOwner();
                    InfamyHelper.increaseInfamy(player, infamy);
                    InfamyHelper.sendInfamyUpdatePacket(player);
                }
            }
        }
    }
}
