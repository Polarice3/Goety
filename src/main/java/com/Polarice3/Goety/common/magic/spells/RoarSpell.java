package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spells;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.function.Predicate;

public class RoarSpell extends Spells {
    private static final Predicate<Entity> field_213690_b = Entity::isAlive;
    public float damage = SpellConfig.RoarDamage.get().floatValue() * SpellConfig.SpellDamageMultiplier.get();

    public int SoulCost() {
        return SpellConfig.RoarCost.get();
    }

    public int CastDuration() {
        return SpellConfig.RoarDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        float radius = 0;
        float enchantment = 0;
        boolean flaming = false;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                radius = WandUtil.getLevels(ModEnchantments.RADIUS.get(), player);
                flaming = WandUtil.getLevels(ModEnchantments.BURNING.get(), player) > 0;
            }
        }
        for(Entity entity : worldIn.getEntitiesOfClass(LivingEntity.class, entityLiving.getBoundingBox().inflate(4.0D + radius), field_213690_b)) {
            if (!(entity == entityLiving)) {
                entity.hurt(DamageSource.mobAttack(entityLiving), damage + enchantment);
                if (flaming){
                    entity.setSecondsOnFire(30);
                }
                this.launch(entity, entityLiving);
            }

        }

        Vector3d vector3d = entityLiving.getBoundingBox().getCenter();

        for(int i = 0; i < 40; ++i) {
            double d0 = worldIn.random.nextGaussian() * 0.2D;
            double d1 = worldIn.random.nextGaussian() * 0.2D;
            double d2 = worldIn.random.nextGaussian() * 0.2D;
            if (flaming){
                worldIn.sendParticles(ParticleTypes.FLAME, vector3d.x, vector3d.y, vector3d.z, 0, d0, d1, d2, 0.5F);
            } else {
                worldIn.sendParticles(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, 0, d0, d1, d2, 0.5F);
            }
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.ROAR_SPELL.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0, 0.0F, 0.0F, 0.0F, 0);
        }
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        float radius = 0;
        float enchantment = 0;
        boolean flaming = false;
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.POTENCY.get(), player);
                radius = WandUtil.getLevels(ModEnchantments.RADIUS.get(), player);
                flaming = WandUtil.getLevels(ModEnchantments.BURNING.get(), player) > 0;
            }
        }
        for(Entity entity : worldIn.getEntitiesOfClass(LivingEntity.class, entityLiving.getBoundingBox().inflate(8.0D + radius), field_213690_b)) {
            if (!(entity == entityLiving)) {
                entity.hurt(DamageSource.mobAttack(entityLiving), (damage * 2) + enchantment);
                if (flaming){
                    entity.setSecondsOnFire(60);
                }
                this.launch(entity, entityLiving);
            }

        }

        Vector3d vector3d = entityLiving.getBoundingBox().getCenter();

        for(int i = 0; i < 40; ++i) {
            double d0 = worldIn.random.nextGaussian() * 0.2D;
            double d1 = worldIn.random.nextGaussian() * 0.2D;
            double d2 = worldIn.random.nextGaussian() * 0.2D;
            if (flaming){
                worldIn.sendParticles(ParticleTypes.FLAME, vector3d.x, vector3d.y, vector3d.z, 0, d0, d1, d2, 0.5F);
            } else {
                worldIn.sendParticles(ParticleTypes.POOF, vector3d.x, vector3d.y, vector3d.z, 0, d0, d1, d2, 0.5F);
            }
        }
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.ROAR_SPELL.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        for(int i = 0; i < entityLiving.level.random.nextInt(35) + 10; ++i) {
            double d = worldIn.random.nextGaussian() * 0.2D;
            worldIn.sendParticles(ParticleTypes.POOF, entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ(), 0, d, d, d, 0.5F);
        }
    }

    private void launch(Entity p_213688_1_, LivingEntity livingEntity) {
        double d0 = p_213688_1_.getX() - livingEntity.getX();
        double d1 = p_213688_1_.getZ() - livingEntity.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        MobUtil.push(p_213688_1_, d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }
}
