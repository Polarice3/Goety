package com.Polarice3.Goety.common.spells;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.Vector3dUtil;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class GhostFireSpell extends Spells{

    public int SoulCost() {
        return SpellConfig.WraithCost.get();
    }

    public int CastDuration() {
        return SpellConfig.WraithDuration.get();
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
            this.IncreaseInfamy(SpellConfig.WraithInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        RayTraceResult rayTraceResult = this.rayTrace(worldIn, entityLiving, range, radius);
        if (rayTraceResult instanceof EntityRayTraceResult){
            Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
            WandUtil.spawnGhostFires(worldIn, target.position(), entityLiving);
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.CAST_SPELL.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        } else if (rayTraceResult instanceof BlockRayTraceResult){
            BlockPos blockPos = ((BlockRayTraceResult) rayTraceResult).getBlockPos();
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
            this.IncreaseInfamy(SpellConfig.WraithInfamyChance.get(), (PlayerEntity) entityLiving);
        }
        RayTraceResult rayTraceResult = this.rayTrace(worldIn, entityLiving, range, radius);
        if (rayTraceResult instanceof EntityRayTraceResult){
            Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
            Vector3d vector3d = target.position();
            WandUtil.spawnGhostFires(worldIn, vector3d, entityLiving);
            WandUtil.spawnGhostFires(worldIn, Vector3dUtil.north(vector3d, 2), entityLiving);
            WandUtil.spawnGhostFires(worldIn, Vector3dUtil.south(vector3d, 2), entityLiving);
            WandUtil.spawnGhostFires(worldIn, Vector3dUtil.west(vector3d, 2), entityLiving);
            WandUtil.spawnGhostFires(worldIn, Vector3dUtil.east(vector3d, 2), entityLiving);
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.CAST_SPELL.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        } else if (rayTraceResult instanceof BlockRayTraceResult){
            BlockPos blockPos = ((BlockRayTraceResult) rayTraceResult).getBlockPos();
            WandUtil.spawnGhostFires(worldIn, blockPos, entityLiving);
            WandUtil.spawnGhostFires(worldIn, blockPos.north(2), entityLiving);
            WandUtil.spawnGhostFires(worldIn, blockPos.south(2), entityLiving);
            WandUtil.spawnGhostFires(worldIn, blockPos.west(2), entityLiving);
            WandUtil.spawnGhostFires(worldIn, blockPos.east(2), entityLiving);
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.CAST_SPELL.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
        }
    }


}
