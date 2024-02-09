package com.Polarice3.Goety.api.magic;

import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;

public interface ISpell {
    int defaultSoulCost();

    default int soulCost(LivingEntity entityLiving){
        return SoulCalculation(entityLiving);
    }

    default int SoulCalculation(LivingEntity entityLiving){
        if (SoulDiscount(entityLiving)){
            return defaultSoulCost() / 2;
        } else {
            return defaultSoulCost() * SoulCostUp(entityLiving);
        }
    }

    int defaultCastDuration();

    default int castDuration(LivingEntity entityLiving){
        if (ReduceCastTime(entityLiving)){
            return defaultCastDuration() / 2;
        } else {
            return defaultCastDuration();
        }
    }

    @Nullable
    SoundEvent CastingSound();

    int defaultSpellCooldown();

    default int spellCooldown(){
        return defaultSpellCooldown();
    }

    void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff);

    SpellType getSpellType();

    boolean conditionsMet(ServerWorld worldIn, LivingEntity entityLiving);

    List<Enchantment> acceptedEnchantments();

    default SoundEvent loopSound(LivingEntity entityLiving){
        return null;
    }

    default ColorUtil particleColors(LivingEntity entityLiving){
        return new ColorUtil(0.2F, 0.2F, 0.2F);
    }

    default RayTraceResult rayTrace(World worldIn, LivingEntity livingEntity, int range, double radius) {
        if (this.entityResult(worldIn, livingEntity, range, radius) == null){
            return this.blockResult(worldIn, livingEntity, range);
        } else {
            return this.entityResult(worldIn, livingEntity, range, radius);
        }
    }

    default BlockRayTraceResult blockResult(World worldIn, LivingEntity livingEntity, double range) {
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

    default EntityRayTraceResult entityResult(World worldIn, LivingEntity livingEntity, int range, double radius){
        Vector3d srcVec = livingEntity.getEyePosition(1.0F);
        Vector3d lookVec = livingEntity.getViewVector(1.0F);
        Vector3d destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
        AxisAlignedBB axisalignedbb = livingEntity.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(radius, radius, radius);
        return ProjectileHelper.getEntityHitResult(worldIn, livingEntity, srcVec, destVec, axisalignedbb, entity -> entity instanceof LivingEntity && !entity.isSpectator() && entity.isPickable());
    }

    default boolean ReduceCastTime(LivingEntity entityLiving){
        return RobeArmorFinder.FindHelm(entityLiving);
    }

    @Nullable
    default EffectInstance summonDownEffect(LivingEntity livingEntity){
        return livingEntity.getEffect(ModEffects.SUMMONDOWN.get());
    }

    default int SoulCostUp(LivingEntity entityLiving){
        EffectInstance mobEffectInstance = summonDownEffect(entityLiving);
        if (mobEffectInstance != null){
            return mobEffectInstance.getAmplifier() + 2;
        }
        return 1;
    }

    default boolean SoulDiscount(LivingEntity entityLiving){
        return RobeArmorFinder.FindArmor(entityLiving);
    }
}
