package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.spider.ISpiderLevels;
import com.Polarice3.Goety.common.spider.SpiderLevelsImp;
import com.Polarice3.Goety.common.spider.SpiderLevelsProvider;
import com.Polarice3.Goety.common.spider.SpiderLevelsUpdatePacket;
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
