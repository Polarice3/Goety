package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.ParticleUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;

public class IllagueEvent {

    public void tick(ServerWorld level) {
        for (Entity entity: level.getAllEntities()){
            if (entity instanceof LivingEntity){
                LivingEntity infected = (LivingEntity) entity;
                if (infected.hasEffect(ModEffects.ILLAGUE.get())){
                    int d = Objects.requireNonNull(infected.getEffect(ModEffects.ILLAGUE.get())).getDuration() + 1;
                    int a = Objects.requireNonNull(infected.getEffect(ModEffects.ILLAGUE.get())).getAmplifier();
                    if (MainConfig.IllagueSpread.get()) {
                        for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, infected.getBoundingBox().inflate(8.0D))) {
                            if (!(livingEntity instanceof PatrollerEntity) && !livingEntity.hasEffect(ModEffects.ILLAGUE.get())) {
                                if (livingEntity instanceof PlayerEntity) {
                                    if (!((PlayerEntity) livingEntity).isCreative()) {
                                        livingEntity.addEffect(new EffectInstance(ModEffects.ILLAGUE.get(), d / 2, a, false, false));
                                    }
                                } else {
                                    livingEntity.addEffect(new EffectInstance(ModEffects.ILLAGUE.get(), d / 2, a, false, false));
                                }
                            }
                        }
                    }
                    if (infected.tickCount % 20 == 0){
                        for(int i = 0; i < 8; ++i) {
                            new ParticleUtil(ModParticleTypes.PLAGUE_EFFECT.get(), infected.getRandomX(0.5D), infected.getRandomY(), infected.getRandomZ(0.5D), 0.0D, 0.5D, 0.0D);
                        }
                    }
                    int i1 = MathHelper.clamp(a * 50, 0, 250);
                    int i2 = a + 1;
                    int i3 = i1 * 10;
                    int c;
                    switch (level.getDifficulty()){
                        default:
                        case EASY:
                            c = 6000;
                            break;
                        case NORMAL:
                            c = 4500;
                            break;
                        case HARD:
                            c = 3000;
                            break;
                    }
                    int random = level.random.nextInt(300 - i1);
                    if (random == 0 && level.getDifficulty() != Difficulty.PEACEFUL) {
                        int r = level.random.nextInt(8);
                        int r2 = level.random.nextInt(c - i3);
                        int r3 = level.random.nextInt(i2);
                        if (r2 == 0){
                            this.increaseIllague(infected);
                        }
                        if (infected instanceof PlayerEntity){
                            switch (r) {
                                case 0:
                                    infected.addEffect(new EffectInstance(Effects.WEAKNESS, 400 * r3, r3, false, false));
                                    break;
                                case 1:
                                    infected.addEffect(new EffectInstance(Effects.HUNGER, 400 * r3, r3, false, false));
                                    break;
                                case 2:
                                    infected.addEffect(new EffectInstance(Effects.CONFUSION, 400 * r3, 0, false, false));
                                    break;
                                case 3:
                                    infected.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400 * r3, r3, false, false));
                                    break;
                                case 4:
                                    infected.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 400 * r3, r3, false, false));
                                    break;
                                case 5:
                                    infected.addEffect(new EffectInstance(Effects.POISON, 400 * r3, r3, false, false));
                                    break;
                                case 6:
                                    infected.addEffect(new EffectInstance(Effects.BLINDNESS, 400 * r3, 0, false, false));
                                    break;
                                case 7:
                                    infected.addEffect(new EffectInstance(Effects.WITHER, 100, r3, false, false));
                                    break;
                            }
                        } else {
                            switch (r) {
                                case 0:
                                    infected.addEffect(new EffectInstance(Effects.WEAKNESS, 400 * r3, r3, false, false));
                                    break;
                                case 1:
                                case 2:
                                case 3:
                                    infected.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400 * r3, r3, false, false));
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                    infected.addEffect(new EffectInstance(Effects.POISON, 400 * r3, r3, false, false));
                                    break;
                                case 7:
                                    infected.addEffect(new EffectInstance(Effects.WITHER, 100, r3, false, false));
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void increaseIllague(LivingEntity infected){
        EffectInstance effectinstance1 = infected.getEffect(ModEffects.ILLAGUE.get());
        int i = 1;
        if (effectinstance1 != null) {
            i += effectinstance1.getAmplifier();
            infected.removeEffectNoUpdate(ModEffects.ILLAGUE.get());
        } else {
            --i;
        }

        i = MathHelper.clamp(i, 0, 4);
        EffectInstance effectinstance = new EffectInstance(ModEffects.ILLAGUE.get(), 6000, i, false, false, true);
        infected.addEffect(effectinstance);
    }
}
