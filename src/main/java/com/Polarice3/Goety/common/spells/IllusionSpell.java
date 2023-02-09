package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.entities.ally.IllusionCloneEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.server.ServerWorld;

public class IllusionSpell extends Spells{
    public int SoulCost() {
        return SpellConfig.IllusionCost.get();
    }

    public int CastDuration() {
        return SpellConfig.IllusionDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        for (Entity entity : worldIn.getAllEntities()) {
            if (entity instanceof IllusionCloneEntity) {
                if (((IllusionCloneEntity) entity).getTrueOwner() == entityLiving) {
                    ((IllusionCloneEntity) entity).die(DamageSource.STARVE);
                }
            }
        }
        for (int i1 = 0; i1 < 4; ++i1) {
            IllusionCloneEntity summonedentity = new IllusionCloneEntity(ModEntityType.ILLUSION_CLONE.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUUID());
            summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
            summonedentity.finalizeSpawn((IServerWorld) worldIn, entityLiving.level.getCurrentDifficultyAt(BlockFinder.SummonRadius(entityLiving, worldIn)), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            summonedentity.setLimitedLife(1200);
            summonedentity.setPersistenceRequired();
            summonedentity.setUpgraded(RobeArmorFinder.FindIllusionSet(entityLiving));
            worldIn.addFreshEntity(summonedentity);
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                worldIn.sendParticles(ParticleTypes.CLOUD, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0, 0.0F, 0.0F, 0.0F, 0);
            }
        }
        if (RobeArmorFinder.FindIllusionSet(entityLiving)){
            entityLiving.addEffect(new EffectInstance(Effects.INVISIBILITY, 1200));
            RayTraceResult rayTraceResult = this.rayTrace(worldIn, entityLiving, 16, 3);
            if (rayTraceResult instanceof EntityRayTraceResult) {
                Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
                if (target instanceof LivingEntity){
                    ((LivingEntity) target).addEffect(new EffectInstance(Effects.BLINDNESS, 400));
                }
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            worldIn.sendParticles(ParticleTypes.CLOUD, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0, 0.0F, 0.0F, 0.0F, 0);
        }
        this.IncreaseInfamy(SpellConfig.IllusionInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        for (Entity entity : worldIn.getAllEntities()) {
            if (entity instanceof IllusionCloneEntity) {
                if (((IllusionCloneEntity) entity).getTrueOwner() == entityLiving) {
                    ((IllusionCloneEntity) entity).die(DamageSource.STARVE);
                }
            }
        }
        for (int i1 = 0; i1 < 4 + entityLiving.level.random.nextInt(4); ++i1) {
            IllusionCloneEntity summonedentity = new IllusionCloneEntity(ModEntityType.ILLUSION_CLONE.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUUID());
            summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
            summonedentity.finalizeSpawn((IServerWorld) worldIn, entityLiving.level.getCurrentDifficultyAt(BlockFinder.SummonRadius(entityLiving, worldIn)), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            summonedentity.setLimitedLife(1200);
            summonedentity.setPersistenceRequired();
            summonedentity.setUpgraded(RobeArmorFinder.FindIllusionSet(entityLiving));
            worldIn.addFreshEntity(summonedentity);
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                worldIn.sendParticles(ParticleTypes.CLOUD, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0, 0.0F, 0.0F, 0.0F, 0);
            }
        }
        if (RobeArmorFinder.FindIllusionSet(entityLiving)){
            entityLiving.addEffect(new EffectInstance(Effects.INVISIBILITY, 1200));
            RayTraceResult rayTraceResult = this.rayTrace(worldIn, entityLiving, 16, 3);
            if (rayTraceResult instanceof EntityRayTraceResult) {
                Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
                if (target instanceof LivingEntity){
                    ((LivingEntity) target).addEffect(new EffectInstance(Effects.BLINDNESS, 400));
                }
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            worldIn.sendParticles(ParticleTypes.CLOUD, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0, 0.0F, 0.0F, 0.0F, 0);
        }
        this.IncreaseInfamy(SpellConfig.IllusionInfamyChance.get(), (PlayerEntity) entityLiving);
    }

}
