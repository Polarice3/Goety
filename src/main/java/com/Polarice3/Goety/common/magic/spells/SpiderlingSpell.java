package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.ally.SpiderlingMinionEntity;
import com.Polarice3.Goety.common.magic.ChargingSpells;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class SpiderlingSpell extends ChargingSpells {

    @Override
    public int Cooldown() {
        return SpellConfig.SpiderlingDuration.get();
    }

    public int SoulCost() {
        return SpellConfig.SpiderlingCost.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_SUMMON;
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.blockPosition();
        SpiderlingMinionEntity summonedentity = new SpiderlingMinionEntity(ModEntityType.SPIDERLING_MINION.get(), worldIn);
        summonedentity.setOwnerId(entityLiving.getUUID());
        summonedentity.setUpgraded(this.ArachnoPower(entityLiving));
        summonedentity.moveTo(blockpos, 0.0F, 0.0F);
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                int enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                int duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
                if (enchantment != 0){
                    summonedentity.addEffect(new EffectInstance(ModEffects.BUFF.get(), Integer.MAX_VALUE, enchantment - 1, false, false));
                }
                summonedentity.setLimitedLife(180 * duration);
            }
            this.IncreaseInfamy(SpellConfig.SpiderlingInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
        worldIn.addFreshEntity(summonedentity);
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        BlockPos blockpos = entityLiving.blockPosition();
        for(int i1 = 0; i1 < 2 + entityLiving.level.random.nextInt(4); ++i1) {
            SpiderlingMinionEntity summonedentity = new SpiderlingMinionEntity(ModEntityType.SPIDERLING_MINION.get(), worldIn);
            summonedentity.setOwnerId(entityLiving.getUUID());
            summonedentity.setUpgraded(this.ArachnoPower(entityLiving));
            summonedentity.moveTo(blockpos, 0.0F, 0.0F);
            if (entityLiving instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) entityLiving;
                if (WandUtil.enchantedFocus(player)){
                    int enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                    int duration = WandUtil.getLevels(ModEnchantments.DURATION.get(), player) + 1;
                    if (enchantment != 0){
                        summonedentity.addEffect(new EffectInstance(ModEffects.BUFF.get(), Integer.MAX_VALUE, enchantment - 1, false, false));
                    }
                    summonedentity.setLimitedLife(180 * duration);
                }
                this.IncreaseInfamy(SpellConfig.SpiderlingInfamyChance.get(), (PlayerEntity) entityLiving);
            }
            summonedentity.finalizeSpawn(worldIn, entityLiving.level.getCurrentDifficultyAt(entityLiving.blockPosition()), SpawnReason.MOB_SUMMONED, (ILivingEntityData) null, (CompoundNBT) null);
            worldIn.addFreshEntity(summonedentity);
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }


}
