package com.Polarice3.Goety.common.magic;

import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/**
 * Spewing Spells Codes based of codes from @TeamTwilight
 */
public abstract class SpewingSpell extends ChargingSpells{

    @Override
    public int Cooldown() {
        return 0;
    }

    public List<Entity> getTarget(LivingEntity livingEntity, double range) {
        return MobUtil.getTargets(livingEntity, range);
    }

    public abstract IParticleData getParticle();

    public abstract void showWandBreath(LivingEntity entityLiving);

    public abstract void showStaffBreath(LivingEntity entityLiving);

    @OnlyIn(Dist.CLIENT)
    public void breathAttack(LivingEntity entityLiving, double pVelocity, double pSpread){
        Vector3d look = entityLiving.getLookAngle();

        double dist = 0.9;
        double px = entityLiving.getX() + look.x * dist;
        double py = entityLiving.getEyeY() + look.y * dist;
        double pz = entityLiving.getZ() + look.z * dist;

        for (int i = 0; i < 2; i++) {
            double dx = look.x;
            double dy = look.y;
            double dz = look.z;

            double spread = pSpread + entityLiving.getRandom().nextDouble() * (pSpread/2);
            double velocity = pVelocity + entityLiving.getRandom().nextDouble() * pVelocity;

            dx += entityLiving.getRandom().nextGaussian() * 0.007499999832361937D * spread;
            dy += entityLiving.getRandom().nextGaussian() * 0.007499999832361937D * spread;
            dz += entityLiving.getRandom().nextGaussian() * 0.007499999832361937D * spread;
            dx *= velocity;
            dy *= velocity;
            dz *= velocity;

            entityLiving.level.addAlwaysVisibleParticle(getParticle(), px, py, pz, dx, dy, dz);
        }
    }
}
