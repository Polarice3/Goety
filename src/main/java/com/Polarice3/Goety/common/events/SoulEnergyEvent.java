package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.common.items.GoldTotemItem;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.packets.client.CTotemDeathPacket;
import com.Polarice3.Goety.common.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.common.tileentities.ArcaTileEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModItems;
import com.Polarice3.Goety.utils.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulEnergyEvent {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        World world = player.level;
        ISoulEnergy soulEnergy = SEHelper.getCapability(player);
        if (!soulEnergy.getSEActive() && soulEnergy.getSoulEnergy() > 0) {
            if (!world.isClientSide()){
                player.addEffect(new EffectInstance(ModEffects.SOUL_HUNGER.get(), 60));
                if (player.tickCount % 5 == 0) {
                    SEHelper.decreaseSESouls(player, 1);
                    SEHelper.sendSEUpdatePacket(player);
                }
            }
        }
        if (soulEnergy.getArcaBlock() != null){
            if (soulEnergy.getArcaBlockDimension() == world.dimension()) {
                if (!world.isClientSide()){
                    BlockPos blockPos = soulEnergy.getArcaBlock();
                    TileEntity tileEntity = world.getBlockEntity(blockPos);
                    if (tileEntity instanceof ArcaTileEntity) {
                        ArcaTileEntity arcaTile = (ArcaTileEntity) tileEntity;
                        if (arcaTile.getPlayer() == player) {
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
                if (world.isClientSide()){
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
                                    new ParticleUtil(ParticleTypes.ENCHANT, d0, d1, d2, d3, d4, d5);
                                }
                            }
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
                if (soulEnergy.getSEActive()){
                    soulEnergy.increaseSE(1);
                } else if (!GoldTotemFinder.FindTotem(player).isEmpty()){
                    GoldTotemItem.increaseSouls(GoldTotemFinder.FindTotem(player), 1);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        Entity killer = event.getSource().getEntity();
        Entity killed = event.getEntity();

        if (killed instanceof LivingEntity){
            LivingEntity victim = (LivingEntity) killed;
            if (killer instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) killer;
                if (!(player instanceof FakePlayer)){
                    if (SEHelper.getSEActive(player)){
                        SEHelper.handleKill(player, victim);
                    } else {
                        GoldTotemItem.handleKill(player, victim);
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
                                if (SEHelper.getSEActive(playerEntity)) {
                                    SEHelper.handleKill(slayer, victim);
                                } else {
                                    GoldTotemItem.handleKill(slayer, victim);
                                }
                            }
                        }
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
                        if (LichdomHelper.isLich(player)){
                            if (soulEnergy.getArcaBlockDimension() == player.level.dimension()) {
                                BlockPos blockPos = SEHelper.getArcaBlock(player);
                                player.teleportTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                            } else {
                                if (soulEnergy.getArcaBlockDimension() != null) {
                                    if (!player.level.isClientSide) {
                                        ServerWorld serverWorld = player.getServer().getLevel(soulEnergy.getArcaBlockDimension());
                                        player.changeDimension(serverWorld);
                                        BlockPos blockPos = SEHelper.getArcaBlock(player);
                                        player.teleportTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                                    }
                                }
                            }
                            player.setHealth(1.0F);
                            player.removeAllEffects();
                            if (soulEnergy.getSoulEnergy() > MainConfig.MaxSouls.get()){
                                player.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                                player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                            } else {
                                player.addEffect(new EffectInstance(ModEffects.SOUL_HUNGER.get(), 12000, 4, false, false));
                            }
                            new SoundUtil(player, SoundEvents.WITHER_DEATH, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            SEHelper.decreaseSESouls(player, MainConfig.MaxSouls.get());
                            SEHelper.sendSEUpdatePacket(player);
                            event.setCanceled(true);
                        } else if (soulEnergy.getSoulEnergy() > MainConfig.MaxSouls.get()) {
                            if (soulEnergy.getArcaBlockDimension() == player.level.dimension()) {
                                BlockPos blockPos = SEHelper.getArcaBlock(player);
                                player.teleportTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                            } else {
                                if (soulEnergy.getArcaBlockDimension() != null) {
                                    if (!player.level.isClientSide) {
                                        ServerWorld serverWorld = player.getServer().getLevel(soulEnergy.getArcaBlockDimension());
                                        player.changeDimension(serverWorld);
                                        BlockPos blockPos = SEHelper.getArcaBlock(player);
                                        player.teleportTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                                    }
                                }
                            }
                            player.setHealth(1.0F);
                            player.removeAllEffects();
                            player.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                            player.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                            player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                            new SoundUtil(player, SoundEvents.WITHER_DEATH, SoundCategory.PLAYERS, 1.0F, 1.0F);
                            SEHelper.decreaseSESouls(player, MainConfig.MaxSouls.get());
                            SEHelper.sendSEUpdatePacket(player);
                            event.setCanceled(true);
                        }
                    }
                }
            } else if (GoldTotemItem.UndyingEffect(player)){
                player.setHealth(1.0F);
                player.removeAllEffects();
                player.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                player.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                ModNetwork.sendTo(player, new CTotemDeathPacket(player.getUUID()));
                GoldTotemItem.setSoulsamount(GoldTotemFinder.FindTotem(player), 0);
                event.setCanceled(true);
            }
        }

    }

}
