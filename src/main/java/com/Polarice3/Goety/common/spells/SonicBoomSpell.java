package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class SonicBoomSpell extends Spells{

    public int SoulCost() {
        return MainConfig.SonicBoomCost.get();
    }

    public int CastDuration() {
        return MainConfig.SonicBoomDuration.get();
    }

    public SoundEvent CastingSound() {
        return ModSounds.SONIC_CHARGE.get();
    }

    @Override
    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        Vector3d srcVec = new Vector3d(entityLiving.getX(), entityLiving.getEyeY(), entityLiving.getZ());
        Vector3d lookVec = entityLiving.getViewVector(1.0F);
        Vector3d destVec = srcVec.add(lookVec.x * 15, lookVec.y * 15, lookVec.z * 15);
        for(int i = 1; i < Math.floor(destVec.length()) + 7; ++i) {
            Vector3d vector3d2 = srcVec.add(lookVec.scale((double)i));
            worldIn.sendParticles(ModParticleTypes.SONIC_BOOM.get(), vector3d2.x, vector3d2.y, vector3d2.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
        for (Entity target: getTargets(entityLiving, 15, 1.0F)) {
            if (!worldIn.isClientSide) {
                if (target instanceof LivingEntity) {
                    LivingEntity target1 = (LivingEntity) target;
                    target1.hurt(ModDamageSource.sonicBoom(entityLiving), 10.0F);
                    double d0 = target1.getX() - entityLiving.getX();
                    double d1 = target1.getZ() - entityLiving.getZ();
                    double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                    MobUtil.push(target1, d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
                }
            }
        }
        this.IncreaseInfamy(MainConfig.SonicBoomInfamyChance.get(), (PlayerEntity) entityLiving);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.ROAR_SPELL.get(), SoundCategory.NEUTRAL, 3.0F, 0.25F);
    }

    @Override
    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        this.WandResult(worldIn, entityLiving);
    }

    public List<Entity> getTargets(LivingEntity livingEntity, double range, float size) {
        List<Entity> target = new ArrayList<>();
        Vector3d lookVec = livingEntity.getViewVector(1.0F);
        List<Entity> entities = livingEntity.level.getEntities(livingEntity, livingEntity.getBoundingBox().expandTowards(lookVec.x * range, lookVec.y * range, lookVec.z * range).inflate(size, size, size));

        for (Entity entity : entities) {
            if (entity.isPickable()) {
                target.add(entity);
            }
        }
        return target;
    }


}
