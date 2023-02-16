package com.Polarice3.Goety.common.entities.hostile.dead;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.Polarice3.Goety.common.entities.neutral.AbstractWraithEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModEntityType;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.EffectsUtil;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import java.util.Objects;

public class BlightEntity extends AbstractWraithEntity implements IDeadMob, IMob {

    public BlightEntity(EntityType<? extends SummonedEntity> p_i48553_1_, World p_i48553_2_) {
        super(p_i48553_1_, p_i48553_2_);
    }

    public void targetSelectGoal(){
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, DEAD_TARGETS));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.BlightHealth.get())
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.BlightDamage.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.BLIGHT_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.BLIGHT_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.BLIGHT_DEATH.get();
    }

    protected SoundEvent getAttackSound(){
        return ModSounds.BLIGHT_ATTACK.get();
    }

    protected SoundEvent getTeleportInSound() {
        return ModSounds.BLIGHT_TELEPORT.get();
    }

    protected SoundEvent getTeleportOutSound() {
        return ModSounds.BLIGHT_TELEPORT.get();
    }

    public void magicFire(LivingEntity livingEntity){
        if (this.level.random.nextFloat() <= 0.2F) {
            Entity summon1 = this.level.random.nextFloat() <= 0.05F ? new DesiccatedEntity(ModEntityType.DESICCATED.get(), this.level) : new BlightlingEntity(ModEntityType.BLIGHTLING.get(), this.level);
            Entity summon2 = this.level.random.nextFloat() <= 0.05F ? new DesiccatedEntity(ModEntityType.DESICCATED.get(), this.level) : new BlightlingEntity(ModEntityType.BLIGHTLING.get(), this.level);
            Entity summon3 = this.level.random.nextFloat() <= 0.05F ? new DesiccatedEntity(ModEntityType.DESICCATED.get(), this.level) : new BlightlingEntity(ModEntityType.BLIGHTLING.get(), this.level);
            Entity summon4 = this.level.random.nextFloat() <= 0.05F ? new DesiccatedEntity(ModEntityType.DESICCATED.get(), this.level) : new BlightlingEntity(ModEntityType.BLIGHTLING.get(), this.level);
            WandUtil.summoningCircles(this.level, this, livingEntity.position()
                    , summon1
                    , summon2
                    , summon3
                    , summon4);
        } else {
            if (this.level.random.nextFloat() <= 0.05F) {
                WandUtil.spawnXBlightFires(this.level, livingEntity.position(), this);
            } else {
                WandUtil.spawnBlightFires(this.level, livingEntity.position(), this);
            }
        }
    }

    public IParticleData getFireParticles(){
        return ModParticleTypes.TOTEM_EFFECT.get();
    }

    @Override
    public void desiccateTarget(LivingEntity pEntity) {
        if (pEntity.hasEffect(ModEffects.DESICCATE.get())){
            int d2 = Objects.requireNonNull(pEntity.getEffect(ModEffects.DESICCATE.get())).getDuration();
            EffectsUtil.resetDuration(pEntity, ModEffects.DESICCATE.get(), Math.max(d2, 200));
        } else {
            pEntity.addEffect(new EffectInstance(ModEffects.DESICCATE.get(), 200));
        }
    }
}
