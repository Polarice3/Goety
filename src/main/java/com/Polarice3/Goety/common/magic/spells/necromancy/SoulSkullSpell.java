package com.Polarice3.Goety.common.magic.spells.necromancy;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.SoulSkullEntity;
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

public class SoulSkullSpell extends Spells {

    @Override
    public int defaultSoulCost() {
        return SpellConfig.SoulSkullCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return SpellConfig.SoulSkullDuration.get();
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.WITHER_SHOOT;
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.SoulSkullCoolDown.get();
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.BURNING.get());
        list.add(ModEnchantments.RADIUS.get());
        return list;
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
        return SpellType.NECROMANCY;
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        SoulSkullEntity soulSkullEntity = new SoulSkullEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        if (isShifting(entityLiving)){
            soulSkullEntity.setDangerous(true);
        }
        soulSkullEntity.setOwner(entityLiving);
        worldIn.addFreshEntity(soulSkullEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        SoulSkullEntity soulSkullEntity = new SoulSkullEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        if (isShifting(entityLiving)){
            soulSkullEntity.setDangerous(true);
        }
        soulSkullEntity.setOwner(entityLiving);
        worldIn.addFreshEntity(soulSkullEntity);
        for(int i = 0; i < 2; ++i) {
            SoulSkullEntity soulSkullEntity1 = new SoulSkullEntity(worldIn,
                    entityLiving.getX() + vector3d.x / 2 + worldIn.random.nextGaussian(),
                    entityLiving.getEyeY() - 0.2,
                    entityLiving.getZ() + vector3d.z / 2 + worldIn.random.nextGaussian(),
                    vector3d.x,
                    vector3d.y,
                    vector3d.z);
            if (isShifting(entityLiving)){
                soulSkullEntity1.setDangerous(true);
            }
            soulSkullEntity1.setOwner(entityLiving);
            worldIn.addFreshEntity(soulSkullEntity1);
        }
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), CastingSound(), SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
