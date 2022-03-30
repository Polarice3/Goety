package com.Polarice3.Goety.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class SoundUtil {
    public SoundUtil(BlockPos pPos, SoundEvent pSound, SoundCategory pCategory, float pVolume, float pPitch) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        if (MINECRAFT.level != null) {
            MINECRAFT.level.playSound(null, (double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D, pSound, pCategory, pVolume, pPitch);
        }
    }

}
