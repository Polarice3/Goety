package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ally.FriendlyVexEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

public class VexSpell extends SummonSpells {
    private final EntityPredicate vexCountTargeting = (new EntityPredicate()).range(16.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

    public int SoulCost() {
        return MainConfig.VexCost.get();
    }

    public int CastDuration() {
        return MainConfig.VexDuration.get();
    }

    public int SummonDownDuration() {
        return MainConfig.VexCooldown.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving){
        for (int i1 = 0; i1 < 3; ++i1) {
            BlockPos blockpos = entityLiving.blockPosition();
            FriendlyVexEntity vexentity = new FriendlyVexEntity(ModEntityType.FRIENDLY_VEX.get(), worldIn);
            vexentity.setOwnerId(entityLiving.getUUID());
            vexentity.moveTo(blockpos, 0.0F, 0.0F);
            vexentity.finalizeSpawn((IServerWorld) worldIn, entityLiving.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            vexentity.setBoundOrigin(blockpos);
            if (MainConfig.WandVexLimit.get() > VexLimit(entityLiving)) {
                vexentity.setLimitedLife(20 * (30 + entityLiving.level.random.nextInt(90)));
            } else {
                vexentity.setLimitedLife(1);
                vexentity.addEffect(new EffectInstance(Effects.WITHER, 800, 1));
                vexentity.addEffect(new EffectInstance(Effects.WEAKNESS, 800, 1));
            }
            worldIn.addFreshEntity(vexentity);
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.VexInfamyChance.get(), (PlayerEntity) entityLiving);
        this.SummonDown(entityLiving);
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving){
        for (int i1 = 0; i1 < 3 + worldIn.random.nextInt(3); ++i1) {
            BlockPos blockpos = entityLiving.blockPosition();
            FriendlyVexEntity vexentity = new FriendlyVexEntity(ModEntityType.FRIENDLY_VEX.get(), worldIn);
            vexentity.setOwnerId(entityLiving.getUUID());
            vexentity.moveTo(blockpos, 0.0F, 0.0F);
            vexentity.finalizeSpawn((IServerWorld) worldIn, entityLiving.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            vexentity.setBoundOrigin(blockpos);
            if (MainConfig.StaffVexLimit.get() > VexLimit(entityLiving)) {
                vexentity.setLimitedLife(40 * (60 + entityLiving.level.random.nextInt(180)));
            } else {
                vexentity.setLimitedLife(1);
                vexentity.addEffect(new EffectInstance(Effects.WITHER, 800, 1));
                vexentity.addEffect(new EffectInstance(Effects.WEAKNESS, 800, 1));
            }
            worldIn.addFreshEntity(vexentity);
        }
        this.SummonDown(entityLiving);
        this.IncreaseInfamy(MainConfig.VexInfamyChance.get(), (PlayerEntity) entityLiving);
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    public int VexLimit(LivingEntity entityLiving){
        return entityLiving.level.getNearbyEntities(FriendlyVexEntity.class, this.vexCountTargeting, entityLiving, entityLiving.getBoundingBox().inflate(16.0D)).size();
    }
}
