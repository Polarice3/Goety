package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.capabilities.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.common.entities.projectiles.FangEntity;
import com.Polarice3.Goety.common.items.magic.GoldTotemItem;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.packets.client.CSoulEnergyPacket;
import com.Polarice3.Goety.common.network.packets.server.TotemDeathPacket;
import com.Polarice3.Goety.common.tileentities.ArcaTileEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SEntityStatusPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulEnergyEvent {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        World world = player.level;
        ISoulEnergy soulEnergy = SEHelper.getCapability(player);
        if (!soulEnergy.getSEActive() && soulEnergy.getSoulEnergy() > 0) {
            if (!world.isClientSide){
                player.addEffect(new EffectInstance(ModEffects.SOUL_HUNGER.get(), 60));
                if (player.tickCount % 5 == 0) {
                    SEHelper.decreaseSESouls(player, 1);
                    SEHelper.sendSEUpdatePacket(player);
                }
            }
        }
        if (soulEnergy.getArcaBlock() != null){
            if (soulEnergy.getArcaBlockDimension() == world.dimension()) {
                if (!world.isClientSide){
                    ServerWorld serverWorld = (ServerWorld) world;
                    BlockPos blockPos = soulEnergy.getArcaBlock();
                    TileEntity tileEntity = world.getBlockEntity(blockPos);
                    if (tileEntity instanceof ArcaTileEntity) {
                        ArcaTileEntity arcaTile = (ArcaTileEntity) tileEntity;
                        if (arcaTile.getPlayer() == player) {
                            Random pRand = world.random;
                            if (pRand.nextInt(12) == 0) {
                                for (int i = 0; i < 3; ++i) {
                                    int j = pRand.nextInt(2) * 2 - 1;
                                    int k = pRand.nextInt(2) * 2 - 1;
                                    double d0 = (double) blockPos.getX() + 0.5D + 0.25D * (double) j;
                                    double d1 = (float) blockPos.getY() + pRand.nextFloat();
                                    double d2 = (double) blockPos.getZ() + 0.5D + 0.25D * (double) k;
                                    double d3 = pRand.nextFloat() * (float) j;
                                    double d4 = ((double) pRand.nextFloat() - 0.5D) * 0.125D;
                                    double d5 = pRand.nextFloat() * (float) k;
                                    serverWorld.sendParticles(ParticleTypes.ENCHANT, d0, d1, d2, 0, d3, d4, d5, 1.0F);
                                }
                            }
                            if (!soulEnergy.getSEActive()) {
                                soulEnergy.setSEActive(true);
                                SEHelper.sendSEUpdatePacket(player);
                            }
                        } else {
                            if (soulEnergy.getSEActive()) {
                                soulEnergy.setSEActive(false);
                                SEHelper.sendSEUpdatePacket(player);
                            }
                        }
                    } else {
                        if (soulEnergy.getSEActive()) {
                            soulEnergy.setSEActive(false);
                            SEHelper.sendSEUpdatePacket(player);
                        }
                    }
                }
            } else if (soulEnergy.getArcaBlockDimension() == null){
                soulEnergy.setArcaBlockDimension(world.dimension());
                SEHelper.sendSEUpdatePacket(player);
            }
        }
        int s = soulEnergy.getSoulEnergy();
        if (s < 0){
            soulEnergy.setSoulEnergy(0);
        }
        if (CuriosFinder.findAmulet(player).getItem() == ModItems.EMERALD_AMULET.get()){
            if (player.tickCount % 100 == 0){
                SEHelper.increaseSouls(player, MainConfig.EmeraldAmuletSouls.get());
            }
        }
        if (RobeArmorFinder.FindNecroBootsofWander(player)){
            if (player.canSpawnSoulSpeedParticle()){
                ModNetwork.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CSoulEnergyPacket(MainConfig.NecroSoulSandSouls.get(), true));
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        Entity killer = event.getSource().getEntity();
        Entity projectile = event.getSource().getDirectEntity();
        Entity killed = event.getEntity();

        if (killed instanceof LivingEntity){
            LivingEntity victim = (LivingEntity) killed;
            if (killer instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) killer;
                if (!(player instanceof FakePlayer)){
                    if (projectile instanceof FangEntity && ((FangEntity) projectile).isTotemSpawned()){
                        FangEntity fangEntity = (FangEntity) projectile;
                        SEHelper.rawHandleKill(player, victim, fangEntity.getSoulEater());
                    } else {
                        SEHelper.handleKill(player, victim);
                    }
                }
            }

            if (killer instanceof OwnedEntity){
                OwnedEntity slayer = (OwnedEntity) killer;
                LivingEntity owner = slayer.getTrueOwner();
                if (owner != null){
                    if (owner instanceof PlayerEntity) {
                        if (RobeArmorFinder.FindArmor(owner)) {
                            PlayerEntity playerEntity = (PlayerEntity) owner;
                            if (!(playerEntity instanceof FakePlayer)) {
                                SEHelper.handleKill(playerEntity, victim);
                            }
                        }
                    }
                }
            }
            if (!(victim instanceof PlayerEntity) || !MainConfig.TotemUndying.get()) {
                if (victim.getMainHandItem().getItem() instanceof GoldTotemItem){
                    ItemStack itemStack = victim.getMainHandItem();
                    if (revive(itemStack, victim)) {
                        event.setCanceled(true);
                    }
                } else if (victim.getOffhandItem().getItem() instanceof GoldTotemItem) {
                    ItemStack itemStack = victim.getOffhandItem();
                    if (revive(itemStack, victim)) {
                        event.setCanceled(true);
                    }
                }
            }
        }

        if (killed instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) killed;
            ISoulEnergy soulEnergy = SEHelper.getCapability(player);
            if (soulEnergy.getSEActive()){
                if (soulEnergy.getArcaBlock() != null) {
                    if (MainConfig.ArcaUndying.get()) {
                        if (!player.level.isClientSide) {
                            if (LichdomHelper.isLich(player)) {
                                SEHelper.teleportDeathArca(player);
                                player.setHealth(1.0F);
                                player.removeAllEffects();
                                if (soulEnergy.getSoulEnergy() > MainConfig.MaxSouls.get()) {
                                    player.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                                    player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                                } else {
                                    player.addEffect(new EffectInstance(ModEffects.SOUL_HUNGER.get(), 12000, 4, false, false));
                                }
                                player.playSound(SoundEvents.WITHER_DEATH, 1.0F, 1.0F);
                                SEHelper.decreaseSESouls(player, MainConfig.MaxSouls.get());
                                SEHelper.sendSEUpdatePacket(player);
                                event.setCanceled(true);
                            } else if (soulEnergy.getSoulEnergy() > MainConfig.MaxSouls.get()) {
                                SEHelper.teleportDeathArca(player);
                                player.setHealth(1.0F);
                                player.removeAllEffects();
                                player.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                                player.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                                player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                                player.playSound(SoundEvents.WITHER_DEATH, 1.0F, 1.0F);
                                SEHelper.decreaseSESouls(player, MainConfig.MaxSouls.get());
                                SEHelper.sendSEUpdatePacket(player);
                                event.setCanceled(true);
                            }
                        }
                    }
                }
            } else if (GoldTotemItem.UndyingEffect(player)){
                if (!player.level.isClientSide) {
                    player.setHealth(1.0F);
                    player.removeAllEffects();
                    player.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                    player.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                    player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                    ModNetwork.sendTo(player, new TotemDeathPacket(player.getUUID()));
                    GoldTotemItem.setSoulsamount(GoldTotemFinder.FindTotem(player), 0);
                }
                event.setCanceled(true);
            }
        }

    }

    public static boolean revive(ItemStack itemStack, LivingEntity victim){
        if (!itemStack.isEmpty()) {
            if (itemStack.getTag() != null) {
                if (itemStack.getTag().getInt(GoldTotemItem.SOULSAMOUNT) == GoldTotemItem.MAXSOULS) {
                    if (!victim.level.isClientSide) {
                        victim.setHealth(1.0F);
                        victim.removeAllEffects();
                        victim.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                        victim.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                        victim.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                        if (victim instanceof PlayerEntity) {
                            ModNetwork.sendTo((PlayerEntity) victim, new TotemDeathPacket(victim.getUUID()));
                        } else {
                            ServerWorld serverWorld = (ServerWorld) victim.level;
                            serverWorld.getChunkSource().broadcast(victim, new SEntityStatusPacket(victim, (byte)35));
                        }
                        GoldTotemItem.setSoulsamount(itemStack, 0);
                        if (victim instanceof MobEntity){
                            itemStack.shrink(1);
                            victim.spawnAtLocation(new ItemStack(ModItems.SPENT_TOTEM.get()));
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
