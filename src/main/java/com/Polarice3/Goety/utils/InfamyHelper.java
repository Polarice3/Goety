package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.capabilities.infamy.IInfamy;
import com.Polarice3.Goety.common.capabilities.infamy.InfamyImp;
import com.Polarice3.Goety.common.capabilities.infamy.InfamyProvider;
import com.Polarice3.Goety.common.capabilities.infamy.InfamyUpdatePacket;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.ConquillagerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.EnviokerEntity;
import com.Polarice3.Goety.common.entities.hostile.illagers.InquillagerEntity;
import com.Polarice3.Goety.common.entities.neutral.IOwned;
import com.Polarice3.Goety.common.network.ModNetwork;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.IllusionerEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
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

    public static int getInfamyGiven(LivingEntity killed){
        if (killed instanceof PillagerEntity) {
            return MainConfig.PillagerInfamy.get();
        } else if (killed instanceof VindicatorEntity) {
            return MainConfig.VindicatorInfamy.get();
        } else if (killed instanceof EvokerEntity) {
            return MainConfig.EvokerInfamy.get();
        } else if (killed instanceof IllusionerEntity) {
            return MainConfig.IllusionerInfamy.get();
        } else if (killed instanceof EnviokerEntity) {
            return MainConfig.EnviokerInfamy.get();
        } else if (killed instanceof InquillagerEntity) {
            return MainConfig.InquillagerInfamy.get();
        } else if (killed instanceof ConquillagerEntity) {
            return MainConfig.ConquillagerInfamy.get();
        } else if (killed instanceof VizierEntity) {
            return MainConfig.VizierInfamy.get();
        } else if (killed.getMaxHealth() >= 50.0F) {
            return MainConfig.PowerfulInfamy.get();
        } else {
            return MainConfig.OtherInfamy.get();
        }
    }

    public static void addInfamy(Entity killer, int infamy){
        if (killer instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) killer;
            if (!GoldTotemFinder.FindTotem(player).isEmpty() || RobeArmorFinder.FindArmor(player)){
                InfamyHelper.increaseInfamy(player, infamy);
                InfamyHelper.sendInfamyUpdatePacket(player);
            }
        }
        if (killer instanceof IOwned){
            IOwned summonedEntity = (IOwned) killer;
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
