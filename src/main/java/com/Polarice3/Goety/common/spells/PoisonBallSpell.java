package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.projectiles.PoisonBallEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

public class PoisonBallSpell extends InstantCastSpells{

    @Override
    public int SoulCost() {
        return MainConfig.PoisonballCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.SLIME_JUMP;
    }

    @Override
    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        PoisonBallEntity smallFireballEntity = new PoisonBallEntity(entityLiving, worldIn);
        smallFireballEntity.setOwner(entityLiving);
        smallFireballEntity.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F, 1.0F);
        worldIn.addFreshEntity(smallFireballEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.PoisonballInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    @Override
    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        for(int i = 0; i < 3; ++i) {
            PoisonBallEntity smallFireballEntity = new PoisonBallEntity(entityLiving, worldIn);
            smallFireballEntity.setOwner(entityLiving);
            smallFireballEntity.setUpgraded(true);
            smallFireballEntity.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F - (float) i/10, 1.0F - (float) i/10);
            worldIn.addFreshEntity(smallFireballEntity);
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.PoisonballInfamyChance.get(), (PlayerEntity) entityLiving);
    }
}
