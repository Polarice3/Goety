package com.Polarice3.Goety.common.events;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.utils.DeadSandExplosion;
import com.Polarice3.Goety.utils.EffectsUtil;
import com.Polarice3.Goety.utils.ExplosionUtil;
import com.Polarice3.Goety.utils.ModDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;

public class EffectsEvent {

    public void tick(ServerWorld level) {
        for (Entity entity: level.getAllEntities()){
            if (entity instanceof LivingEntity){
                LivingEntity infected = (LivingEntity) entity;
                if (infected.hasEffect(ModEffects.ILLAGUE.get())){
                    this.Illague(level, infected);
                }
                if (infected.hasEffect(ModEffects.DESICCATE.get())){
                    this.Desiccate(level, infected);
                }
                if (infected.hasEffect(ModEffects.COSMIC.get())){
                    this.Cosmic(level, infected);
                }
            }
        }
    }

    public void Illague(ServerWorld level, LivingEntity infected){
        int duration = Objects.requireNonNull(infected.getEffect(ModEffects.ILLAGUE.get())).getDuration() + 1;
        int amplifier = Objects.requireNonNull(infected.getEffect(ModEffects.ILLAGUE.get())).getAmplifier();
        if (MainConfig.IllagueSpread.get()) {
            for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, infected.getBoundingBox().inflate(8.0D))) {
                if (!(livingEntity instanceof PatrollerEntity) && !(livingEntity instanceof IDeadMob) && !livingEntity.hasEffect(ModEffects.ILLAGUE.get())) {
                    if (livingEntity instanceof PlayerEntity) {
                        if (!((PlayerEntity) livingEntity).isCreative()) {
                            livingEntity.addEffect(new EffectInstance(ModEffects.ILLAGUE.get(), duration / 2, amplifier, false, false));
                        }
                    } else {
                        livingEntity.addEffect(new EffectInstance(ModEffects.ILLAGUE.get(), duration / 2, amplifier, false, false));
                    }
                }
            }
        }
        if (infected.tickCount % 20 == 0){
            for(int i = 0; i < 8; ++i) {
                level.sendParticles(ModParticleTypes.PLAGUE_EFFECT.get(), infected.getRandomX(0.5D), infected.getRandomY(), infected.getRandomZ(0.5D), 1, 0.0D, 0.5D, 0.0D, 0);
            }
        }
        int i1 = MathHelper.clamp(amplifier * 50, 0, 250);
        int i2 = amplifier + 1;
        int i3 = i1 * 10;
        int c;
        switch (level.getDifficulty()){
            default:
            case EASY:
                c = 4500;
                break;
            case NORMAL:
                c = 3000;
                break;
            case HARD:
                c = 1500;
                break;
        }
        int k = 600 >> amplifier;
        if (k > 0) {
            if ((infected.tickCount % k == 0) && level.getDifficulty() != Difficulty.PEACEFUL) {
                int r = level.random.nextInt(8);
                int r2 = level.random.nextInt(c - i3);
                int r3 = level.random.nextInt(i2);
                int r4 = r3 + 1;
                if (r2 == 0) {
                    EffectsUtil.amplifyEffect(infected, ModEffects.ILLAGUE.get(), 6000);
                }
                if (infected instanceof PlayerEntity) {
                    switch (r) {
                        case 0:
                            infected.addEffect(new EffectInstance(Effects.WEAKNESS, 400 * r4, r3, false, false));
                            break;
                        case 1:
                            infected.addEffect(new EffectInstance(Effects.HUNGER, 400 * r4, r3, false, false));
                            break;
                        case 2:
                            infected.addEffect(new EffectInstance(Effects.CONFUSION, 400 * r4, 0, false, false));
                            break;
                        case 3:
                            infected.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400 * r4, r3, false, false));
                            break;
                        case 4:
                            infected.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 400 * r4, r3, false, false));
                            break;
                        case 5:
                            infected.addEffect(new EffectInstance(Effects.POISON, 400 * r4, r3, false, false));
                            break;
                        case 6:
                            infected.addEffect(new EffectInstance(Effects.BLINDNESS, 400 * r4, 0, false, false));
                            break;
                        case 7:
                            infected.addEffect(new EffectInstance(Effects.WITHER, 100, r3, false, false));
                            break;
                    }
                } else {
                    switch (r) {
                        case 0:
                            infected.addEffect(new EffectInstance(Effects.WEAKNESS, 400 * r4, r3, false, false));
                            break;
                        case 1:
                        case 2:
                        case 3:
                            infected.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400 * r4, r3, false, false));
                            break;
                        case 4:
                        case 5:
                        case 6:
                            infected.addEffect(new EffectInstance(Effects.POISON, 400 * r4, r3, false, false));
                            break;
                        case 7:
                            infected.addEffect(new EffectInstance(Effects.WITHER, 100, r3, false, false));
                            break;
                    }
                }
            }
        }
    }

    public void Desiccate(ServerWorld level, LivingEntity infected){
        int a = Objects.requireNonNull(infected.getEffect(ModEffects.DESICCATE.get())).getAmplifier();
        int i = 100 >> a;
        if (i > 0) {
            if (infected.tickCount % i == 0 && level.random.nextBoolean()) {
                if (infected.hurt(ModDamageSource.DESICCATE, 2.0F)) {
                    if (a > 2) {
                        DeadSandExplosion.Mode mode = level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && MainConfig.DeadSandSpread.get() ? DeadSandExplosion.Mode.SPREAD : DeadSandExplosion.Mode.NONE;
                        ExplosionUtil.deadSandExplode(level, infected, infected.getX(), infected.getY(), infected.getZ(), 1.0F, mode);
                    }
                }
            }
        }
    }

    public void Cosmic(ServerWorld level, LivingEntity pLivingEntity){
        int amplifier = Objects.requireNonNull(pLivingEntity.getEffect(ModEffects.COSMIC.get())).getAmplifier();
        int k = 1200 >> amplifier;
        if (k > 0) {
            if (pLivingEntity.tickCount % k == 0) {
                if (pLivingEntity instanceof PlayerEntity) {
                    int r = level.random.nextInt(20);
                    int a = level.random.nextInt(5);
                    switch (r) {
                        case 0:
                            pLivingEntity.addEffect(new EffectInstance(Effects.WEAKNESS, 400, a));
                            break;
                        case 1:
                            pLivingEntity.addEffect(new EffectInstance(Effects.DAMAGE_BOOST, 400, a));
                            break;
                        case 2:
                            pLivingEntity.addEffect(new EffectInstance(Effects.HUNGER, 400, a));
                            break;
                        case 3:
                            pLivingEntity.addEffect(new EffectInstance(Effects.CONFUSION, 400));
                            break;
                        case 4:
                            pLivingEntity.addEffect(new EffectInstance(Effects.REGENERATION, 400, a));
                            break;
                        case 5:
                            pLivingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 400, a));
                            break;
                        case 6:
                            pLivingEntity.addEffect(new EffectInstance(Effects.INVISIBILITY, 400));
                            break;
                        case 7:
                            pLivingEntity.addEffect(new EffectInstance(Effects.GLOWING, 400));
                            break;
                        case 8:
                            pLivingEntity.addEffect(new EffectInstance(Effects.BLINDNESS, 400));
                            break;
                        case 9:
                            pLivingEntity.addEffect(new EffectInstance(Effects.NIGHT_VISION, 400));
                            break;
                        case 10:
                            pLivingEntity.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 400, a));
                            break;
                        case 11:
                            pLivingEntity.addEffect(new EffectInstance(Effects.DIG_SPEED, 400, a));
                            break;
                        case 12:
                            pLivingEntity.addEffect(new EffectInstance(Effects.HEALTH_BOOST, 400, a));
                            break;
                        case 13:
                            pLivingEntity.addEffect(new EffectInstance(ModEffects.HOSTED.get(), 400, a));
                            break;
                        case 14:
                            pLivingEntity.addEffect(new EffectInstance(ModEffects.SAPPED.get(), 400, a));
                            break;
                        case 15:
                            pLivingEntity.addEffect(new EffectInstance(Effects.UNLUCK, 400, a));
                            break;
                        case 16:
                            pLivingEntity.addEffect(new EffectInstance(Effects.ABSORPTION, 400, a));
                            break;
                        case 17:
                            pLivingEntity.addEffect(new EffectInstance(Effects.LEVITATION, 400, a));
                            break;
                        case 18:
                            pLivingEntity.addEffect(new EffectInstance(ModEffects.HEALTH_LOSS.get(), 400, a));
                            break;
                    }
                }
            }
        }
    }
}
