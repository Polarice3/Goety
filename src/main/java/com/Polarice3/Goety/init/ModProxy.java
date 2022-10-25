package com.Polarice3.Goety.init;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface ModProxy {
    PlayerEntity getPlayer();
    void addBoss(MobEntity mob);
    void removeBoss(MobEntity mob);
}
