package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
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

public class GhostFireSpell extends Spells{

    public int SoulCost() {
        return MainConfig.WraithCost.get();
    }

    public int CastDuration() {
        return MainConfig.WraithDuration.get();
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    public void WandResult(ServerWorld worldIn, LivingEntity entityLiving){
        int range = 16;
        double radius = 2.0D;
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(playerEntity)) {
                range += WandUtil.getLevels(ModEnchantments.RANGE.get(), playerEntity);
                radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), playerEntity);
            }
            this.IncreaseInfamy(MainConfig.WraithInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        RayTraceResult rayTraceResult = this.rayTrace(worldIn, entityLiving, range, radius);
        BlockPos blockPos = null;
        if (rayTraceResult instanceof EntityRayTraceResult){
            Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
            blockPos = target.blockPosition();
        } else if (rayTraceResult instanceof BlockRayTraceResult){
            blockPos = ((BlockRayTraceResult) rayTraceResult).getBlockPos();
        }
        if (blockPos != null) {
            WandUtil.spawnGhostFires(worldIn, blockPos, entityLiving);
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.CAST_SPELL.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }

    public void StaffResult(ServerWorld worldIn, LivingEntity entityLiving) {
        int range = 16;
        double radius = 2.0D;
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entityLiving;
            if (WandUtil.enchantedFocus(playerEntity)) {
                range += WandUtil.getLevels(ModEnchantments.RANGE.get(), playerEntity);
                radius += WandUtil.getLevels(ModEnchantments.RADIUS.get(), playerEntity);
            }
            this.IncreaseInfamy(MainConfig.WraithInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        RayTraceResult rayTraceResult = this.rayTrace(worldIn, entityLiving, range, radius);
        BlockPos blockPos = null;
        if (rayTraceResult instanceof EntityRayTraceResult){
            Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
            blockPos = target.blockPosition();
        } else if (rayTraceResult instanceof BlockRayTraceResult){
            blockPos = ((BlockRayTraceResult) rayTraceResult).getBlockPos();
        }
        if (blockPos != null) {
            WandUtil.spawnGhostFires(worldIn, blockPos, entityLiving);
            WandUtil.spawnGhostFires(worldIn, blockPos.north(2), entityLiving);
            WandUtil.spawnGhostFires(worldIn, blockPos.south(2), entityLiving);
            WandUtil.spawnGhostFires(worldIn, blockPos.west(2), entityLiving);
            WandUtil.spawnGhostFires(worldIn, blockPos.east(2), entityLiving);
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.CAST_SPELL.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }


}
