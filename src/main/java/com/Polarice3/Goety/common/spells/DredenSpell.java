package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.DredenMinionEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
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
import net.minecraft.world.server.ServerWorld;

public class DredenSpell extends SummonSpells{

    public int SoulCost() {
        return MainConfig.DredenCost.get();
    }

    public int CastDuration() {
        return MainConfig.DredenDuration.get();
    }

    public int SummonDownDuration() {
        return MainConfig.DredenCooldown.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide()) {
            int enchantment = 0;
            int duration = 1;
            if (entityLiving instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) entityLiving;
                if (WandUtil.enchantedFocus(player)){
                    enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                    duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
                }
                this.IncreaseInfamy(MainConfig.DredenInfamyChance.get(), (PlayerEntity) entityLiving);
            }
            if (entityLiving.isCrouching()) {
                for (Entity entity : worldIn.getAllEntities()) {
                    if (entity instanceof DredenMinionEntity) {
                        if (((DredenMinionEntity) entity).getTrueOwner() == entityLiving) {
                            entity.moveTo(entityLiving.position());
                        }
                    }
                }
                for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                }
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            } else {
                DredenMinionEntity summonedentity = new DredenMinionEntity(ModEntityType.DREDEN_MINION.get(), worldIn);
                summonedentity.setOwnerId(entityLiving.getUUID());
                summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
                summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                summonedentity.setPersistenceRequired();
                summonedentity.setUpgraded(this.NecroPower(entityLiving));
                worldIn.addFreshEntity(summonedentity);
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    worldIn.sendParticles(ParticleTypes.POOF, summonedentity.getX(), summonedentity.getEyeY(), summonedentity.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                }
                this.SummonDown(entityLiving);

            }
        }
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide()) {
            int enchantment = 0;
            int duration = 1;
            if (entityLiving instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) entityLiving;
                if (WandUtil.enchantedFocus(player)){
                    enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                    duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
                }
                this.IncreaseInfamy(MainConfig.DredenInfamyChance.get(), (PlayerEntity) entityLiving);
            }
            if (entityLiving.isCrouching()) {
                for (Entity entity : worldIn.getAllEntities()) {
                    if (entity instanceof DredenMinionEntity) {
                        if (((DredenMinionEntity) entity).getTrueOwner() == entityLiving) {
                            entity.moveTo(entityLiving.position());
                        }
                    }
                }
                for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                }
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            } else {
                for (int i1 = 0; i1 < 2 + entityLiving.level.random.nextInt(4); ++i1) {
                    DredenMinionEntity summonedentity = new DredenMinionEntity(ModEntityType.DREDEN_MINION.get(), worldIn);
                    summonedentity.setOwnerId(entityLiving.getUUID());
                    summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
                    summonedentity.setPersistenceRequired();
                    summonedentity.setUpgraded(this.NecroPower(entityLiving));
                    summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                    summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                    worldIn.addFreshEntity(summonedentity);
                    for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                        worldIn.sendParticles(ParticleTypes.POOF, summonedentity.getX(), summonedentity.getEyeY(), summonedentity.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                    }
                }
                this.SummonDown(entityLiving);
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
        }
    }
}
