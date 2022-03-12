package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

public class DragonFireballSpell extends Spells{

    public int SoulCost() {
        return MainConfig.DragonFireballCost.get();
    }

    public int CastDuration() {
        return MainConfig.DragonFireballDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ENDER_DRAGON_GROWL;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        Random random = worldIn.random;
        double d2 = random.nextGaussian() * 0.01D + vector3d.x;
        double d3 = random.nextGaussian() * 0.01D + vector3d.y;
        double d4 = random.nextGaussian() * 0.01D + vector3d.z;
        DragonFireballEntity dragonFireball = new DragonFireballEntity(worldIn, entityLiving, d2, d3, d4);
        dragonFireball.setOwner(entityLiving);
        dragonFireball.setPos(entityLiving.getX() + vector3d.x * 2.0D, entityLiving.getY(0.5D), entityLiving.getZ() + vector3d.z * 2.0D);
        worldIn.addFreshEntity(dragonFireball);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ENDER_DRAGON_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.DragonFireballInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        Random random = worldIn.random;
        double d2 = random.nextGaussian() * 0.01D + vector3d.x;
        double d3 = random.nextGaussian() * 0.01D + vector3d.y;
        double d4 = random.nextGaussian() * 0.01D + vector3d.z;
        DragonFireballEntity dragonFireball = new DragonFireballEntity(worldIn, entityLiving, d2, d3, d4);
        dragonFireball.setOwner(entityLiving);
        dragonFireball.setPos(entityLiving.getX() + vector3d.x * 2.0D, entityLiving.getY(0.5D), entityLiving.getZ() + vector3d.z * 2.0D);
        worldIn.addFreshEntity(dragonFireball);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ENDER_DRAGON_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.DragonFireballInfamyChance.get(), (PlayerEntity) entityLiving);
    }
}
