package com.Polarice3.Goety.common;

import com.Polarice3.Goety.init.ModProxy;
import net.minecraft.entity.player.PlayerEntity;

public class CommonProxy implements ModProxy {
    @Override
    public PlayerEntity getPlayer() {
        return null;
    }
}
