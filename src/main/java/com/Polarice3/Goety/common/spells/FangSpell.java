package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

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

    public void WandResult(World worldIn, LivingEntity entityLiving){
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        RayTraceResult rayTraceResult = rayTrace(worldIn, playerEntity, RayTraceContext.FluidMode.NONE);
        Vector3d vector3d = rayTraceResult.getLocation();
        double d0 = Math.min(vector3d.y, entityLiving.getY());
        double d1 = Math.max(vector3d.y, entityLiving.getY()) + 1.0D;
        float f = (float) MathHelper.atan2(vector3d.z - entityLiving.getZ(), vector3d.x - entityLiving.getX());
        int range = 16;
        if (WandUtil.enchantedFocus(playerEntity)){
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), playerEntity);
        }
        if (!playerEntity.isCrouching()) {
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

    public void StaffResult(World worldIn, LivingEntity entityLiving){
        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        RayTraceResult rayTraceResult = rayTrace(worldIn, playerEntity, RayTraceContext.FluidMode.NONE);
        Vector3d vector3d = rayTraceResult.getLocation();
        double d0 = Math.min(vector3d.y, entityLiving.getY());
        double d1 = Math.max(vector3d.y, entityLiving.getY()) + 1.0D;
        float f = (float) MathHelper.atan2(vector3d.z - entityLiving.getZ(), vector3d.x - entityLiving.getX());
        int range = 16;
        if (WandUtil.enchantedFocus(playerEntity)){
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), playerEntity);
        }
        if (!playerEntity.isCrouching()) {
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

    protected static BlockRayTraceResult rayTrace(World worldIn, PlayerEntity player, RayTraceContext.FluidMode fluidMode) {
        float f = player.xRot;
        float f1 = player.yRot;
        Vector3d vector3d = player.getEyePosition(1.0F);
        float f2 = MathHelper.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * ((float)Math.PI / 180F));
        float f5 = MathHelper.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getAttribute(net.minecraftforge.common.ForgeMod.REACH_DISTANCE.get()).getValue();
        Vector3d vector3d1 = vector3d.add((double)f6 * d0, (double)f5 * d0, (double)f7 * d0);
        return worldIn.clip(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.OUTLINE, fluidMode, player));
    }

}
