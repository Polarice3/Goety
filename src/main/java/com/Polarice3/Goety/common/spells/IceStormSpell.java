package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.IceStormEntity;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class IceStormSpell extends Spells{

    public int SoulCost() {
        return MainConfig.IceStormCost.get();
    }

    public int CastDuration() {
        return MainConfig.IceStormDuration.get();
    }

    public SoundEvent CastingSound() {
        return SoundEvents.EVOKER_PREPARE_ATTACK;
    }

    public void WandResult(World worldIn, LivingEntity entityLiving) {
        int enchantment = 1;
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        IceStormEntity iceStormEntity = new IceStormEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.RANGE.get(), player) + 1;
            }
            this.IncreaseInfamy(MainConfig.IceStormInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        iceStormEntity.setTotallife(60 * enchantment);
        iceStormEntity.setOwner(entityLiving);
        iceStormEntity.setOwnerId(entityLiving.getUUID());
        worldIn.addFreshEntity(iceStormEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    public void StaffResult(World worldIn, LivingEntity entityLiving) {
        int enchantment = 1;
        Vector3d vector3d = entityLiving.getViewVector( 1.0F);
        IceStormEntity iceStormEntity = new IceStormEntity(worldIn,
                entityLiving.getX() + vector3d.x / 2,
                entityLiving.getEyeY() - 0.2,
                entityLiving.getZ() + vector3d.z / 2,
                vector3d.x,
                vector3d.y,
                vector3d.z);
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(player)){
                enchantment = WandUtil.getLevels(ModEnchantments.RANGE.get(), player) + 1;
            }
            this.IncreaseInfamy(MainConfig.IceStormInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        iceStormEntity.setTotallife(120 * enchantment);
        iceStormEntity.setOwner(entityLiving);
        iceStormEntity.setOwnerId(entityLiving.getUUID());
        iceStormEntity.setUpgraded(true);
        worldIn.addFreshEntity(iceStormEntity);
        worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }
}
