package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.server.ServerWorld;

public class AcidBreathSpell extends SpewingSpell{

    @Override
    public int SoulCost() {
        return MainConfig.EnderAcidCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.FIRE_BREATH.get();
    }

    @Override
    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        int enchantment = 0;
        int range = 0;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
            this.IncreaseInfamy(MainConfig.EnderAcidInfamyChance.get(), player);
        }
        Entity target = getTarget(entityLiving, range + 15, 1.0F);
        if (!worldIn.isClientSide) {
            if (target != null) {
                if (target instanceof LivingEntity) {
                    LivingEntity livingTarget = (LivingEntity) target;
                    if (livingTarget.canBeAffected(new EffectInstance(Effects.HARM))) {
                        if (livingTarget.isInvertedHealAndHarm()) {
                            livingTarget.heal((float) (3 << enchantment));
                        } else {
                            livingTarget.hurt(DamageSource.indirectMagic(entityLiving, entityLiving), (float) (3 << enchantment));
                        }
                    }
                }
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.FIRE_BREATH.get(), SoundCategory.NEUTRAL, worldIn.random.nextFloat() * 0.5F, worldIn.random.nextFloat() * 0.5F);
    }

    @Override
    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        int enchantment = 0;
        int range = 0;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
            this.IncreaseInfamy(MainConfig.EnderAcidInfamyChance.get(), player);
        }
        Entity target = getTarget(entityLiving, range + 18, 2.0F);
        if (!worldIn.isClientSide) {
            if (target != null) {
                if (target instanceof LivingEntity) {
                    LivingEntity livingTarget = (LivingEntity) target;
                    if (livingTarget.canBeAffected(new EffectInstance(Effects.HARM))) {
                        if (livingTarget.isInvertedHealAndHarm()) {
                            livingTarget.heal((float) (3 << enchantment));
                        } else {
                            livingTarget.hurt(DamageSource.indirectMagic(entityLiving, entityLiving), (float) (3 << enchantment));
                        }
                    }
                }
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.FIRE_BREATH.get(), SoundCategory.NEUTRAL, worldIn.random.nextFloat() * 0.5F, worldIn.random.nextFloat() * 0.5F);
    }

    @Override
    public IParticleData getParticle() {
        return ParticleTypes.DRAGON_BREATH;
    }

    @Override
    public void showWandBreath(LivingEntity entityLiving) {
        int range = 0;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
        }
        this.breathAttack(entityLiving, 0.3F + ((double) range / 10), 5);
    }

    @Override
    public void showStaffBreath(LivingEntity entityLiving) {
        int range = 0;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
        }
        this.breathAttack(entityLiving, 0.6F + ((double) range / 10), 10);
    }
}
