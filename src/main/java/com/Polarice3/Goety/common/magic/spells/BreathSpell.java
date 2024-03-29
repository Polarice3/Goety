package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.magic.ChargingSpells;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

public class BreathSpell extends ChargingSpells {

    @Override
    public int Cooldown() {
        return SpellConfig.BreathingDuration.get();
    }

    public int defaultSoulCost() {
        return SpellConfig.BreathingCost.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.CONDUIT_ACTIVATE;
    }

    @Override
    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff) {
        if (staff){
            StaffResult(worldIn, entityLiving);
        } else {
            WandResult(worldIn, entityLiving);
        }
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        if (entityLiving.getAirSupply() < entityLiving.getMaxAirSupply()) {
            entityLiving.setAirSupply(increaseAirSupply(entityLiving.getAirSupply(), entityLiving));
        }
        for(int i1 = 0; i1 < entityLiving.level.random.nextInt(35) + 10; ++i1) {
            worldIn.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        if (entityLiving.getAirSupply() < entityLiving.getMaxAirSupply()) {
            entityLiving.setAirSupply(increaseAirSuperSupply(entityLiving.getAirSupply(), entityLiving));
        }
        for(int i1 = 0; i1 < entityLiving.level.random.nextInt(35) + 10; ++i1) {
            worldIn.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
    }

    public int increaseAirSupply(int pCurrentAir, LivingEntity livingEntity) {
        return Math.min(pCurrentAir + 20, livingEntity.getMaxAirSupply());
    }

    public int increaseAirSuperSupply(int pCurrentAir, LivingEntity livingEntity) {
        return Math.min(pCurrentAir + 40, livingEntity.getMaxAirSupply());
    }
}
