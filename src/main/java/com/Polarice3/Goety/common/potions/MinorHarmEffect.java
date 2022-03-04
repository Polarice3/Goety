package com.Polarice3.Goety.common.potions;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;

public class MinorHarmEffect extends ModEffects{
    public MinorHarmEffect() {
        super(EffectType.HARMFUL, 4393481);
    }

    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        if (!entityLivingBaseIn.isInvertedHealAndHarm()){
            entityLivingBaseIn.hurt(DamageSource.MAGIC, (float)(2 << amplifier));
        } else {
            entityLivingBaseIn.heal((float)Math.max(2 << amplifier, 0));
        }
    }

    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity entityLivingBaseIn, int amplifier, double health) {
        if (!entityLivingBaseIn.isInvertedHealAndHarm()) {
            int j = (int) (health * (double) (2 << amplifier) + 0.5D);
            if (source == null) {
                entityLivingBaseIn.hurt(DamageSource.MAGIC, (float) j);
            } else {
                entityLivingBaseIn.hurt(DamageSource.indirectMagic(source, indirectSource), (float) j);
            }
            this.performEffect(entityLivingBaseIn, amplifier);
        } else {
            int i = (int)(health * (double)(2 << amplifier) + 0.5D);
            entityLivingBaseIn.heal((float)i);
        }
    }

}
