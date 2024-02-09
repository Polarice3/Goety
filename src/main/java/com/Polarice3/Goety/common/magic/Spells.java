package com.Polarice3.Goety.common.magic;

import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.api.magic.SpellType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class Spells implements ISpell {

    public Spells(){
    }

    public abstract int defaultSoulCost();

    public abstract int defaultCastDuration();

    @Nullable
    public abstract SoundEvent CastingSound();

    public abstract int defaultSpellCooldown();

    public abstract void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff);

    public SpellType getSpellType(){
        return SpellType.NONE;
    }

    public boolean isShifting(LivingEntity entityLiving){
        return entityLiving.isCrouching() || entityLiving.isShiftKeyDown();
    }

    public boolean conditionsMet(ServerWorld worldIn, LivingEntity entityLiving){
        return true;
    }

    public List<Enchantment> acceptedEnchantments(){
        return new ArrayList<>();
    }

    protected RayTraceResult rayTraceCollide(World worldIn, LivingEntity livingEntity, int range, double radius) {
        if (this.entityCollideResult(worldIn, livingEntity, range, radius) == null){
            return this.blockResult(worldIn, livingEntity, range);
        } else {
            return this.entityCollideResult(worldIn, livingEntity, range, radius);
        }
    }

    protected EntityRayTraceResult entityCollideResult(World worldIn, LivingEntity livingEntity, int range, double radius){
        Vector3d srcVec = livingEntity.getEyePosition(1.0F);
        Vector3d lookVec = livingEntity.getViewVector(1.0F);
        Vector3d destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);
        AxisAlignedBB axisalignedbb = livingEntity.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(radius, radius, radius);
        return ProjectileHelper.getEntityHitResult(worldIn, livingEntity, srcVec, destVec, axisalignedbb, entity -> entity instanceof LivingEntity && livingEntity.canSee(entity) && !entity.isSpectator() && entity.isPickable());
    }

    public SoundCategory getSoundSource(){
        return SoundCategory.PLAYERS;
    }
}
