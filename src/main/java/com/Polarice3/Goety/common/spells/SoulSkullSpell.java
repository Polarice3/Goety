package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.entities.projectiles.SoulSkullEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class SoulSkullSpell extends InstantCastSpells {

    @Override
    public int SoulCost() {
        return SpellConfig.SoulSkullCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.WITHER_SHOOT;
    }

    @Override
    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        SoulSkullEntity soulSkullEntity = new SoulSkullEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        if (isShifting(entityLiving)){
            soulSkullEntity.setDangerous(true);
        }
        soulSkullEntity.setOwner(entityLiving);
        worldIn.addFreshEntity(soulSkullEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(SpellConfig.SoulSkullInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    @Override
    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        SoulSkullEntity soulSkullEntity = new SoulSkullEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        if (isShifting(entityLiving)){
            soulSkullEntity.setDangerous(true);
        }
        soulSkullEntity.setOwner(entityLiving);
        worldIn.addFreshEntity(soulSkullEntity);
        for(int i = 0; i < 2; ++i) {
            SoulSkullEntity soulSkullEntity1 = new SoulSkullEntity(worldIn,
                    entityLiving.getX() + vector3d.x / 2 + worldIn.random.nextGaussian(),
                    entityLiving.getEyeY() - 0.2,
                    entityLiving.getZ() + vector3d.z / 2 + worldIn.random.nextGaussian(),
                    vector3d.x,
                    vector3d.y,
                    vector3d.z);
            if (isShifting(entityLiving)){
                soulSkullEntity1.setDangerous(true);
            }
            soulSkullEntity1.setOwner(entityLiving);
            worldIn.addFreshEntity(soulSkullEntity1);
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(SpellConfig.SoulSkullInfamyChance.get(), (PlayerEntity) entityLiving);
    }
}
