package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class FangSpell extends Spells{

    public int SoulCost() {
        return MainConfig.FangCost.get();
    }

    public int CastDuration() {
        return MainConfig.FangDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_ATTACK;
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving){
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        int range = 16;
        double radius = 2.0D;
        if (WandUtil.enchantedFocus(playerEntity)){
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), playerEntity);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), playerEntity);
        }
        RayTraceResult rayTraceResult = this.rayTrace(worldIn, playerEntity, range, radius);
        Vector3d vector3d = rayTraceResult.getLocation();
        double d0 = Math.min(vector3d.y, entityLiving.getY());
        double d1 = Math.max(vector3d.y, entityLiving.getY()) + 1.0D;
        float f = (float) MathHelper.atan2(vector3d.z - entityLiving.getZ(), vector3d.x - entityLiving.getX());
        if (rayTraceResult instanceof EntityRayTraceResult){
            Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
            d0 = Math.min(target.getY(), entityLiving.getY());
            d1 = Math.max(target.getY(), entityLiving.getY()) + 1.0D;
            f = (float)MathHelper.atan2(target.getZ() - entityLiving.getZ(), target.getX() - entityLiving.getX());
        }
        if (!isShifting(entityLiving)) {
            for (int l = 0; l < range; ++l) {
                double d2 = 1.25D * (double) (l + 1);
                WandUtil.spawnFangs(entityLiving, entityLiving.getX() + (double) MathHelper.cos(f) * d2, entityLiving.getZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, l);
            }
        } else {
            for(int i = 0; i < 5; ++i) {
                float f1 = f + (float)i * (float)Math.PI * 0.4F;
                WandUtil.spawnFangs(entityLiving,entityLiving.getX() + (double)MathHelper.cos(f1) * 1.5D, entityLiving.getZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
            }

            for(int k = 0; k < 8; ++k) {
                float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
                WandUtil.spawnFangs(entityLiving,entityLiving.getX() + (double)MathHelper.cos(f2) * 2.5D, entityLiving.getZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
            }
        }
        this.IncreaseInfamy(MainConfig.FangInfamyChance.get(), (PlayerEntity) entityLiving);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving){
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        int range = 16;
        double radius = 2.0D;
        if (WandUtil.enchantedFocus(playerEntity)){
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), playerEntity);
            radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), playerEntity);
        }
        RayTraceResult rayTraceResult = this.rayTrace(worldIn, playerEntity, range, radius);
        Vector3d vector3d = rayTraceResult.getLocation();
        double d0 = Math.min(vector3d.y, entityLiving.getY());
        double d1 = Math.max(vector3d.y, entityLiving.getY()) + 1.0D;
        float f = (float) MathHelper.atan2(vector3d.z - entityLiving.getZ(), vector3d.x - entityLiving.getX());
        if (rayTraceResult instanceof EntityRayTraceResult){
            Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
            d0 = Math.min(target.getY(), entityLiving.getY());
            d1 = Math.max(target.getY(), entityLiving.getY()) + 1.0D;
            f = (float)MathHelper.atan2(target.getZ() - entityLiving.getZ(), target.getX() - entityLiving.getX());
        }
        if (!isShifting(entityLiving)) {
            for(int l = 0; l < range; ++l) {
                double d2 = 1.25D * (double)(l + 1);
                float fleft = f + 0.4F;
                float fright = f - 0.4F;
                WandUtil.spawnFangs(entityLiving,entityLiving.getX() + (double)MathHelper.cos(f) * d2, entityLiving.getZ() + (double)MathHelper.sin(f) * d2, d0, d1, f, l);
                WandUtil.spawnFangs(entityLiving,entityLiving.getX() + (double)MathHelper.cos(fleft) * d2, entityLiving.getZ() + (double)MathHelper.sin(fleft) * d2, d0, d1, fleft, l);
                WandUtil.spawnFangs(entityLiving,entityLiving.getX() + (double)MathHelper.cos(fright) * d2, entityLiving.getZ() + (double)MathHelper.sin(fright) * d2, d0, d1, fright, l);
            }
        } else {
            for(int i = 0; i < 5; ++i) {
                float f1 = f + (float)i * (float)Math.PI * 0.4F;
                WandUtil.spawnFangs(entityLiving,entityLiving.getX() + (double)MathHelper.cos(f1) * 1.5D, entityLiving.getZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
            }

            for(int k = 0; k < 8; ++k) {
                float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + 1.2566371F;
                WandUtil.spawnFangs(entityLiving,entityLiving.getX() + (double)MathHelper.cos(f2) * 2.5D, entityLiving.getZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
            }

            for(int k = 0; k < 11; ++k) {
                float f2 = f + (float)k * (float)Math.PI * 4.0F / 16.0F + 2.5133462F;
                WandUtil.spawnFangs(entityLiving,entityLiving.getX() + (double)MathHelper.cos(f2) * 3.5D, entityLiving.getZ() + (double)MathHelper.sin(f2) * 3.5D, d0, d1, f2, 6);
            }

            for(int k = 0; k < 14; ++k) {
                float f2 = f + (float)k * (float)Math.PI * 8.0F / 32.0F + 5.0266924F;
                WandUtil.spawnFangs(entityLiving,entityLiving.getX() + (double)MathHelper.cos(f2) * 4.5D, entityLiving.getZ() + (double)MathHelper.sin(f2) * 4.5D, d0, d1, f2, 9);
            }
        }
        this.IncreaseInfamy(MainConfig.FangInfamyChance.get(), (PlayerEntity) entityLiving);
        worldIn.playSound((PlayerEntity) null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }

}
