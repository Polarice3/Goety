package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.hostile.ParasiteEntity;
import com.Polarice3.Goety.common.entities.neutral.MinionEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HostedEvent {

    @SubscribeEvent
    public static void HostedEffect(LivingEvent.LivingUpdateEvent event){
        LivingEntity host = event.getEntityLiving();
        World level = event.getEntity().getCommandSenderWorld();
        if (host.hasEffect(ModEffects.HOSTED.get())) {
            if (host instanceof AbstractSkeletonEntity || host instanceof ParasiteEntity
                    || host instanceof VexEntity || host instanceof MinionEntity
                    || host instanceof BlazeEntity || host instanceof MagmaCubeEntity
                    || host instanceof GuardianEntity || host instanceof IronGolemEntity
                    || host instanceof SilverfishEntity || host instanceof EndermiteEntity || host instanceof WitherEntity){
                host.removeEffect(ModEffects.HOSTED.get());
            } else if (host instanceof EndermanEntity){
                int amp = Objects.requireNonNull(host.getEffect(ModEffects.HOSTED.get())).getAmplifier() * 10;
                int random = level.random.nextInt(120 - amp);
                if (random == 0) {
                    EndermiteEntity parasiteEntity = new EndermiteEntity(EntityType.ENDERMITE, level);
                    parasiteEntity.setPos(host.getX(), host.getY(), host.getZ());
                    parasiteEntity.setTarget(host);
                    level.addFreshEntity(parasiteEntity);
                }
            } else {
                int amp = Objects.requireNonNull(host.getEffect(ModEffects.HOSTED.get())).getAmplifier() * 10;
                int random = level.random.nextInt(120 - amp);
                if (random == 0) {
                    ParasiteEntity parasiteEntity = new ParasiteEntity(ModEntityType.PARASITE.get(), level);
                    parasiteEntity.setPos(host.getX(), host.getY(), host.getZ());
                    parasiteEntity.setTarget(host);
                    level.addFreshEntity(parasiteEntity);
                }
            }
        }
    }
}
