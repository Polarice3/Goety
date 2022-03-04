package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ally.CreeperlingMinionEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CreeperlingSpell extends ChargingSpells{

    @Override
    public int Cooldown() {
        return MainConfig.CreeperlingDuration.get();
    }

    public int SoulCost() {
        return MainConfig.CreeperlingCost.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.blockPosition();
        CreeperlingMinionEntity summonedentity = new CreeperlingMinionEntity(ModEntityType.CREEPERLING_MINION.get(), worldIn);
        summonedentity.setOwnerId(entityLiving.getUUID());
        summonedentity.moveTo(blockpos, 0.0F, 0.0F);
        summonedentity.setLimitedLife(180);
        worldIn.addFreshEntity(summonedentity);
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            new ParticleUtil(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.blockPosition();
        CreeperlingMinionEntity summonedentity = new CreeperlingMinionEntity(ModEntityType.CREEPERLING_MINION.get(), worldIn);
        summonedentity.setOwnerId(entityLiving.getUUID());
        summonedentity.setUpgraded();
        summonedentity.moveTo(blockpos, 0.0F, 0.0F);
        summonedentity.setLimitedLife(360);
        worldIn.addFreshEntity(summonedentity);
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            new ParticleUtil(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
    }


}
