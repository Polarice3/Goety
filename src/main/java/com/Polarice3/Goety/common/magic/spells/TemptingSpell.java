package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.magic.ChargingSpells;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;

public class TemptingSpell extends ChargingSpells {

    @Override
    public int Cooldown() {
        return SpellConfig.TemptingDuration.get();
    }

    public int defaultSoulCost() {
        return SpellConfig.TemptingCost.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_CAST_SPELL;
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
        int i = (int) entityLiving.getX();
        int j = (int) entityLiving.getY();
        int k = (int) entityLiving.getZ();
        float speed = RobeArmorFinder.FindFelBootsofWander(entityLiving) ? 1.75F : 1.25F;
        for (AnimalEntity entity : worldIn.getEntitiesOfClass(AnimalEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(8.0D))) {
            if(!entity.hasPassenger(entityLiving)) {
                entity.getNavigation().moveTo(entityLiving, speed);
            }
        }
        for(int i1 = 0; i1 < entityLiving.level.random.nextInt(35) + 10; ++i1) {
            worldIn.sendParticles(ParticleTypes.NOTE, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        int i = (int) entityLiving.getX();
        int j = (int) entityLiving.getY();
        int k = (int) entityLiving.getZ();
        float speed = RobeArmorFinder.FindFelBootsofWander(entityLiving) ? 1.75F : 1.25F;
        for (AnimalEntity entity : worldIn.getEntitiesOfClass(AnimalEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(16.0D))) {
            if(!entity.hasPassenger(entityLiving)) {
                entity.getNavigation().moveTo(entityLiving, speed);
            }
        }
        for(int i1 = 0; i1 < entityLiving.level.random.nextInt(35) + 10; ++i1) {
            worldIn.sendParticles(ParticleTypes.NOTE, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
    }


}
