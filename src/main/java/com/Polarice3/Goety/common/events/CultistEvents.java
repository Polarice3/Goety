package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.BeldamEntity;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.utils.ConstantPaths;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.GossipType;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CultistEvents {

    @SubscribeEvent
    public static void SpecialSpawnEvents(LivingSpawnEvent.CheckSpawn event){
        if (event.getEntityLiving() instanceof VillagerEntity){
            VillagerEntity villager = (VillagerEntity) event.getEntityLiving();
            if (MainConfig.CultistSpread.get()) {
                if (!villager.isBaby() && event.getSpawnReason() == SpawnReason.STRUCTURE) {
                    if (event.getWorld().getRandom().nextFloat() <= 0.05F) {
                        villager.addTag(ConstantPaths.secretCultist());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void LivingEffects(LivingEvent.LivingUpdateEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity != null){
            if (livingEntity instanceof WitchEntity){
                WitchEntity witch = (WitchEntity) livingEntity;
                List<AbstractCultistEntity> list = witch.level.getEntitiesOfClass(AbstractCultistEntity.class, witch.getBoundingBox().inflate(8.0D));
                if (!witch.level.isClientSide) {
                    ServerWorld serverWorld = (ServerWorld) witch.level;
                    if (list.size() >= 5) {
                        BeldamEntity beldam = witch.convertTo(ModEntityType.BELDAM.get(), true);
                        if (beldam != null) {
                            beldam.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(witch.blockPosition()), SpawnReason.CONVERSION, null, null);
                            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(witch, beldam);
                        }
                    }
                }
            }
            if (livingEntity instanceof VillagerEntity){
                VillagerEntity villager = (VillagerEntity) livingEntity;
                if (MainConfig.CultistSpread.get()) {
                    if (villager.getTags().contains(ConstantPaths.secretCultist())) {
                        if (villager.getVillagerData().getLevel() >= 3) {
                            villager.removeTag(ConstantPaths.secretCultist());
                        }
                        if (!villager.level.isClientSide) {
                            ServerWorld serverWorld = (ServerWorld) villager.level;
                            Raid raid = serverWorld.getRaidAt(villager.blockPosition());
                            if (raid != null && raid.isActive() && !raid.isOver()) {
                                MobUtil.revealCultist(serverWorld, villager);
                            }
                        }
                        if (villager.tickCount % 1200 == 0) {
                            MobUtil.secretConversion(villager);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void TargetSelection(LivingSetAttackTargetEvent event){
        LivingEntity livingEntity = event.getEntityLiving();
        LivingEntity target = event.getTarget();
        if (livingEntity instanceof MobEntity) {
            MobEntity mob = (MobEntity) livingEntity;
            if (target != null) {
                if (mob instanceof ZombieEntity) {
                    if (target instanceof VillagerEntity) {
                        if (target.getTags().contains(ConstantPaths.secretCultist())) {
                            mob.setTarget(null);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void HurtEvent(LivingHurtEvent event){
        LivingEntity victim = event.getEntityLiving();
        Entity attacker = event.getSource().getEntity();
        if (attacker instanceof LivingEntity) {
            if (victim instanceof VillagerEntity) {
                VillagerEntity villager = (VillagerEntity) victim;
                if (villager.getTags().contains(ConstantPaths.secretCultist()) && !villager.isBaby()) {
                    if (!villager.level.isClientSide) {
                        ServerWorld serverWorld = (ServerWorld) villager.level;
                        if (MobUtil.getWitnesses(villager)) {
                            if (villager.getRandom().nextFloat() <= 0.25F) {
                                MobUtil.revealCultist(serverWorld, villager);
                            }
                        } else {
                            MobUtil.revealCultist(serverWorld, villager);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void SpecialDeath(LivingDeathEvent event){
        Entity killed = event.getEntity();
        Entity killer = event.getSource().getEntity();
        World world = killed.getCommandSenderWorld();
        if (killed instanceof VillagerEntity) {
            if (killer instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) killer;
                if (killed.getTags().contains(ConstantPaths.revealedCultist())) {
                    for (VillagerEntity villager : world.getEntitiesOfClass(VillagerEntity.class, player.getBoundingBox().inflate(16.0D))) {
                        villager.getGossips().add(player.getUUID(), GossipType.MINOR_POSITIVE, 25);
                    }
                }
            }
        }
    }
}
