package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;
import java.util.Optional;

public abstract class SpewingSpell extends ChargingSpells{

    @Override
    public int Cooldown() {
        return 0;
    }

    public abstract double range();

    public Entity getTarget(LivingEntity livingEntity) {
        Entity target = null;
        double range = range();
        Vector3d srcVec = new Vector3d(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
        Vector3d lookVec = livingEntity.getViewVector(1.0F);
        Vector3d destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
        float size = 1.0F;
        List<Entity> entities = livingEntity.level.getEntities(livingEntity, livingEntity.getBoundingBox().expandTowards(lookVec.x * range, lookVec.y * range, lookVec.z * range).inflate(size, size, size));
        double hitDist = 0;

        for (Entity entity : entities) {
            if (entity.isPickable()) {
                float borderSize = entity.getPickRadius();
                AxisAlignedBB collisionBB = entity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
                Optional<Vector3d> interceptPos = collisionBB.clip(srcVec, destVec);

                if (collisionBB.contains(srcVec)) {
                    if (0.0D < hitDist || hitDist == 0.0D) {
                        target = entity;
                        hitDist = 0.0D;
                    }
                } else if (interceptPos.isPresent()) {
                    double possibleDist = srcVec.distanceTo(interceptPos.get());

                    if (possibleDist < hitDist || hitDist == 0.0D) {
                        target = entity;
                        hitDist = possibleDist;
                    }
                }
            }
        }
        return target;
    }

    public Entity getStaffTarget(LivingEntity livingEntity) {
        Entity target = null;
        double range = range() * 2;
        Vector3d srcVec = new Vector3d(livingEntity.getX(), livingEntity.getEyeY(), livingEntity.getZ());
        Vector3d lookVec = livingEntity.getViewVector(1.0F);
        Vector3d destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
        float size = 2.0F;
        List<Entity> entities = livingEntity.level.getEntities(livingEntity, livingEntity.getBoundingBox().expandTowards(lookVec.x * range, lookVec.y * range, lookVec.z * range).inflate(size, size, size));
        double hitDist = 0;

        for (Entity entity : entities) {
            if (entity.isPickable()) {
                float borderSize = entity.getPickRadius();
                AxisAlignedBB collisionBB = entity.getBoundingBox().inflate(borderSize, borderSize, borderSize);
                Optional<Vector3d> interceptPos = collisionBB.clip(srcVec, destVec);

                if (collisionBB.contains(srcVec)) {
                    if (0.0D < hitDist || hitDist == 0.0D) {
                        target = entity;
                        hitDist = 0.0D;
                    }
                } else if (interceptPos.isPresent()) {
                    double possibleDist = srcVec.distanceTo(interceptPos.get());

                    if (possibleDist < hitDist || hitDist == 0.0D) {
                        target = entity;
                        hitDist = possibleDist;
                    }
                }
            }
        }
        return target;
    }

    public abstract IParticleData getParticle();

    public abstract double getParticleVelocity();

    public void breathAttack(LivingEntity entityLiving){
        Vector3d look = entityLiving.getLookAngle();

        double dist = 0.9;
        double px = entityLiving.getX() + look.x * dist;
        double py = entityLiving.getEyeY() + look.y * dist;
        double pz = entityLiving.getZ() + look.z * dist;

        for (int i = 0; i < 2; i++) {
            double dx = look.x;
            double dy = look.y;
            double dz = look.z;

            double spread = 5 + entityLiving.getRandom().nextDouble() * 2.5;
            double velocity = getParticleVelocity() + entityLiving.getRandom().nextDouble() * getParticleVelocity();

            dx += entityLiving.getRandom().nextGaussian() * 0.007499999832361937D * spread;
            dy += entityLiving.getRandom().nextGaussian() * 0.007499999832361937D * spread;
            dz += entityLiving.getRandom().nextGaussian() * 0.007499999832361937D * spread;
            dx *= velocity;
            dy *= velocity;
            dz *= velocity;

            new ParticleUtil(getParticle(), px, py, pz, dx, dy, dz);
        }
    }

    public void breathStaffAttack(LivingEntity entityLiving){
        Vector3d look = entityLiving.getLookAngle();

        double dist = 0.9;
        double px = entityLiving.getX() + look.x * dist;
        double py = entityLiving.getEyeY() + look.y * dist;
        double pz = entityLiving.getZ() + look.z * dist;

        for (int i = 0; i < 2; i++) {
            double dx = look.x;
            double dy = look.y;
            double dz = look.z;

            double spread = 10 + entityLiving.getRandom().nextDouble() * 5;
            double velocity = (getParticleVelocity() * 2) + entityLiving.getRandom().nextDouble() * (getParticleVelocity() * 2);

            dx += entityLiving.getRandom().nextGaussian() * 0.007499999832361937D * spread;
            dy += entityLiving.getRandom().nextGaussian() * 0.007499999832361937D * spread;
            dz += entityLiving.getRandom().nextGaussian() * 0.007499999832361937D * spread;
            dx *= velocity;
            dy *= velocity;
            dz *= velocity;

            new ParticleUtil(getParticle(), px, py, pz, dx, dy, dz);
        }
    }
}
