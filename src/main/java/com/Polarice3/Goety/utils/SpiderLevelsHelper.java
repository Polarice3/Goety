package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.capabilities.spider.ISpiderLevels;
import com.Polarice3.Goety.common.capabilities.spider.SpiderLevelsImp;
import com.Polarice3.Goety.common.capabilities.spider.SpiderLevelsProvider;
import com.Polarice3.Goety.common.capabilities.spider.SpiderLevelsUpdatePacket;
import com.Polarice3.Goety.common.network.ModNetwork;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class SpiderLevelsHelper {
    public static ISpiderLevels getCapability(LivingEntity livingEntity) {
        return livingEntity.getCapability(SpiderLevelsProvider.CAPABILITY).orElse(new SpiderLevelsImp());
    }

    public static void sendSpiderLevelsUpdatePacket(PlayerEntity player, LivingEntity livingEntity) {
        ModNetwork.sendTo(player, new SpiderLevelsUpdatePacket(livingEntity));
    }
}
