package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class BreathSpell extends ChargingSpells{

    @Override
    public int Cooldown() {
        return MainConfig.BreathingDuration.get();
    }

    public int SoulCost() {
        return MainConfig.BreathingCost.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.CONDUIT_ACTIVATE;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        if (entityLiving.getAirSupply() < entityLiving.getMaxAirSupply()) {
            entityLiving.setAirSupply(increaseAirSupply(entityLiving.getAirSupply(), entityLiving));
        }
        for(int i1 = 0; i1 < entityLiving.level.random.nextInt(35) + 10; ++i1) {
            new ParticleUtil(ParticleTypes.BUBBLE_COLUMN_UP, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
        this.IncreaseInfamy(MainConfig.BreathingInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        if (entityLiving.getAirSupply() < entityLiving.getMaxAirSupply()) {
            entityLiving.setAirSupply(increaseAirSuperSupply(entityLiving.getAirSupply(), entityLiving));
        }
        for(int i1 = 0; i1 < entityLiving.level.random.nextInt(35) + 10; ++i1) {
            new ParticleUtil(ParticleTypes.BUBBLE_COLUMN_UP, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
        this.IncreaseInfamy(MainConfig.BreathingInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    public int increaseAirSupply(int pCurrentAir, LivingEntity livingEntity) {
        return Math.min(pCurrentAir + 20, livingEntity.getMaxAirSupply());
    }

    public int increaseAirSuperSupply(int pCurrentAir, LivingEntity livingEntity) {
        return Math.min(pCurrentAir + 40, livingEntity.getMaxAirSupply());
    }
}
