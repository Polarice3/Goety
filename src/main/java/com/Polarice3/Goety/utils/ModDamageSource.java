package com.Polarice3.Goety.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ModDamageSource extends DamageSource {
    public static final DamageSource DESICCATE = new ModDamageSource(source("desiccate")).bypassArmor().setMagic();
    public static final DamageSource FROST = new ModDamageSource(source("frost")).setMagic();
    public static final DamageSource ROOTS = new ModDamageSource(source("roots"));

    public ModDamageSource(String pMessageId) {
        super(pMessageId);
    }

    public static DamageSource fireBreath(LivingEntity pMob) {
        return new EntityDamageSource(source("fire_breath"), pMob).setIsFire();
    }

    public static DamageSource frostBreath(LivingEntity pMob){
        return new EntityDamageSource(source("frost_breath"), pMob);
    }

    public static DamageSource directDesiccate(LivingEntity pMob) {
        return new EntityDamageSource(source("directDesiccate"), pMob).bypassArmor().setMagic();
    }

    public static DamageSource indirectDesiccate(Entity pSource, @Nullable Entity pIndirectEntity) {
        return (new IndirectEntityDamageSource(source("indirectDesiccate"), pSource, pIndirectEntity)).bypassArmor().setMagic();
    }

    public static DamageSource directFrost(LivingEntity pMob) {
        return new EntityDamageSource(source("directFrost"), pMob).setMagic();
    }

    public static DamageSource indirectFrost(Entity pSource, @Nullable Entity pIndirectEntity) {
        return (new IndirectEntityDamageSource(source("indirectFrost"), pSource, pIndirectEntity)).setMagic();
    }

    public static DamageSource sonicBoom(Entity p_216877_) {
        return (new EntityDamageSource(source("sonic_boom"), p_216877_)).bypassArmor().bypassMagic().setMagic();
    }

    public static DamageSource modFireball(@Nullable Entity pIndirectEntity, World world){
        FireballEntity fireball = new FireballEntity(EntityType.FIREBALL, world);
        return pIndirectEntity == null ? (new IndirectEntityDamageSource("onFire", fireball, fireball)).setIsFire().setProjectile() : (new IndirectEntityDamageSource("fireball", fireball, pIndirectEntity)).setIsFire().setProjectile();
    }

    public static DamageSource sword(Entity pSource, @Nullable Entity pIndirectEntity){
        return (new IndirectEntityDamageSource(source("sword"), pSource, pIndirectEntity)).setProjectile();
    }

    public static DamageSource notThorns(PlayerEntity pPlayer){
        return new EntityDamageSource("player", pPlayer).setThorns();
    }

    public static DamageSource roots(Entity pSource, @Nullable Entity pIndirectEntity){
        return (new IndirectEntityDamageSource(source("indirectRoots"), pSource, pIndirectEntity));
    }

    public static DamageSource magicFire(Entity pSource, @Nullable Entity pIndirectEntity){
        return (new IndirectEntityDamageSource(source("magicFire"), pSource, pIndirectEntity)).bypassArmor().setIsFire();
    }

    public static boolean breathAttacks(DamageSource source){
        return source.getMsgId().contains("breath");
    }

    public static boolean desiccateAttacks(DamageSource source){
        return source.getMsgId().contains("desiccate") || source.getMsgId().contains("Desiccate");
    }

    public static boolean frostAttacks(DamageSource source){
        return source.getMsgId().contains("frost") || source.getMsgId().contains("Frost") || source.getMsgId().contains("ice")
                || source.getMsgId().contains("Ice") || source.getMsgId().contains("freeze") || source.getMsgId().contains("Freeze");
    }

    public static boolean physicalAttacks(DamageSource source){
        return source.getDirectEntity() != null && source.getDirectEntity() instanceof LivingEntity
                && source instanceof EntityDamageSource
                && (source.getMsgId().equals("mob") || source.getMsgId().equals("player"));
    }

    public static boolean rootAttacks(DamageSource source){
        return source.getMsgId().equals(source("indirectRoots")) || source.getMsgId().equals(source("roots"));
    }

    public static boolean magicFireAttacks(DamageSource source){
        return source.getMsgId().equals(source("magicFire"));
    }

    public static boolean noKnockBackAttacks(DamageSource source){
        return rootAttacks(source) || magicFireAttacks(source);
    }

    public static String source(String source){
        return "goety." + source;
    }

}
