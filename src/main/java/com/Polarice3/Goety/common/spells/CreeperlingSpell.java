package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.CreeperlingMinionEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;

public class CreeperlingSpell extends ChargingSpells{

    @Override
    public int Cooldown() {
        return MainConfig.CreeperlingDuration.get();
    }

    public int SoulCost() {
        return MainConfig.CreeperlingCost.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.blockPosition();
        CreeperlingMinionEntity summonedentity = new CreeperlingMinionEntity(ModEntityType.CREEPERLING_MINION.get(), worldIn);
        summonedentity.setOwnerId(entityLiving.getUUID());
        summonedentity.moveTo(blockpos, 0.0F, 0.0F);
        if (RobeArmorFinder.FindFelSet(entityLiving)) {
            Collection<EffectInstance> collection = entityLiving.getActiveEffects();
            if (!collection.isEmpty()) {
                for (EffectInstance effectinstance : collection) {
                    if (!effectinstance.getEffect().isBeneficial()) {
                        summonedentity.addEffect(new EffectInstance(effectinstance));
                    }
                }
            }
        }
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                int enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                int duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
                if (enchantment != 0){
                    summonedentity.setExplosionRadius(MainConfig.CreeperlingExplosionRadius.get() + (enchantment/2.5));
                }
                summonedentity.setLimitedLife(180 * duration);
            }
            this.IncreaseInfamy(MainConfig.CreeperlingInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        worldIn.addFreshEntity(summonedentity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.blockPosition();
        for(int i1 = 0; i1 < 1 + entityLiving.level.random.nextInt(3); ++i1) {
            CreeperlingMinionEntity summonedentity = new CreeperlingMinionEntity(ModEntityType.CREEPERLING_MINION.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUUID());
            summonedentity.setUpgraded();
            summonedentity.moveTo(blockpos, 0.0F, 0.0F);
            if (RobeArmorFinder.FindFelSet(entityLiving)) {
                Collection<EffectInstance> collection = entityLiving.getActiveEffects();
                if (!collection.isEmpty()) {
                    for (EffectInstance effectinstance : collection) {
                        if (!effectinstance.getEffect().isBeneficial()) {
                            summonedentity.addEffect(new EffectInstance(effectinstance));
                        }
                    }
                }
            }
            if (entityLiving instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) entityLiving;
                if (WandUtil.enchantedFocus(player)){
                    int enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                    int duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
                    if (enchantment != 0){
                        summonedentity.setExplosionRadius(MainConfig.CreeperlingExplosionRadius.get() + (enchantment/2.5));
                    }
                    summonedentity.setLimitedLife(180 * duration);
                }
                this.IncreaseInfamy(MainConfig.CreeperlingInfamyChance.get(), (PlayerEntity) entityLiving);
            }
            worldIn.addFreshEntity(summonedentity);
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }


}
