package com.Polarice3.Goety.utils;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EmitterParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
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

    public ParticleUtil(IParticleData pParticleData, LivingEntity livingEntity, World world){
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
