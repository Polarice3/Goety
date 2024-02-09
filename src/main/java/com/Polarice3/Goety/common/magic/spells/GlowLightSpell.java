package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.entities.projectiles.GlowLightEntity;
import com.Polarice3.Goety.common.magic.Spells;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

public class GlowLightSpell extends Spells {

    @Override
    public int defaultSoulCost() {
        return SpellConfig.GlowLightCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return SpellConfig.GlowLightDuration.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.GlowLightCoolDown.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.EGG_THROW;
    }

    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff){
        GlowLightEntity soulLightEntity = new GlowLightEntity(worldIn, entityLiving);
        soulLightEntity.setOwner(entityLiving);
        soulLightEntity.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F, 1.0F);
        worldIn.addFreshEntity(soulLightEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EGG_THROW, this.getSoundSource(), 1.0F, 1.0F);
    }
}
