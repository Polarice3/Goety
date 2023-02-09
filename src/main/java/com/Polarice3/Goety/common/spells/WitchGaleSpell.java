package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.WitchGaleEntity;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class WitchGaleSpell extends Spells{

    public int SoulCost() {
        return SpellConfig.WitchGaleCost.get();
    }

    public int CastDuration() {
        return SpellConfig.WitchGaleDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        int enchantment = 1;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.RANGE.get(), player) + 1;
            }
            this.IncreaseInfamy(SpellConfig.WitchGaleInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        Random random = worldIn.random;
        double d2 = random.nextGaussian() * 0.01D + vector3d.x;
        double d3 = random.nextGaussian() * 0.01D + vector3d.y;
        double d4 = random.nextGaussian() * 0.01D + vector3d.z;
        WitchGaleEntity dragonFireball = new WitchGaleEntity(worldIn, entityLiving, d2, d3, d4);
        dragonFireball.setOwnerId(entityLiving.getUUID());
        dragonFireball.setTotallife(60 * enchantment);
        dragonFireball.setPos(entityLiving.getX() + vector3d.x * 2.0D, entityLiving.getY(0.5D), entityLiving.getZ() + vector3d.z * 2.0D);
        worldIn.addFreshEntity(dragonFireball);
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.PHANTOM_FLAP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            worldIn.sendParticles(ParticleTypes.WITCH, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        int enchantment = 1;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.RANGE.get(), player) + 1;
            }
            this.IncreaseInfamy(SpellConfig.WitchGaleInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        Random random = worldIn.random;
        double d2 = random.nextGaussian() * 0.01D + vector3d.x;
        double d3 = random.nextGaussian() * 0.01D + vector3d.y;
        double d4 = random.nextGaussian() * 0.01D + vector3d.z;
        WitchGaleEntity dragonFireball = new WitchGaleEntity(worldIn, entityLiving, d2, d3, d4);
        dragonFireball.setOwnerId(entityLiving.getUUID());
        dragonFireball.setUpgraded(true);
        dragonFireball.setTotallife(120 * enchantment);
        dragonFireball.setPos(entityLiving.getX() + vector3d.x * 2.0D, entityLiving.getY(0.5D), entityLiving.getZ() + vector3d.z * 2.0D);
        worldIn.addFreshEntity(dragonFireball);
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.PHANTOM_FLAP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            worldIn.sendParticles(ParticleTypes.WITCH, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 1, 0.0F, 0.0F, 0.0F, 0);
        }
    }

}
