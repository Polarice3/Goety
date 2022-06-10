package com.Polarice3.Goety.utils;

import com.Polarice3.Goety.common.entities.hostile.HuskarlEntity;
import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class MobUtil {
    public static void deadSandConvert(Entity entity, boolean natural){
        if (entity instanceof MonsterEntity && !(entity instanceof IDeadMob)){
            MonsterEntity monster = (MonsterEntity) entity;
            MonsterEntity corrupt = null;
            if (monster instanceof CreeperEntity) {
                corrupt = monster.convertTo(ModEntityType.BOOMER.get(), false);
            }
            if (monster instanceof SpiderEntity) {
                corrupt = monster.convertTo(ModEntityType.DUNE_SPIDER.get(), false);
            }
            if (monster instanceof ZombieEntity && !(monster instanceof HuskarlEntity)) {
                corrupt = monster.convertTo(ModEntityType.FALLEN.get(), true);
            }
            if (monster instanceof AbstractSkeletonEntity && !(monster instanceof WitherSkeletonEntity)) {
                if (monster.level.random.nextFloat() < 0.01F){
                    corrupt = monster.convertTo(ModEntityType.MARCIRE.get(), false);
                } else {
                    corrupt = monster.convertTo(ModEntityType.DESICCATED.get(), true);
                }
            }
            if (corrupt != null) {
                corrupt.finalizeSpawn((IServerWorld) corrupt.level, corrupt.level.getCurrentDifficultyAt(corrupt.blockPosition()), SpawnReason.CONVERSION, null, null);
                if (!natural){
                    net.minecraftforge.event.ForgeEventFactory.onLivingConvert((LivingEntity) entity, corrupt);
                    new SoundUtil(corrupt.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundCategory.HOSTILE, 1.0F, 1.0F);
                }
            }
        }
    }

    public static boolean nonDesiccateExplodeEntities(Entity entity){
        return !(entity instanceof CreeperEntity)
                && !(entity instanceof SpiderEntity)
                && !(entity instanceof ZombieEntity)
                && !(entity instanceof AbstractSkeletonEntity);
    }

    public static boolean playerValidity(PlayerEntity player, boolean lich){
        if (!player.isCreative() && !player.isSpectator()) {
            if (lich) {
                return !LichdomHelper.isLich(player);
            } else {
                return true;
            }
        }
        return false;
    }

    public static LootContext.Builder createLootContext(DamageSource pDamageSource, LivingEntity livingEntity) {
        LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld)livingEntity.level)).withRandom(livingEntity.getRandom()).withParameter(LootParameters.THIS_ENTITY, livingEntity).withParameter(LootParameters.ORIGIN, livingEntity.position()).withParameter(LootParameters.DAMAGE_SOURCE, pDamageSource).withOptionalParameter(LootParameters.KILLER_ENTITY, pDamageSource.getEntity()).withOptionalParameter(LootParameters.DIRECT_KILLER_ENTITY, pDamageSource.getDirectEntity());
        if (livingEntity.getLastHurtByMob() != null && livingEntity.getLastHurtByMob() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity.getLastHurtByMob();
            lootcontext$builder = lootcontext$builder.withParameter(LootParameters.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
        }

        return lootcontext$builder;
    }

}
