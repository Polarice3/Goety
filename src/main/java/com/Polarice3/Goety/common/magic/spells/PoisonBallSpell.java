package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.entities.projectiles.PoisonBallEntity;
import com.Polarice3.Goety.common.magic.InstantCastSpells;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

public class PoisonBallSpell extends InstantCastSpells {

    @Override
    public int SoulCost() {
        return SpellConfig.PoisonballCost.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.SLIME_JUMP;
    }

    @Override
    public SpellType getSpellType(){
        return SpellType.FEL;
    }

    @Override
    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        PoisonBallEntity poisonBall = new PoisonBallEntity(entityLiving, worldIn);
        poisonBall.setOwner(entityLiving);
        poisonBall.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F, 1.0F);
        worldIn.addFreshEntity(poisonBall);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        PoisonBallEntity poisonBall = new PoisonBallEntity(entityLiving, worldIn);
        poisonBall.setOwner(entityLiving);
        poisonBall.setUpgraded(true);
        poisonBall.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F, 1.0F);
        worldIn.addFreshEntity(poisonBall);
        for(int i = 0; i < 2; ++i) {
            PoisonBallEntity poisonBall1 = new PoisonBallEntity(entityLiving, worldIn);
            poisonBall1.setOwner(entityLiving);
            poisonBall1.setUpgraded(true);
            poisonBall1.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F - (float) i/10, 1.0F - (float) i/10);
            worldIn.addFreshEntity(poisonBall1);
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
