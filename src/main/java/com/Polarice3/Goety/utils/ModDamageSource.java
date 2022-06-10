package com.Polarice3.Goety.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;

public class ModDamageSource extends DamageSource {
    public static final DamageSource DESICCATE = (new ModDamageSource("desiccate")).setDesiccate().bypassArmor().setMagic();
    private boolean isBreath;
    private boolean isDesiccate;

    public ModDamageSource(String pMessageId) {
        super(pMessageId);
    }

    public static DamageSource fireBreath(LivingEntity pMob) {
        return new ModEntityDamageSource("fire_breath", pMob).setBreath().setIsFire();
    }

    public static DamageSource directDesiccate(LivingEntity pMob) {
        return new ModEntityDamageSource("directDesiccate", pMob).setDesiccate().bypassArmor().setMagic();
    }

    public static DamageSource indirectDesiccate(Entity pSource, @Nullable Entity pIndirectEntity) {
        return (new ModIndirectEntityDamageSource("indirectDesiccate", pSource, pIndirectEntity)).setDesiccate().bypassArmor().setMagic();
    }

    public boolean isBreath() {
        return this.isBreath;
    }

    public DamageSource setBreath() {
        this.isBreath = true;
        return this;
    }

    public boolean isDesiccate() {
        return this.isDesiccate;
    }

    public DamageSource setDesiccate() {
        this.isDesiccate = true;
        return this;
    }

    public static boolean desiccateAttacks(DamageSource source){
        return source == ModDamageSource.DESICCATE || (source instanceof ModDamageSource && ((ModDamageSource) source).isDesiccate());
    }

}
