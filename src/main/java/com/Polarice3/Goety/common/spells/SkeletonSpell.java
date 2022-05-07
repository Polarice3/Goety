package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ally.SkeletonMinionEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class SkeletonSpell extends SummonSpells{

    public int SoulCost() {
        return MainConfig.SkeletonCost.get();
    }

    public int CastDuration() {
        return MainConfig.SkeletonDuration.get();
    }

    public int SummonDownDuration() {
        return MainConfig.SkeletonCooldown.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        if (entityLiving.isCrouching()){
            if (!worldIn.isClientSide){
                ServerWorld serverWorld = (ServerWorld) worldIn;
                for (Entity entity: serverWorld.getAllEntities()){
                    if (entity instanceof SkeletonMinionEntity){
                        if (((SkeletonMinionEntity) entity).getTrueOwner() == entityLiving){
                            entity.moveTo(entityLiving.position());
                        }
                    }
                }
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    new ParticleUtil(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
                }
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        } else {
            SkeletonMinionEntity summonedentity = new SkeletonMinionEntity(ModEntityType.SKELETON_MINION.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUUID());
            summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
            summonedentity.finalizeSpawn((IServerWorld) worldIn, entityLiving.level.getCurrentDifficultyAt(BlockFinder.SummonRadius(entityLiving, worldIn)), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            summonedentity.setLimitedLife(20 * (30 + entityLiving.level.random.nextInt(90)));
            summonedentity.setPersistenceRequired();
            summonedentity.setUpgraded(this.NecroPower(entityLiving));
            worldIn.addFreshEntity(summonedentity);
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                new ParticleUtil(ParticleTypes.POOF, summonedentity.getX(), summonedentity.getEyeY(), summonedentity.getZ(), 0.0F, 0.0F, 0.0F);
            }
            this.SummonDown(entityLiving);
            this.IncreaseInfamy(MainConfig.SkeletonInfamyChance.get(), (PlayerEntity) entityLiving);
        }
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        if (entityLiving.isCrouching()){
            if (!worldIn.isClientSide){
                ServerWorld serverWorld = (ServerWorld) worldIn;
                for (Entity entity: serverWorld.getAllEntities()){
                    if (entity instanceof SkeletonMinionEntity){
                        if (((SkeletonMinionEntity) entity).getTrueOwner() == entityLiving){
                            entity.moveTo(entityLiving.position());
                        }
                    }
                }
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    new ParticleUtil(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
                }
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        } else {
            for (int i1 = 0; i1 < 2 + entityLiving.level.random.nextInt(4); ++i1) {
                SkeletonMinionEntity summonedentity = new SkeletonMinionEntity(ModEntityType.SKELETON_MINION.get(), worldIn);
                summonedentity.setOwnerId(entityLiving.getUUID());
                summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
                summonedentity.setPersistenceRequired();
                summonedentity.setUpgraded(this.NecroPower(entityLiving));
                summonedentity.setLimitedLife(60 * (90 + entityLiving.level.random.nextInt(180)));
                summonedentity.finalizeSpawn((IServerWorld) worldIn, entityLiving.level.getCurrentDifficultyAt(BlockFinder.SummonRadius(entityLiving, worldIn)), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                worldIn.addFreshEntity(summonedentity);
                for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    new ParticleUtil(ParticleTypes.POOF, summonedentity.getX(), summonedentity.getEyeY(), summonedentity.getZ(), 0.0F, 0.0F, 0.0F);
                }
            }
            this.SummonDown(entityLiving);
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            this.IncreaseInfamy(MainConfig.SkeletonInfamyChance.get(), (PlayerEntity) entityLiving);
        }
    }
}
