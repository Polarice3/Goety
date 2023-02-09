package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.entities.projectiles.ModFireballEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

/**
 * Learned you could use this method for better projectile accuracy from codes by @Yunus1903
 */
public class FireballSpell extends InstantCastSpells{

    @Override
    public int SoulCost() {
        return SpellConfig.FireballCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.BLAZE_SHOOT;
    }

    @Override
    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        ModFireballEntity smallFireballEntity = new ModFireballEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        smallFireballEntity.setOwner(entityLiving);
        worldIn.addFreshEntity(smallFireballEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(SpellConfig.FireballInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    @Override
    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        ModFireballEntity smallFireballEntity = new ModFireballEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        smallFireballEntity.setOwner(entityLiving);
        worldIn.addFreshEntity(smallFireballEntity);
        for(int i = 0; i < 2; ++i) {
            ModFireballEntity smallFireballEntity2 = new ModFireballEntity(worldIn,
                    entityLiving.getX() + vector3d.x / 2 + worldIn.random.nextGaussian(),
                    entityLiving.getEyeY() - 0.2,
                    entityLiving.getZ() + vector3d.z / 2 + worldIn.random.nextGaussian(),
                    vector3d.x,
                    vector3d.y,
                    vector3d.z);
            smallFireballEntity2.setOwner(entityLiving);
            worldIn.addFreshEntity(smallFireballEntity2);
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(SpellConfig.FireballInfamyChance.get(), (PlayerEntity) entityLiving);
    }
}
