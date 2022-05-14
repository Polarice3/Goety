package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ally.FriendlyVexEntity;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.items.GoldTotemItem;
import com.Polarice3.Goety.common.soulenergy.ISoulEnergy;
import com.Polarice3.Goety.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GoldTotemEvent {

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        Entity killer = event.getSource().getEntity();
        LivingEntity slayer = (LivingEntity) killer;
        Entity killed = event.getEntity();

        if (killer instanceof PlayerEntity && killed instanceof MobEntity){
            PlayerEntity player = (PlayerEntity) killer;
            LivingEntity victim = (LivingEntity) killed;
            if (!(player instanceof FakePlayer)){
                if (SEHelper.getSEActive(player)){
                    SEHelper.handleKill(player, victim);
                } else {
                    GoldTotemItem.handleKill(player, victim);
                }
            }
        }

        if (killer instanceof SummonedEntity){
            LivingEntity owner = ((SummonedEntity) killer).getTrueOwner();
            if (owner != null){
                if (owner instanceof PlayerEntity) {
                    if (RobeArmorFinder.FindArmor(owner)) {
                        PlayerEntity playerEntity = (PlayerEntity) owner;
                        LivingEntity victim = (LivingEntity) killed;
                        if (!(playerEntity instanceof FakePlayer)) {
                            if (SEHelper.getSEActive(playerEntity)){
                                SEHelper.handleKill(slayer, victim);
                            } else {
                                GoldTotemItem.handleKill(slayer, victim);
                            }
                        }
                    }
                }
            }
        }

        if (killer instanceof FriendlyVexEntity){
            LivingEntity owner = ((FriendlyVexEntity) killer).getTrueOwner();
            if (owner != null){
                if (owner instanceof PlayerEntity) {
                    if (RobeArmorFinder.FindArmor(owner)) {
                        PlayerEntity playerEntity = (PlayerEntity) owner;
                        LivingEntity victim = (LivingEntity) killed;
                        if (!(playerEntity instanceof FakePlayer)) {
                            if (SEHelper.getSEActive(playerEntity)){
                                SEHelper.handleKill(slayer, victim);
                            } else {
                                GoldTotemItem.handleKill(slayer, victim);
                            }
                        }
                    }
                }
            }
        }

        if (event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            Minecraft minecraft = Minecraft.getInstance();
            ISoulEnergy soulEnergy = SEHelper.getCapability(player);
            if (soulEnergy.getSEActive()){
                if (soulEnergy.getArcaBlock() != null) {
                    if (soulEnergy.getSoulEnergy() > MainConfig.MaxSouls.get()) {
                        if (soulEnergy.getArcaBlockDimension() == player.level.dimension()){
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
                        player.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                        player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                        new SoundUtil(player.blockPosition(), SoundEvents.WITHER_DEATH, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        SEHelper.decreaseSESouls(player, MainConfig.MaxSouls.get());
                        SEHelper.sendSEUpdatePacket(player);
                        event.setCanceled(true);
                    }
                }
            } else if (GoldTotemItem.UndyingEffect(player)){
                player.setHealth(1.0F);
                player.removeAllEffects();
                player.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                player.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                new ParticleUtil(ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getY(), player.getZ(), 0.0F, 0.0F, 0.0F);
                new SoundUtil(player.blockPosition(), SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                minecraft.gameRenderer.displayItemActivation(GoldTotemFinder.FindTotem(player));
                GoldTotemItem.setSoulsamount(GoldTotemFinder.FindTotem(player), 0);
                event.setCanceled(true);
            }
        }

    }

}
