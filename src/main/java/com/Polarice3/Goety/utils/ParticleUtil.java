package com.Polarice3.Goety.utils;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EmitterParticle;
import net.minecraft.entity.Entity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

public class ParticleUtil {
    public ParticleUtil(IParticleData pParticleData, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed){
        Minecraft MINECRAFT = Minecraft.getInstance();
        if (MINECRAFT.level != null){
            MINECRAFT.level.addParticle(pParticleData, true, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        }
    }

    public ParticleUtil(World world, IParticleData pParticleData, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed){
        if (world != null){
            world.addAlwaysVisibleParticle(pParticleData, true, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
        }
    }

    public ParticleUtil(Entity pEntity, IParticleData pParticleData){
        Minecraft MINECRAFT = Minecraft.getInstance();
        if (MINECRAFT.level != null){
            if (MINECRAFT.level.isClientSide) {
                if (pEntity != null) {
                    new EmitterParticle(MINECRAFT.level, pEntity, pParticleData);
                }
            }
        }
    }

    public ParticleUtil(IParticleData pParticleData, BlockPos pPos, BlockState pState) {
        Minecraft MINECRAFT = Minecraft.getInstance();
        if (MINECRAFT.level != null) {
            VoxelShape voxelshape = pState.getShape(MINECRAFT.level, pPos);
            voxelshape.forAllBoxes((p_228348_3_, p_228348_5_, p_228348_7_, p_228348_9_, p_228348_11_, p_228348_13_) -> {
                double d1 = Math.min(1.0D, p_228348_9_ - p_228348_3_);
                double d2 = Math.min(1.0D, p_228348_11_ - p_228348_5_);
                double d3 = Math.min(1.0D, p_228348_13_ - p_228348_7_);
                int i = Math.max(2, MathHelper.ceil(d1 / 0.25D));
                int j = Math.max(2, MathHelper.ceil(d2 / 0.25D));
                int k = Math.max(2, MathHelper.ceil(d3 / 0.25D));

                for(int l = 0; l < i; ++l) {
                    for(int i1 = 0; i1 < j; ++i1) {
                        for(int j1 = 0; j1 < k; ++j1) {
                            double d4 = ((double)l + 0.5D) / (double)i;
                            double d5 = ((double)i1 + 0.5D) / (double)j;
                            double d6 = ((double)j1 + 0.5D) / (double)k;
                            double d7 = d4 * d1 + p_228348_3_;
                            double d8 = d5 * d2 + p_228348_5_;
                            double d9 = d6 * d3 + p_228348_7_;
                            new ParticleUtil(pParticleData, (double)pPos.getX() + d7, (double)pPos.getY() + d8, (double)pPos.getZ() + d9, d4 - 0.5D, d5 - 0.5D, d6 - 0.5D);
                        }
                    }
                }
            });
        }
    }
}
