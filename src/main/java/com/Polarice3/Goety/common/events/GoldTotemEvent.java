package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.ally.FriendlyVexEntity;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.items.GoldTotemItem;
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
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GoldTotemEvent {

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        Entity killer = event.getSource().getEntity();
        Entity killed = event.getEntity();

        if (killer instanceof PlayerEntity && killed instanceof MobEntity){
            PlayerEntity slayer = (PlayerEntity) killer;
            LivingEntity victim = (LivingEntity) killed;
            if (!(slayer instanceof FakePlayer)){
                GoldTotemItem.handleKill(slayer, victim);
            }
        }

        if (killer instanceof SummonedEntity){
            LivingEntity owner = ((SummonedEntity) killer).getTrueOwner();
            if (owner != null){
                if (owner instanceof PlayerEntity) {
                    if (RobeArmorFinder.FindArmor(owner)) {
                        PlayerEntity slayer = (PlayerEntity) owner;
                        LivingEntity victim = (LivingEntity) killed;
                        if (!(slayer instanceof FakePlayer)) {
                            GoldTotemItem.handleKill(slayer, victim);
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
                        PlayerEntity slayer = (PlayerEntity) owner;
                        LivingEntity victim = (LivingEntity) killed;
                        if (!(slayer instanceof FakePlayer)) {
                            GoldTotemItem.handleKill(slayer, victim);
                        }
                    }
                }
            }
        }

        if (event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            Minecraft minecraft = Minecraft.getInstance();
            if (GoldTotemItem.UndyingEffect(player)){
                player.setHealth(1.0F);
                player.removeAllEffects();
                player.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                player.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                player.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                new ParticleUtil(ParticleTypes.TOTEM_OF_UNDYING, player.getX(), player.getY(), player.getZ(), 0.0F, 0.0F, 0.0F);
                new SoundUtil(player.blockPosition(), SoundEvents.TOTEM_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                minecraft.gameRenderer.displayItemActivation(GoldTotemFinder.FindTotem(player));
                if (LichdomUtil.isLich(player)){
                    GoldTotemItem.setSoulsamount(GoldTotemFinder.FindTotem(player), 0);
                } else {
                    GoldTotemItem.EmptySoulTotem(player);
                }
                event.setCanceled(true);
            }
        }

    }

}
