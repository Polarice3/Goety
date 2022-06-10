package com.Polarice3.Goety.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.MathHelper;

public class EffectsUtil {
    public static void amplifyEffect(LivingEntity infected, Effect effect, int duration){
        EffectInstance effectinstance1 = infected.getEffect(effect);
        int i = 1;
        if (effectinstance1 != null) {
            i += effectinstance1.getAmplifier();
            infected.removeEffectNoUpdate(effect);
        } else {
            --i;
        }

        i = MathHelper.clamp(i, 0, 4);
        EffectInstance effectinstance = new EffectInstance(effect, duration, i);
        infected.addEffect(effectinstance);
    }

    public static void resetDuration(LivingEntity infected, Effect effect, int duration){
        EffectInstance effectinstance1 = infected.getEffect(effect);
        int a = 0;
        if (effectinstance1 != null) {
            a = effectinstance1.getAmplifier();
            infected.removeEffectNoUpdate(effect);
        }
        EffectInstance effectinstance = new EffectInstance(effect, duration, a);
        infected.addEffect(effectinstance);
    }

    public static void increaseDuration(LivingEntity infected, Effect effect, int duration){
        EffectInstance effectinstance1 = infected.getEffect(effect);
        int i = duration;
        int a = 0;
        if (effectinstance1 != null) {
            a = effectinstance1.getAmplifier();
            i = effectinstance1.getDuration() + duration;
            infected.removeEffectNoUpdate(effect);
        }
        EffectInstance effectinstance = new EffectInstance(effect, i, a);
        infected.addEffect(effectinstance);
    }
}
