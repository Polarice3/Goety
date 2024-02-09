package com.Polarice3.Goety.common.magic.spells.necromancy;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.WraithMinionEntity;
import com.Polarice3.Goety.common.magic.SummonSpells;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class WraithSpell extends SummonSpells {
    public int burning = 0;

    public int defaultSoulCost() {
        return SpellConfig.WraithCost.get();
    }

    public int defaultCastDuration() {
        return SpellConfig.WraithDuration.get();
    }

    public int SummonDownDuration() {
        return SpellConfig.WraithSummonDown.get();
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.WraithCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.NECROMANCY;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        list.add(ModEnchantments.BURNING.get());
        return list;
    }

    public void commonResult(ServerWorld worldIn, LivingEntity entityLiving){
        if (WandUtil.enchantedFocus(entityLiving)){
            this.enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), entityLiving);
            this.duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), entityLiving) + 1;
            this.burning = WandUtil.getLevels(ModEnchantments.BURNING.get(), entityLiving);
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
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.SUMMON_SPELL.get(), this.getSoundSource(), 1.0F, 1.0F);
        }
    }

    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff) {
        this.commonResult(worldIn, entityLiving);
        if (!isShifting(entityLiving)) {
            int i = 1;
            if (staff){
                i = 2 + entityLiving.level.random.nextInt(4);
            }
            for (int i1 = 0; i1 < i; ++i1) {
                WraithMinionEntity summonedentity = new WraithMinionEntity(ModEntityType.WRAITH_MINION.get(), worldIn);
                summonedentity.setOwnerId(entityLiving.getUUID());
                summonedentity.moveTo(BlockFinder.SummonRadius(entityLiving, worldIn), 0.0F, 0.0F);
                MobUtil.moveDownToGround(summonedentity);
                summonedentity.setPersistenceRequired();
                summonedentity.setUpgraded(this.NecroPower(entityLiving));
                summonedentity.setBurningLevel(burning);
                summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, null, null);
                if (enchantment > 0){
                    int boost = MathHelper.clamp(enchantment - 1, 0, 10);
                    summonedentity.addEffect(new EffectInstance(ModEffects.BUFF.get(), Integer.MAX_VALUE, boost));
                }
                this.SummonSap(entityLiving, summonedentity);
                this.setTarget(worldIn, entityLiving, summonedentity);
                worldIn.addFreshEntity(summonedentity);
                this.summonAdvancement(entityLiving, entityLiving);
            }
            this.SummonDown(entityLiving);
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.SUMMON_SPELL.get(), this.getSoundSource(), 1.0F, 1.0F);
        }
    }
}
