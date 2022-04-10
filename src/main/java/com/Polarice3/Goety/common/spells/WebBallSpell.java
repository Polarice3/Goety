package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.projectiles.WebBallEntity;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Random;

public class WebBallSpell extends Spells{

    public int SoulCost() {
        return MainConfig.WebballCost.get();
    }

    public int CastDuration() {
        return MainConfig.WebballDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
    }

    @Override
    public void WandResult(World worldIn, LivingEntity entityLiving) {
        WebBallEntity smallFireballEntity = new WebBallEntity(ModEntityType.WEB_BALL.get(), entityLiving, worldIn);
        smallFireballEntity.setOwner(entityLiving);
        smallFireballEntity.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F, 1.0F);
        worldIn.addFreshEntity(smallFireballEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.WebballInfamyChance.get(), (PlayerEntity) entityLiving);
    }

    @Override
    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        for(int i = 0; i < 3; ++i) {
            WebBallEntity smallFireballEntity = new WebBallEntity(ModEntityType.WEB_BALL.get(), entityLiving, worldIn);
            smallFireballEntity.setOwner(entityLiving);
            smallFireballEntity.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F, 1.0F);
            worldIn.addFreshEntity(smallFireballEntity);
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);
        this.IncreaseInfamy(MainConfig.WebballInfamyChance.get(), (PlayerEntity) entityLiving);
    }
}
