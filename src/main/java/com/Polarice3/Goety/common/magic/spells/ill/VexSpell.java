package com.Polarice3.Goety.common.magic.spells.ill;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.FriendlyVexEntity;
import com.Polarice3.Goety.common.entities.neutral.MinionEntity;
import com.Polarice3.Goety.common.magic.SummonSpells;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VexSpell extends SummonSpells {
    private final EntityPredicate vexCountTargeting = (new EntityPredicate()).range(16.0D).allowUnseeable().ignoreInvisibilityTesting().allowInvulnerable().allowSameTeam();

    public int defaultSoulCost() {
        return SpellConfig.VexCost.get();
    }

    public int defaultCastDuration() {
        return SpellConfig.VexDuration.get();
    }

    public int SummonDownDuration() {
        return SpellConfig.VexSummonDown.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.VexCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.ILL;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    @Override
    public ColorUtil particleColors(LivingEntity entityLiving) {
        return new ColorUtil(0.7F, 0.7F, 0.8F);
    }

    public void commonResult(ServerWorld worldIn, LivingEntity entityLiving){
        if (WandUtil.enchantedFocus(entityLiving)){
            enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), entityLiving);
            duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), entityLiving) + 1;
        }
        if (isShifting(entityLiving)) {
            for (Entity entity : worldIn.getAllEntities()) {
                if (entity instanceof MinionEntity) {
                    MinionEntity minion = (MinionEntity) entity;
                    if (minion instanceof FriendlyVexEntity) {
                        if (minion.getTrueOwner() == entityLiving) {
                            entity.kill();
                        }
                    }
                }
            }
            for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
            }
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, this.getSoundSource(), 1.0F, 1.0F);
        }
    }

    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff) {
        this.commonResult(worldIn, entityLiving);
        if (!isShifting(entityLiving)) {
            int i = 3;
            if (staff){
                i = 3 + worldIn.random.nextInt(3);
            }
            for (int i1 = 0; i1 < i; ++i1) {
                BlockPos blockpos = entityLiving.blockPosition().offset(-2 + entityLiving.getRandom().nextInt(5), 1, -2 + entityLiving.getRandom().nextInt(5));
                FriendlyVexEntity vexentity = new FriendlyVexEntity(ModEntityType.FRIENDLY_VEX.get(), worldIn);
                vexentity.setOwnerId(entityLiving.getUUID());
                vexentity.moveTo(blockpos, 0.0F, 0.0F);
                vexentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(blockpos), SpawnReason.MOB_SUMMONED, null, null);
                vexentity.setBoundOrigin(blockpos);
                int limit = staff ? SpellConfig.StaffVexLimit.get() : SpellConfig.WandVexLimit.get();
                if (limit > VexLimit(entityLiving)) {
                    vexentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                } else {
                    vexentity.setLimitedLife(1);
                    vexentity.addEffect(new EffectInstance(Effects.WITHER, 800, 1));
                }
                if (enchantment > 0) {
                    Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(vexentity.getMainHandItem());
                    map.putIfAbsent(Enchantments.SHARPNESS, enchantment);
                    EnchantmentHelper.setEnchantments(map, vexentity.getMainHandItem());
                    vexentity.setItemSlot(EquipmentSlotType.MAINHAND, vexentity.getMainHandItem());
                }
                this.SummonSap(entityLiving, vexentity);
                this.setTarget(worldIn, entityLiving, vexentity);
                worldIn.addFreshEntity(vexentity);
                this.summonAdvancement(entityLiving, vexentity);
            }
            worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, this.getSoundSource(), 1.0F, 1.0F);
            this.SummonDown(entityLiving);
        }
    }

    public int VexLimit(LivingEntity entityLiving){
        return entityLiving.level.getNearbyEntities(FriendlyVexEntity.class, this.vexCountTargeting, entityLiving, entityLiving.getBoundingBox().inflate(16.0D)).size();
    }
}
