package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

public class FireballSpell extends InstantCastSpells{

    @Override
    public int SoulCost() {
        return MainConfig.FireballCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.BLAZE_SHOOT;
    }

    @Override
    public void WandResult(World worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        Random random = worldIn.random;
        double d2 = random.nextGaussian() * 0.01D + vector3d.x;
        double d3 = random.nextGaussian() * 0.01D + vector3d.y;
        double d4 = random.nextGaussian() * 0.01D + vector3d.z;
        SmallFireballEntity smallFireballEntity = new SmallFireballEntity(worldIn, entityLiving, d2, d3, d4);
        smallFireballEntity.setOwner(entityLiving);
        smallFireballEntity.setPos(entityLiving.getX() + vector3d.x * 2.0D, entityLiving.getY(0.5D) + 0.5D, entityLiving.getZ() + vector3d.z * 2.0D);
        worldIn.addFreshEntity(smallFireballEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.FireballInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    @Override
    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        Random random = worldIn.random;
        double d2 = random.nextGaussian() * 0.01D + vector3d.x;
        double d3 = random.nextGaussian() * 0.01D + vector3d.y;
        double d4 = random.nextGaussian() * 0.01D + vector3d.z;
        for(int i = 0; i < 3; ++i) {
            SmallFireballEntity smallFireballEntity = new SmallFireballEntity(worldIn, entityLiving, d2, d3, d4);
            smallFireballEntity.setOwner(entityLiving);
            smallFireballEntity.setPos(entityLiving.getX() + vector3d.x * 2.0D, entityLiving.getY(0.5D) + 0.5D, entityLiving.getZ() + vector3d.z * 2.0D);
            worldIn.addFreshEntity(smallFireballEntity);
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.FireballInfamyChance.get(), (PlayerEntity) entityLiving);
    }
}
