package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.ColorUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class JungleZombieMinionEntity extends ZombieMinionEntity{
    public JungleZombieMinionEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.JungleZombieServantHealth.get())
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.JungleZombieServantDamage.get())
                .add(Attributes.ARMOR, AttributesConfig.JungleZombieServantArmor.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected boolean isSunSensitive() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.JUNGLE_ZOMBIE_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.JUNGLE_ZOMBIE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.JUNGLE_ZOMBIE_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return ModSounds.JUNGLE_ZOMBIE_STEP.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide){
            if (this.tickCount % 5 == 0 && this.level.random.nextBoolean()) {
                double[] colors = ColorUtil.rgbParticle(2735172);
                this.level.addParticle(ModParticleTypes.BIG_CULT_SPELL.get(), this.getX(), this.getY() + 1.0D, this.getZ(),
                        colors[0],
                        colors[1],
                        colors[2]);
            }
        }
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag && pEntity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) pEntity;
            livingEntity.addEffect(new EffectInstance(Effects.POISON, 100));
        }

        return flag;
    }
}
