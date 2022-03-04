package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class TemptingSpell extends ChargingSpells{

    @Override
    public int Cooldown() {
        return MainConfig.TemptingDuration.get();
    }

    public int SoulCost() {
        return MainConfig.TemptingCost.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_CAST_SPELL;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        int i = (int) entityLiving.getX();
        int j = (int) entityLiving.getY();
        int k = (int) entityLiving.getZ();
        for (AnimalEntity entity : worldIn.getEntitiesOfClass(AnimalEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(8.0D))) {
            entity.getNavigation().moveTo(entityLiving, 1.25F);
        }
        for(int i1 = 0; i1 < entityLiving.level.random.nextInt(35) + 10; ++i1) {
            new ParticleUtil(ParticleTypes.NOTE, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        int i = (int) entityLiving.getX();
        int j = (int) entityLiving.getY();
        int k = (int) entityLiving.getZ();
        for (AnimalEntity entity : worldIn.getEntitiesOfClass(AnimalEntity.class, (new AxisAlignedBB(i, j, k, i, j - 4, k)).inflate(16.0D))) {
            entity.getNavigation().moveTo(entityLiving, 1.5F);
        }
        for(int i1 = 0; i1 < entityLiving.level.random.nextInt(35) + 10; ++i1) {
            new ParticleUtil(ParticleTypes.NOTE, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
    }


}
