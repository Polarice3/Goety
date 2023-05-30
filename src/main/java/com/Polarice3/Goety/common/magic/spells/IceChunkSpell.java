package com.Polarice3.Goety.common.magic.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.IceChunkEntity;
import com.Polarice3.Goety.common.magic.Spells;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.server.ServerWorld;

public class IceChunkSpell extends Spells {

    public int SoulCost() {
        return SpellConfig.IceChunkCost.get();
    }

    public int CastDuration() {
        return SpellConfig.IceChunkDuration.get();
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving){
        int range = 16;
        double radius = 2.0D;
        float damage = 0.0F;
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(playerEntity)) {
                range += WandUtil.getLevels(ModEnchantments.RANGE.get(), playerEntity);
                radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), playerEntity);
                damage += WandUtil.getLevels(ModEnchantments.POTENCY.get(), playerEntity);
            }
            this.IncreaseInfamy(SpellConfig.IceChunkInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        RayTraceResult rayTraceResult = this.rayTrace(worldIn, entityLiving, range, radius);
        if (rayTraceResult instanceof EntityRayTraceResult){
            Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
            if (target instanceof LivingEntity) {
                IceChunkEntity iceChunkEntity = new IceChunkEntity(worldIn, entityLiving, (LivingEntity) target);
                iceChunkEntity.setExtraDamage(damage);
                worldIn.addFreshEntity(iceChunkEntity);
            }
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.ICE_CHUNK_SUMMON.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        } else if (rayTraceResult instanceof BlockRayTraceResult){
            BlockPos blockPos = ((BlockRayTraceResult) rayTraceResult).getBlockPos();
            IceChunkEntity iceChunkEntity = new IceChunkEntity(worldIn, entityLiving, null);
            iceChunkEntity.setExtraDamage(damage);
            iceChunkEntity.setPos(blockPos.getX(), blockPos.getY() + 4, blockPos.getZ());
            worldIn.addFreshEntity(iceChunkEntity);
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.ICE_CHUNK_SUMMON.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        this.WandResult(worldIn, entityLiving);
    }


}
