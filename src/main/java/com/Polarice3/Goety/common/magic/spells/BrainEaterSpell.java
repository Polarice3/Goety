package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.magic.ChargingSpells;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

public class BrainEaterSpell extends ChargingSpells {

    @Override
    public int Cooldown() {
        return SpellConfig.BrainEaterDuration.get();
    }

    public int SoulCost() {
        return SpellConfig.BrainEaterCost.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (player.experienceProgress > 0 && player.getHealth() < player.getMaxHealth()){
                player.giveExperiencePoints(-SpellConfig.BrainEaterXPCost.get());
                player.heal(4.0F);
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_DRINK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    worldIn.sendParticles(ParticleTypes.HAPPY_VILLAGER, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                }
                this.IncreaseInfamy(SpellConfig.BrainEaterInfamyChance.get(), (PlayerEntity) entityLiving);
            } else {
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                }
            }
        }

    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (player.experienceProgress > 0 && player.getHealth() < player.getMaxHealth()){
                player.giveExperiencePoints(-SpellConfig.BrainEaterXPCost.get());
                player.heal(8.0F);
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_DRINK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    worldIn.sendParticles(ParticleTypes.HAPPY_VILLAGER, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                }
                this.IncreaseInfamy(SpellConfig.BrainEaterInfamyChance.get(), (PlayerEntity) entityLiving);
            } else {
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
                }
            }
        }

    }


}
