package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class FireBreathSpell extends SpewingSpell{

    @Override
    public double range() {
        return 15;
    }

    @Override
    public double getParticleVelocity() {
        return 0.3;
    }

    @Override
    public int SoulCost() {
        return MainConfig.FireBreathCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    @Override
    public void WandResult(World worldIn, LivingEntity entityLiving) {
        this.breathAttack(entityLiving);
        Entity target = getTarget(entityLiving);
        if (target != null) {
            if (!target.fireImmune()){
                target.setSecondsOnFire(30);
                target.hurt(DamageSource.mobAttack(entityLiving), 2.0F);
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.BLAZE_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.FireBreathInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    @Override
    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        this.breathStaffAttack(entityLiving);
        Entity target = getStaffTarget(entityLiving);
        if (target != null) {
            if (!target.fireImmune()){
                target.setSecondsOnFire(60);
                target.hurt(DamageSource.mobAttack(entityLiving), 4.0F);
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.BLAZE_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.FireBreathInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    @Override
    public IParticleData getParticle() {
        return ParticleTypes.FLAME;
    }

}
