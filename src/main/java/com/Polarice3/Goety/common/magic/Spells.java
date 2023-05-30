package com.Polarice3.Goety.common.magic;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.capabilities.infamy.IInfamy;
import com.Polarice3.Goety.utils.InfamyHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public abstract class Spells {

    public Spells(){
    }

    public abstract int SoulCost();

    public abstract int CastDuration();

    public abstract SoundEvent CastingSound();

    public abstract void WandResult(ServerWorld worldIn, LivingEntity entityLiving);

    public abstract void StaffResult(ServerWorld worldIn, LivingEntity entityLiving);

    public boolean isShifting(LivingEntity entityLiving){
        return entityLiving.isCrouching() || entityLiving.isShiftKeyDown();
    }

    public void IncreaseInfamy(int random, PlayerEntity player){
        if (MainConfig.InfamySpell.get()){
            if (random != 0) {
                int random2 = player.level.random.nextInt(random);
                if (random2 == 0) {
                    IInfamy infamy1 = InfamyHelper.getCapability(player);
                    infamy1.increaseInfamy(MainConfig.InfamySpellGive.get());
                }
            }
        }
    }

    protected RayTraceResult rayTrace(World worldIn, LivingEntity livingEntity, int range, double radius) {
        if (this.entityResult(worldIn, livingEntity, range, radius) == null){
            return this.blockResult(worldIn, livingEntity, range);
        } else {
            return this.entityResult(worldIn, livingEntity, range, radius);
        }
    }

    protected BlockRayTraceResult blockResult(World worldIn, LivingEntity livingEntity, double range) {
        float f = livingEntity.xRot;
        float f1 = livingEntity.yRot;
        Vector3d vector3d = livingEntity.getEyePosition(1.0F);
        float f2 = MathHelper.cos(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * ((float)Math.PI / 180F) - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * ((float)Math.PI / 180F));
        float f5 = MathHelper.sin(-f * ((float)Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vector3d vector3d1 = vector3d.add((double)f6 * range, (double)f5 * range, (double)f7 * range);
        return worldIn.clip(new RayTraceContext(vector3d, vector3d1, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, livingEntity));
    }

    protected EntityRayTraceResult entityResult(World worldIn, LivingEntity livingEntity, int range, double radius){
        Vector3d srcVec = livingEntity.getEyePosition(1.0F);
        Vector3d lookVec = livingEntity.getViewVector(1.0F);
        Vector3d destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
        AxisAlignedBB axisalignedbb = livingEntity.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(radius, radius, radius);
        return ProjectileHelper.getEntityHitResult(worldIn, livingEntity, srcVec, destVec, axisalignedbb, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable());
    }

}