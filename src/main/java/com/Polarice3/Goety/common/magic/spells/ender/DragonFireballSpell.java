package com.Polarice3.Goety.common.magic.spells.ender;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.ModDragonFireballEntity;
import com.Polarice3.Goety.common.magic.Spells;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class DragonFireballSpell extends Spells {

    public int defaultSoulCost() {
        return SpellConfig.DragonFireballCost.get();
    }

    public int defaultCastDuration() {
        return SpellConfig.DragonFireballDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.ENDER_DRAGON_GROWL;
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.DragonFireballCoolDown.get();
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
    public SpellType getSpellType(){
        return SpellType.ENDER;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.RADIUS.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        ModDragonFireballEntity dragonFireball = new ModDragonFireballEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        dragonFireball.setOwner(entityLiving);
        worldIn.addFreshEntity(dragonFireball);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ENDER_DRAGON_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        ModDragonFireballEntity dragonFireball = new ModDragonFireballEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        dragonFireball.setOwner(entityLiving);
        worldIn.addFreshEntity(dragonFireball);
        for(int i = 0; i < 2; ++i) {
            ModDragonFireballEntity dragonFireball1 = new ModDragonFireballEntity(worldIn,
                    entityLiving.getX() + vector3d.x / 2 + worldIn.random.nextGaussian(),
                    entityLiving.getEyeY() - 0.2,
                    entityLiving.getZ() + vector3d.z / 2 + worldIn.random.nextGaussian(),
                    vector3d.x,
                    vector3d.y,
                    vector3d.z);
            dragonFireball1.setOwner(entityLiving);
            worldIn.addFreshEntity(dragonFireball1);
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.ENDER_DRAGON_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
