package com.Polarice3.Goety.utils;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
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

    public ParticleUtil(IParticleData pParticleData, Entity livingEntity, World world){
        Minecraft MINECRAFT = Minecraft.getInstance();
        List<BlockPos> positions = Lists.newArrayList();
        if (MINECRAFT.level != null) {
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
                if (world.random.nextInt(50) == 0) {
                    float f = -0.5F + world.random.nextFloat();
                    float f1 = -2.0F + world.random.nextFloat();
                    float f2 = -0.5F + world.random.nextFloat();
                    BlockPos blockpos1 = blockpos.subtract(livingEntity.blockPosition());
                    Vector3d vector3d1 = (new Vector3d(f, f1, f2)).add(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
                    new ParticleUtil(pParticleData, vector3d.x, vector3d.y, vector3d.z, vector3d1.x, vector3d1.y, vector3d1.z);
                }
            }
        }
    }
}
