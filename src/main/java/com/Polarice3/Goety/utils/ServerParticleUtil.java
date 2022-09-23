package com.Polarice3.Goety.utils;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

public class ServerParticleUtil {
    public static void smokeParticles(IParticleData pParticleData, double x, double y, double z, World world){
        ServerWorld serverWorld = (ServerWorld) world;
        serverWorld.sendParticles(pParticleData, x, y, z, 1, 0, 0, 0, 0);
    }

    public static void addParticles(IParticleData pParticleData, double x, double y, double z, double pXOffset, double pYOffset, double pZOffset, World world){
        ServerWorld serverWorld = (ServerWorld) world;
        serverWorld.sendParticles(pParticleData, x, y, z, 0, pXOffset, pYOffset, pZOffset, 0.5F);
    }

    public static void gatheringParticles(IParticleData pParticleData, Entity livingEntity, ServerWorld serverWorld){
        List<BlockPos> positions = Lists.newArrayList();
        if (serverWorld != null) {
            for(int j1 = -2; j1 <= 2; ++j1) {
                for(int k1 = -2; k1 <= 2; ++k1) {
                    for(int l1 = -2; l1 <= 2; ++l1) {
                        int i2 = Math.abs(j1);
                        int l = Math.abs(k1);
                        int i1 = Math.abs(l1);
                        if ((j1 == 0 && (l == 2 || i1 == 2) || k1 == 0 && (i2 == 2 || i1 == 2) || l1 == 0 && (i2 == 2 || l == 2))) {
                            BlockPos blockpos1 = livingEntity.blockPosition().offset(j1, k1, l1);
                            positions.add(blockpos1);
                        }
                    }
                }
            }
            Vector3d vector3d = new Vector3d(livingEntity.blockPosition().getX(), livingEntity.blockPosition().getY(), livingEntity.blockPosition().getZ());
            for(BlockPos blockpos : positions) {
                if (serverWorld.random.nextInt(50) == 0) {
                    float f = -0.5F + serverWorld.random.nextFloat();
                    float f1 = -2.0F + serverWorld.random.nextFloat();
                    float f2 = -0.5F + serverWorld.random.nextFloat();
                    BlockPos blockpos1 = blockpos.subtract(livingEntity.blockPosition());
                    Vector3d vector3d1 = (new Vector3d(f, f1, f2)).add(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                    serverWorld.sendParticles(pParticleData, vector3d.x, vector3d.y, vector3d.z, 0, vector3d1.x, vector3d1.y, vector3d1.z, 0.5F);
                }
            }
        }
    }
}
