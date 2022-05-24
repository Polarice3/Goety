package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

public class LavaballSpell extends Spells{

    @Override
    public int SoulCost() {
        return MainConfig.LavaballCost.get();
    }

    public int CastDuration() {
        return MainConfig.LavaballDuration.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_ATTACK;
    }

    @Override
    public void WandResult(World worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        FireballEntity fireballEntity = new FireballEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        fireballEntity.setOwner(entityLiving);
        worldIn.addFreshEntity(fireballEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GHAST_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.LavaballInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    @Override
    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        FireballEntity fireballEntity = new FireballEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        fireballEntity.setOwner(entityLiving);
        worldIn.addFreshEntity(fireballEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.GHAST_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.LavaballInfamyChance.get(), (PlayerEntity) entityLiving);
    }
}
