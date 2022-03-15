package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.init.ModRegistry;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.player.PlayerEntity;
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
            if (infected.hasEffect(ModRegistry.ILLAGUE.get())) {
                int d = Objects.requireNonNull(infected.getEffect(ModRegistry.ILLAGUE.get())).getDuration() + 1;
                if (MainConfig.IllagueSpread.get()) {
                    for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, infected.getBoundingBox().inflate(8.0D))) {
                        if (!(livingEntity instanceof PatrollerEntity) && !livingEntity.hasEffect(ModRegistry.ILLAGUE.get())) {
                            if (livingEntity instanceof PlayerEntity) {
                                if (!((PlayerEntity) livingEntity).isCreative()) {
                                    livingEntity.addEffect(new EffectInstance(ModRegistry.ILLAGUE.get(), d / 2, 0, false, false));
                                }
                            } else {
                                livingEntity.addEffect(new EffectInstance(ModRegistry.ILLAGUE.get(), d / 2, 0, false, false));
                            }
                        }
                    }
                }
                if (level.isClientSide) {
                    for(int i = 0; i < 2; ++i) {
                        new ParticleUtil(ModParticleTypes.PLAGUE_EFFECT.get(), infected.getRandomX(0.5D), infected.getRandomY(), infected.getRandomZ(0.5D), 0.0D, 0.5D, 0.0D);
                    }
                }
                int amp = Objects.requireNonNull(infected.getEffect(ModRegistry.ILLAGUE.get())).getAmplifier();
                int i1 = amp * 50;
                int i2 = amp + 1;
                int i3 = MathHelper.clamp(i2, 0, 5);
                int random = level.random.nextInt(600 - i1);
                if (random == 0) {
                    int r = level.random.nextInt(7);
                    int r2 = level.random.nextInt(12000);
                    int a = level.random.nextInt(i2);
                    if (r2 == 0){
                        infected.addEffect(new EffectInstance(ModRegistry.ILLAGUE.get(), 12000, i3, false, false));
                    }
                    if (infected instanceof PlayerEntity){
                        switch (r) {
                            case 0:
                                infected.addEffect(new EffectInstance(Effects.WEAKNESS, 400, a, false, false));
                                break;
                            case 1:
                                infected.addEffect(new EffectInstance(Effects.HUNGER, 400, a, false, false));
                                break;
                            case 2:
                                infected.addEffect(new EffectInstance(Effects.CONFUSION, 400, 0, false, false));
                                break;
                            case 3:
                                infected.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400, a, false, false));
                                break;
                            case 4:
                                infected.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 400, a, false, false));
                                break;
                            case 5:
                                infected.addEffect(new EffectInstance(Effects.POISON, 400, a, false, false));
                                break;
                            case 6:
                                infected.addEffect(new EffectInstance(Effects.BLINDNESS, 400, 0, false, false));
                                break;
                        }
                    } else {
                        switch (r) {
                            case 0:
                                infected.addEffect(new EffectInstance(Effects.WEAKNESS, 400, a, false, false));
                                break;
                            case 1:
                            case 2:
                            case 3:
                                infected.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400, a, false, false));
                                break;
                            case 4:
                            case 5:
                            case 6:
                                infected.addEffect(new EffectInstance(Effects.POISON, 400, a, false, false));
                                break;
                        }
                    }
                }
            }
        }
    }
}
