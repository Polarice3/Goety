package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.utils.ModDamageSource;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class FireBreathSpell extends SpewingSpell{

    @Override
    public int SoulCost() {
        return MainConfig.FireBreathCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    @Override
    public void WandResult(World worldIn, LivingEntity entityLiving) {
        float enchantment = 0;
        int burning = 1;
        int range = 0;
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)) {
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                burning += WandUtil.getLevels(ModEnchantments.BURNING.get(), player);
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
            this.IncreaseInfamy(MainConfig.FireBreathInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        Entity target = getTarget(entityLiving, range + 15, 1.0F);
        if (!worldIn.isClientSide) {
            if (target != null) {
                if (!target.fireImmune()) {
                    target.setSecondsOnFire(5 * burning);
                    target.hurt(ModDamageSource.fireBreath(entityLiving), 2.0F + enchantment);
                }
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.BLAZE_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    @Override
    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        float enchantment = 0;
        int burning = 1;
        int range = 0;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                burning += WandUtil.getLevels(ModEnchantments.BURNING.get(), player);
                range = WandUtil.getLevels(ModEnchantments.RANGE.get(), player);
            }
            this.IncreaseInfamy(MainConfig.FireBreathInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        Entity target = getTarget(entityLiving, range + 18, 2.0F);
        if (!worldIn.isClientSide) {
            if (target != null) {
                if (!target.fireImmune()) {
                    target.setSecondsOnFire(8 * burning);
                    target.hurt(ModDamageSource.fireBreath(entityLiving), 4.0F + enchantment);
                }
            }
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.BLAZE_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    @Override
    public IParticleData getParticle() {
        return ParticleTypes.FLAME;
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
