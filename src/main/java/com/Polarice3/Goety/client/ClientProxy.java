package com.Polarice3.Goety.client;

import com.Polarice3.Goety.init.ModProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public class ClientProxy implements ModProxy {
    @Override
    public PlayerEntity getPlayer() {
        return Minecraft.getInstance().player;
    }
}
