package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class BrainEaterSpell extends ChargingSpells{

    @Override
    public int Cooldown() {
        return MainConfig.BrainEaterDuration.get();
    }

    public int SoulCost() {
        return MainConfig.BrainEaterCost.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (player.experienceProgress > 0 && player.getHealth() < player.getMaxHealth()){
                player.giveExperiencePoints(-MainConfig.BrainEaterXPCost.get());
                player.heal(4.0F);
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_DRINK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    new ParticleUtil(ParticleTypes.HAPPY_VILLAGER, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
                }
                this.IncreaseInfamy(MainConfig.BrainEaterInfamyChance.get(), (PlayerEntity) entityLiving);
            } else {
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    new ParticleUtil(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
                }
            }
        }

    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (player.experienceProgress > 0 && player.getHealth() < player.getMaxHealth()){
                player.giveExperiencePoints(-MainConfig.BrainEaterXPCost.get());
                player.heal(8.0F);
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_DRINK, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    new ParticleUtil(ParticleTypes.HAPPY_VILLAGER, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
                }
                this.IncreaseInfamy(MainConfig.BrainEaterInfamyChance.get(), (PlayerEntity) entityLiving);
            } else {
                worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
                    new ParticleUtil(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
                }
            }
        }

    }


}
