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
        DragonFireballEntity dragonFireball = new DragonFireballEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        dragonFireball.setOwner(entityLiving);
        worldIn.addFreshEntity(dragonFireball);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ENDER_DRAGON_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.DragonFireballInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        DragonFireballEntity dragonFireball = new DragonFireballEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        dragonFireball.setOwner(entityLiving);
        worldIn.addFreshEntity(dragonFireball);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ENDER_DRAGON_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.DragonFireballInfamyChance.get(), (PlayerEntity) entityLiving);
    }
}
