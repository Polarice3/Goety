package com.Polarice3.Goety.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;

public class ModDamageSource extends DamageSource {
    private boolean isBreath;

    public ModDamageSource(String pMessageId) {
        super(pMessageId);
    }

    public static DamageSource fireBreath(LivingEntity pMob) {
        return new ModEntityDamageSource("fire_breath", pMob).setBreath().setIsFire();
    }

    public boolean isBreath() {
        return this.isBreath;
    }

    public DamageSource setBreath() {
        this.isBreath = true;
        return this;
    }

}
