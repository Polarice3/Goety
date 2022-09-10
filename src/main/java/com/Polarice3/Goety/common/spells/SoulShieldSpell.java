package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

public class SoulShieldSpell extends Spells{

    public int SoulCost() {
        return MainConfig.SoulShieldCost.get();
    }

    public int CastDuration() {
        return MainConfig.SoulShieldDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        int duration = 1;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
            }
            this.IncreaseInfamy(MainConfig.SoulShieldInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        entityLiving.addEffect(new EffectInstance(ModEffects.SOUL_SHIELD.get(), 1200 * duration));
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.WITHER_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        int duration = 1;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
            }
            this.IncreaseInfamy(MainConfig.SoulShieldInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        entityLiving.addEffect(new EffectInstance(ModEffects.SOUL_SHIELD.get(), 2400 * duration));
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.WITHER_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for (int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
    }
}
