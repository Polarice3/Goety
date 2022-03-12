package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class RoarSpell extends Spells {
    private static final Predicate<Entity> field_213690_b = Entity::isAlive;

    public int SoulCost() {
        return MainConfig.RoarCost.get();
    }

    public int CastDuration() {
        return MainConfig.RoarDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        for(Entity entity : worldIn.getEntitiesOfClass(LivingEntity.class, entityLiving.getBoundingBox().inflate(8.0D), field_213690_b)) {
            if (!(entity == entityLiving)) {
                entity.hurt(DamageSource.mobAttack(entityLiving), 3.0F);
                this.launch(entity, entityLiving);
            }

        }

        Vector3d vector3d = entityLiving.getBoundingBox().getCenter();

        for(int i = 0; i < 40; ++i) {
            double d0 = worldIn.random.nextGaussian() * 0.2D;
            double d1 = worldIn.random.nextGaussian() * 0.2D;
            double d2 = worldIn.random.nextGaussian() * 0.2D;
            new ParticleUtil(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.RAVAGER_ROAR, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            new ParticleUtil(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0.0F, 0.0F, 0.0F);
        }
        this.IncreaseInfamy(MainConfig.RoarInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        for(Entity entity : worldIn.getEntitiesOfClass(LivingEntity.class, entityLiving.getBoundingBox().inflate(16.0D), field_213690_b)) {
            if (!(entity == entityLiving)) {
                entity.hurt(DamageSource.mobAttack(entityLiving), 6.0F);
                this.superlaunch(entity, entityLiving);
            }

        }

        Vector3d vector3d = entityLiving.getBoundingBox().getCenter();

        for(int i = 0; i < 40; ++i) {
            double d0 = worldIn.random.nextGaussian() * 0.2D;
            double d1 = worldIn.random.nextGaussian() * 0.2D;
            double d2 = worldIn.random.nextGaussian() * 0.2D;
            new ParticleUtil(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.RAVAGER_ROAR, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            double d = worldIn.random.nextGaussian() * 0.2D;
            new ParticleUtil(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), d, d, d);
        }
        this.IncreaseInfamy(MainConfig.RoarInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    private void superlaunch(Entity p_213688_1_, LivingEntity livingEntity) {
        double d0 = p_213688_1_.getX() - livingEntity.getX();
        double d1 = p_213688_1_.getZ() - livingEntity.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.push(d0 / d2 * 8.0D, 0.2D, d1 / d2 * 8.0D);
    }

    private void launch(Entity p_213688_1_, LivingEntity livingEntity) {
        double d0 = p_213688_1_.getX() - livingEntity.getX();
        double d1 = p_213688_1_.getZ() - livingEntity.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        p_213688_1_.push(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }
}
