package com.Polarice3.Goety.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SoundUtil {

    @OnlyIn(Dist.CLIENT)
    public SoundUtil(BlockPos pPos, SoundEvent pSound, SoundCategory pCategory, float pVolume, float pPitch) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        PlayerEntity player = MINECRAFT.player;
        if (MINECRAFT.level != null) {
            MINECRAFT.level.playSound(player, (double) pPos.getX() + 0.5D, (double) pPos.getY() + 0.5D, (double) pPos.getZ() + 0.5D, pSound, pCategory, pVolume, pPitch);
        }
    }

    public SoundUtil(Entity entity, SoundEvent pSound, SoundCategory pCategory, float pVolume, float pPitch) {
        if (entity.level != null) {
            entity.level.playLocalSound(entity.position().x() + 0.5D, entity.position().y() + 0.5D, entity.position().z() + 0.5D, pSound, pCategory, pVolume, pPitch, false);
        }
    }

}
