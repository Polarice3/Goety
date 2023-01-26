package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.WraithMinionEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
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
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;

public class WraithSpell extends SummonSpells{
    public int burning = 0;

    public int SoulCost() {
        return MainConfig.WraithCost.get();
    }

    public int CastDuration() {
        return MainConfig.WraithDuration.get();
    }

    public int SummonDownDuration() {
        return MainConfig.WraithCooldown.get();
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    public void commonResult(ServerWorld worldIn, LivingEntity entityLiving){
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                this.enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                this.duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
                this.burning = WandUtil.getLevels(ModEnchantments.BURNING.get(), player);
            }
            this.IncreaseInfamy(MainConfig.WraithInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        if (isShifting(entityLiving)) {
            for (Entity entity : worldIn.getAllEntities()) {
                if (entity instanceof WraithMinionEntity) {
                    if (((WraithMinionEntity) entity).getTrueOwner() == entityLiving) {
                        entity.moveTo(entityLiving.position());
                    }
                }
            }
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
            }
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.SUMMON_SPELL.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        this.commonResult(worldIn, entityLiving);
        if (!isShifting(entityLiving))  {
                WraithMinionEntity summonedentity = new WraithMinionEntity(ModEntityType.WRAITH_MINION.get(), worldIn);
                summonedentity.setOwnerId(entityLiving.getUUID());
                summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
                summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                summonedentity.setPersistenceRequired();
                summonedentity.setUpgraded(this.NecroPower(entityLiving));
                if (enchantment > 0){
                    int boost = MathHelper.clamp(enchantment - 1, 0, 10);
                    summonedentity.addEffect(new EffectInstance(ModEffects.BUFF.get(), Integer.MAX_VALUE, boost));
                }
                if (burning > 0){
                    summonedentity.setBurningLevel(burning);
                }
                this.SummonSap(entityLiving, summonedentity);
                worldIn.addFreshEntity(summonedentity);
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.SUMMON_SPELL.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    worldIn.sendParticles(ParticleTypes.POOF, summonedentity.getX(), summonedentity.getEyeY(), summonedentity.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                }
                this.SummonDown(entityLiving);

            }
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        this.commonResult(worldIn, entityLiving);
        if (!isShifting(entityLiving)) {
                for (int i1 = 0; i1 < 2 + entityLiving.level.random.nextInt(4); ++i1) {
                    WraithMinionEntity summonedentity = new WraithMinionEntity(ModEntityType.WRAITH_MINION.get(), worldIn);
                    summonedentity.setOwnerId(entityLiving.getUUID());
                    summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
                    summonedentity.setPersistenceRequired();
                    summonedentity.setUpgraded(this.NecroPower(entityLiving));
                    summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                    summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                    if (enchantment > 0){
                        int boost = MathHelper.clamp(enchantment - 1, 0, 10);
                        summonedentity.addEffect(new EffectInstance(ModEffects.BUFF.get(), Integer.MAX_VALUE, boost));
                    }
                    if (burning > 0){
                        summonedentity.setBurningLevel(burning);
                    }
                    this.SummonSap(entityLiving, summonedentity);
                    worldIn.addFreshEntity(summonedentity);
                    for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                        worldIn.sendParticles(ParticleTypes.POOF, summonedentity.getX(), summonedentity.getEyeY(), summonedentity.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                    }
                }
                this.SummonDown(entityLiving);
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.SUMMON_SPELL.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
    }
}
