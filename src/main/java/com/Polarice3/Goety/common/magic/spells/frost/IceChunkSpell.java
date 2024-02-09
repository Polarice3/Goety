package com.Polarice3.Goety.common.magic.spells.frost;

import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.IceChunkEntity;
import com.Polarice3.Goety.common.magic.Spells;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

public class IceChunkSpell extends Spells {

    public int defaultSoulCost() {
        return SpellConfig.IceChunkCost.get();
    }

    @Override
    public int defaultCastDuration() {
        return SpellConfig.IceChunkDuration.get();
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SPELL.get();
    }

    @Override
    public int defaultSpellCooldown() {
        return SpellConfig.IceChunkCoolDown.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.FROST;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    public void SpellResult(ServerWorld worldIn, LivingEntity entityLiving, boolean staff){
        int range = 16;
        double radius = 2.0D;
        float damage = 0.0F;
        if (WandUtil.enchantedFocus(entityLiving)) {
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), entityLiving);
            damage += WandUtil.getLevels(ModEnchantments.POTENCY.get(), entityLiving);
        }
        RayTraceResult rayTraceResult = this.rayTrace(worldIn, entityLiving, range, radius);
        if (rayTraceResult instanceof EntityRayTraceResult){
            Entity target = ((EntityRayTraceResult) rayTraceResult).getEntity();
            if (target instanceof LivingEntity) {
                IceChunkEntity iceChunkEntity = new IceChunkEntity(worldIn, entityLiving, (LivingEntity) target);
                iceChunkEntity.setExtraDamage(damage);
                worldIn.addFreshEntity(iceChunkEntity);
            }
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.ICE_CHUNK_SUMMON.get(), this.getSoundSource(), 1.0F, 1.0F);
        } else if (rayTraceResult instanceof BlockRayTraceResult){
            BlockPos blockPos = ((BlockRayTraceResult) rayTraceResult).getBlockPos();
            IceChunkEntity iceChunkEntity = new IceChunkEntity(worldIn, entityLiving, null);
            iceChunkEntity.setExtraDamage(damage);
            iceChunkEntity.setPos(blockPos.getX() + 0.5F, blockPos.getY() + 4, blockPos.getZ() + 0.5F);
            worldIn.addFreshEntity(iceChunkEntity);
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), ModSounds.ICE_CHUNK_SUMMON.get(), this.getSoundSource(), 1.0F, 1.0F);
        }
    }

}
