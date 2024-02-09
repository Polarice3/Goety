package com.Polarice3.Goety.common.magic.spells.necromancy;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.DredenMinionEntity;
import com.Polarice3.Goety.common.entities.ally.StrayMinionEntity;
import com.Polarice3.Goety.common.magic.SummonSpells;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.Enchantment;
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
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class DredenSpell extends SummonSpells {

    public int defaultSoulCost() {
        return SpellConfig.DredenCost.get();
    }

    public int defaultCastDuration() {
        return SpellConfig.DredenDuration.get();
    }

    public int SummonDownDuration() {
        return SpellConfig.DredenSummonDown.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.DredenCoolDown.get();
    }

    @Override
    public SpellType getSpellType(){
        return SpellType.NECROMANCY;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    public void commonResult(ServerWorld worldIn, LivingEntity entityLiving){
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
            }
        }
        if (isShifting(entityLiving)) {
            for (Entity entity : worldIn.getAllEntities()) {
                if (entity instanceof DredenMinionEntity) {
                    if (((DredenMinionEntity) entity).getTrueOwner() == entityLiving) {
                        entity.moveTo(entityLiving.position());
                    }
                }
                if (entity instanceof StrayMinionEntity) {
                    if (((StrayMinionEntity) entity).getTrueOwner() == entityLiving) {
                        entity.moveTo(entityLiving.position());
                    }
                }
            }
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
            }
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
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
        this.commonResult(worldIn, entityLiving);
        if (!isShifting(entityLiving)) {
            DredenMinionEntity summonedentity = new DredenMinionEntity(ModEntityType.DREDEN_MINION.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUUID());
            summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
            MobUtil.moveDownToGround(summonedentity);
            summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
            summonedentity.setPersistenceRequired();
            summonedentity.setUpgraded(this.NecroPower(entityLiving));
            summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            if (enchantment > 0){
                int boost = MathHelper.clamp(enchantment - 1, 0, 10);
                summonedentity.addEffect(new EffectInstance(ModEffects.BUFF.get(), Integer.MAX_VALUE, boost));
            }
            this.SummonSap(entityLiving, summonedentity);
            this.setTarget(worldIn, entityLiving, summonedentity);
            worldIn.addFreshEntity(summonedentity);
            this.summonAdvancement(entityLiving, summonedentity);
            this.SummonDown(entityLiving);
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.SUMMON_SPELL.get(), this.getSoundSource(), 1.0F, 1.0F);

        }
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        this.commonResult(worldIn, entityLiving);
        if (!isShifting(entityLiving)) {
                for (int i1 = 0; i1 < 2 + entityLiving.level.random.nextInt(4); ++i1) {
                    DredenMinionEntity summonedentity = new DredenMinionEntity(ModEntityType.DREDEN_MINION.get(), worldIn);
                    summonedentity.setOwnerId(entityLiving.getUUID());
                    summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
                    MobUtil.moveDownToGround(summonedentity);
                    summonedentity.setPersistenceRequired();
                    summonedentity.setUpgraded(this.NecroPower(entityLiving));
                    summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                    summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
                    if (enchantment > 0){
                        int boost = MathHelper.clamp(enchantment - 1, 0, 10);
                        summonedentity.addEffect(new EffectInstance(ModEffects.BUFF.get(), Integer.MAX_VALUE, boost));
                    }
                    this.SummonSap(entityLiving, summonedentity);
                    this.setTarget(worldIn, entityLiving, summonedentity);
                    worldIn.addFreshEntity(summonedentity);
                    this.summonAdvancement(entityLiving, summonedentity);
                }
            this.SummonDown(entityLiving);
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.SUMMON_SPELL.get(), this.getSoundSource(), 1.0F, 1.0F);
            }
    }
}
