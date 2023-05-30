package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.entities.projectiles.SoulLightEntity;
import com.Polarice3.Goety.common.magic.InstantCastSpells;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

public class SoulLightSpell extends InstantCastSpells {

    @Override
    public int SoulCost() {
        return SpellConfig.SoulLightCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.EGG_THROW;
    }

    @Override
    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        SoulLightEntity soulLightEntity = new SoulLightEntity(worldIn, entityLiving);
        soulLightEntity.setOwner(entityLiving);
        soulLightEntity.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F, 1.0F);
        worldIn.addFreshEntity(soulLightEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        SoulLightEntity soulLightEntity = new SoulLightEntity(worldIn, entityLiving);
        soulLightEntity.setOwner(entityLiving);
        soulLightEntity.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F, 1.0F);
        worldIn.addFreshEntity(soulLightEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
