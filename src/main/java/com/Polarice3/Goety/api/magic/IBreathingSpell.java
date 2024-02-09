package com.Polarice3.Goety.api.magic;

import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;

/**
 * Breathing Spells Codes based of codes from @TeamTwilight
 */
public interface IBreathingSpell extends IEverChargeSpell{
    default List<Entity> getTarget(LivingEntity livingEntity, double range) {
        return MobUtil.getTargets(livingEntity.level, livingEntity, range, 3.0D);
    }

    void showWandBreath(LivingEntity entityLiving);

    default void breathAttack(IParticleData particleOptions, LivingEntity entityLiving, double pVelocity, double pSpread){
        this.breathAttack(particleOptions, entityLiving, 2, pVelocity, pSpread);
    }

    default void breathAttack(IParticleData particleOptions, LivingEntity entityLiving, int pParticleAmount, double pVelocity, double pSpread){
        Vector3d look = entityLiving.getLookAngle();

        double dist = 0.9;
        double px = entityLiving.getX() + look.x * dist;
        double py = entityLiving.getEyeY() + look.y * dist;
        double pz = entityLiving.getZ() + look.z * dist;

        if (pParticleAmount <= 0){
            pParticleAmount = 1;
        }

        for (int i = 0; i < pParticleAmount; i++) {
            double spread = pSpread + entityLiving.getRandom().nextDouble() * (pSpread/2);
            double velocity = pVelocity + entityLiving.getRandom().nextDouble() * pVelocity;

            Vector3d vecSpread = new Vector3d(
                    entityLiving.getRandom().nextGaussian() * 0.007499999832361937D * spread,
                    entityLiving.getRandom().nextGaussian() * 0.007499999832361937D * spread,
                    entityLiving.getRandom().nextGaussian() * 0.007499999832361937D * spread);
            Vector3d vec3 = look.add(vecSpread).multiply(velocity, velocity, velocity);
            entityLiving.level.addAlwaysVisibleParticle(particleOptions, px, py, pz, vec3.x, vec3.y, vec3.z);
        }
    }

    default void dragonBreathAttack(IParticleData particleOptions, LivingEntity entityLiving, double pVelocity){
        this.dragonBreathAttack(particleOptions, entityLiving, 10, pVelocity);
    }

    default void dragonBreathAttack(IParticleData particleOptions, LivingEntity entityLiving, int pParticleAmount, double pVelocity){
        Vector3d look = entityLiving.getLookAngle();

        double dist = 0.9D;
        double px = entityLiving.getX() + look.x * dist;
        double py = entityLiving.getEyeY() + look.y * dist;
        double pz = entityLiving.getZ() + look.z * dist;

        double velocity = pVelocity + entityLiving.getRandom().nextDouble() * pVelocity;
        for (int i = 0; i < pParticleAmount; i++) {
            double offset = 0.15D;
            double dx = entityLiving.getRandom().nextDouble() * 2.0D * offset - offset;
            double dy = entityLiving.getRandom().nextDouble() * 2.0D * offset - offset;
            double dz = entityLiving.getRandom().nextDouble() * 2.0D * offset - offset;

            double angle = 0.5D;
            Vector3d randomVec = new Vector3d(entityLiving.getRandom().nextDouble() * 2.0D * angle - angle, entityLiving.getRandom().nextDouble() * 2.0D * angle - angle, entityLiving.getRandom().nextDouble() * 2.0D * angle - angle).normalize();
            Vector3d result = (look.normalize().scale(3.0D).add(randomVec)).normalize().scale(velocity);
            entityLiving.level.addAlwaysVisibleParticle(particleOptions, px + dx, py + dy, pz + dz, result.x, result.y, result.z);
        }
    }
}
