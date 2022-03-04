package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ally.ZombieMinionEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

public class ZombieSpell extends SummonSpells{

    public int SoulCost() {
        return MainConfig.ZombieCost.get();
    }

    public int CastDuration() {
        return MainConfig.ZombieDuration.get();
    }

    public int SummonDownDuration() {
        return MainConfig.ZombieCooldown.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.blockPosition();
        ZombieMinionEntity summonedentity = new ZombieMinionEntity(ModEntityType.ZOMBIE_MINION.get(), worldIn);
        summonedentity.setOwnerId(entityLiving.getUUID());
        summonedentity.moveTo(blockpos, 0.0F, 0.0F);
        summonedentity.finalizeSpawn((IServerWorld) worldIn, entityLiving.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
        summonedentity.setLimitedLife(20 * (30 + entityLiving.level.random.nextInt(90)));
        summonedentity.setUpgraded(this.NecroPower(entityLiving));
        worldIn.addFreshEntity(summonedentity);
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            new ParticleUtil(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
        this.SummonDown(entityLiving);
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.blockPosition();
        for(int i1 = 0; i1 < 2 + entityLiving.level.random.nextInt(4); ++i1) {
            ZombieMinionEntity summonedentity = new ZombieMinionEntity(ModEntityType.ZOMBIE_MINION.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUUID());
            summonedentity.moveTo(blockpos, 0.0F, 0.0F);
            summonedentity.finalizeSpawn((IServerWorld) worldIn, entityLiving.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            summonedentity.setLimitedLife(60 * (90 + entityLiving.level.random.nextInt(180)));
            summonedentity.setUpgraded(this.NecroPower(entityLiving));
            worldIn.addFreshEntity(summonedentity);
        }
        this.SummonDown(entityLiving);
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            new ParticleUtil(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
    }
}
