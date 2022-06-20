package com.Polarice3.Goety.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoundUtil {
    public SoundUtil(BlockPos pPos, SoundEvent pSound, SoundCategory pCategory, float pVolume, float pPitch) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        PlayerEntity player = MINECRAFT.player;
        if (MINECRAFT.level != null) {
            MINECRAFT.level.playSound(player, (double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D, pSound, pCategory, pVolume, pPitch);
        }
    }

    public SoundUtil(Vector3d pVector3d, SoundEvent pSound, SoundCategory pCategory, float pVolume, float pPitch) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        PlayerEntity player = MINECRAFT.player;
        if (MINECRAFT.level != null) {
            MINECRAFT.level.playSound(player, pVector3d.x() + 0.5D, pVector3d.y() + 0.5D, pVector3d.z() + 0.5D, pSound, pCategory, pVolume, pPitch);
        }
    }

}
