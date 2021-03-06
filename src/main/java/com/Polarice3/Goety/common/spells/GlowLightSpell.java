package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.projectiles.GlowLightEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class GlowLightSpell extends InstantCastSpells{

    @Override
    public int SoulCost() {
        return MainConfig.GlowLightCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.EGG_THROW;
    }

    @Override
    public void WandResult(World worldIn, LivingEntity entityLiving) {
        GlowLightEntity soulLightEntity = new GlowLightEntity(worldIn, entityLiving);
        soulLightEntity.setOwner(entityLiving);
        soulLightEntity.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F, 1.0F);
        worldIn.addFreshEntity(soulLightEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        GlowLightEntity soulLightEntity = new GlowLightEntity(worldIn, entityLiving);
        soulLightEntity.setOwner(entityLiving);
        soulLightEntity.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F, 1.0F);
        worldIn.addFreshEntity(soulLightEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
