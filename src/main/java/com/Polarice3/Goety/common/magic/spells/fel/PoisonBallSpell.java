package com.Polarice3.Goety.common.magic.spells.fel;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.PoisonBallEntity;
import com.Polarice3.Goety.common.magic.Spells;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class PoisonBallSpell extends Spells {

    @Override
    public int defaultSoulCost() {
        return SpellConfig.PoisonballCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return SpellConfig.PoisonballDuration.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.SLIME_JUMP;
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.PoisonballCoolDown.get();
    }

    @Override
    public SpellType getSpellType(){
        return SpellType.FEL;
    }

    @Override
    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff) {
        if (staff){
            StaffResult(worldIn, entityLiving);
        } else {
            WandResult(worldIn, entityLiving);
        }
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        PoisonBallEntity poisonBall = new PoisonBallEntity(entityLiving, worldIn);
        poisonBall.setOwner(entityLiving);
        poisonBall.shootFromRotation(entityLiving, entityLiving.xRot, entityLiving.yRot, 0.0F, 1.5F, 1.0F);
        worldIn.addFreshEntity(poisonBall);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

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
