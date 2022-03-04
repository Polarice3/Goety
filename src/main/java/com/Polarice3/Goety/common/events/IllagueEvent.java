package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModRegistryHandler;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class IllagueEvent {

    @SubscribeEvent
    public static void IllagueEffect(LivingEvent.LivingUpdateEvent event) {
        LivingEntity infected = event.getEntityLiving();
        World level = event.getEntity().getCommandSenderWorld();
        if (infected != null) {
            if (infected.hasEffect(ModRegistryHandler.ILLAGUE.get())) {
                for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, infected.getBoundingBox().inflate(8.0D))) {
                    if (livingEntity.getMobType() != CreatureAttribute.UNDEAD || livingEntity.getMobType() != CreatureAttribute.ILLAGER
                            || !livingEntity.hasEffect(ModRegistryHandler.ILLAGUE.get())) {
                        livingEntity.addEffect(new EffectInstance(ModRegistryHandler.ILLAGUE.get(), 12000));
                    }
                }
                int amp = Objects.requireNonNull(infected.getEffect(ModRegistryHandler.ILLAGUE.get())).getAmplifier();
                int i1 = amp * 50;
                int i2 = amp + 1;
                int i3 = MathHelper.clamp(i2, 0, 5);
                int random = level.random.nextInt(600 - i1);
                if (random == 0) {
                    int r = level.random.nextInt(6);
                    int r2 = level.random.nextInt(12000);
                    int a = level.random.nextInt(i2);
                    if (r2 == 0){
                        infected.addEffect(new EffectInstance(ModRegistryHandler.ILLAGUE.get(), 12000, i3));
                    }
                    switch (r) {
                        case 0:
                            infected.addEffect(new EffectInstance(Effects.WEAKNESS, 400, a));
                            break;
                        case 1:
                            infected.addEffect(new EffectInstance(Effects.HUNGER, 400, a));
                            break;
                        case 2:
                            infected.addEffect(new EffectInstance(Effects.CONFUSION, 400));
                            break;
                        case 3:
                            infected.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400, a));
                            break;
                        case 4:
                            infected.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 400, a));
                            break;
                        case 5:
                            infected.addEffect(new EffectInstance(Effects.POISON, 400, a));
                            break;
                    }
                }
            }
        }
    }
}
