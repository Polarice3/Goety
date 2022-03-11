package com.Polarice3.Goety.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.IParticleData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleUtil {
    public ParticleUtil(IParticleData pParticleData, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed){
        Minecraft MINECRAFT = Minecraft.getInstance();
        if (MINECRAFT.level != null){
            MINECRAFT.level.addParticle(pParticleData, true, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        }
    }
}
