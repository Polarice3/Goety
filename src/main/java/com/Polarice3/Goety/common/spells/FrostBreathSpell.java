package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

public class FrostBreathSpell extends SpewingSpell{

    @Override
    public int SoulCost() {
        return MainConfig.FrostBreathCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.SNOW_GOLEM_AMBIENT;
    }

    @Override
    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        float enchantment = 0;
        int duration = 1;
        int range = 0;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), player);
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
            this.IncreaseInfamy(MainConfig.FrostBreathInfamyChance.get(), player);
        }
        if (!worldIn.isClientSide) {
            for (Entity target : getTarget(entityLiving, range + 15)) {
                if (target != null) {
                    if (target instanceof LivingEntity) {
                        LivingEntity livingTarget = (LivingEntity) target;
                        if (livingTarget.hurt(ModDamageSource.frostBreath(entityLiving), 1.0F + enchantment)) {
                            livingTarget.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100 * duration));
                        }
                    }
                }
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.PLAYER_BREATH, SoundCategory.NEUTRAL, worldIn.random.nextFloat() * 0.5F, worldIn.random.nextFloat() * 0.5F);
    }

    @Override
    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        float enchantment = 0;
        int duration = 1;
        int range = 0;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), player);
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
            this.IncreaseInfamy(MainConfig.FrostBreathInfamyChance.get(), player);
        }
        if (!worldIn.isClientSide) {
            for (Entity target : getTarget(entityLiving, range + 18)) {
                if (target != null) {
                    if (target instanceof LivingEntity) {
                        LivingEntity livingTarget = (LivingEntity) target;
                        if (livingTarget.hurt(ModDamageSource.frostBreath(entityLiving), 2.0F + enchantment)) {
                            livingTarget.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100 * duration));
                        }
                    }
                }
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.PLAYER_BREATH, SoundCategory.NEUTRAL, worldIn.random.nextFloat() * 0.5F, worldIn.random.nextFloat() * 0.5F);
    }

    @Override
    public IParticleData getParticle() {
        return ParticleTypes.POOF;
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
