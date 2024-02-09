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
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class MossySkeletonMinionEntity extends AbstractSMEntity {
    public MossySkeletonMinionEntity(EntityType<? extends AbstractSMEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.MossySkeletonServantHealth.get())
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.MossySkeletonServantDamage.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected SoundEvent getAmbientSound() {
        return ModSounds.MOSSY_SKELETON_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.MOSSY_SKELETON_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.MOSSY_SKELETON_DEATH.get();
    }

    protected SoundEvent getStepSound() {
        return ModSounds.MOSSY_SKELETON_STEP.get();
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

    @Override
    public SoundEvent getShootSound() {
        return ModSounds.MOSSY_SKELETON_SHOOT.get();
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag && pEntity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) pEntity;
            livingEntity.addEffect(new EffectInstance(Effects.POISON, 100));
        }

        return flag;
    }

    protected AbstractArrowEntity getMobArrow(ItemStack pArrowStack, float pDistanceFactor) {
        AbstractArrowEntity abstractarrowentity = super.getMobArrow(pArrowStack, pDistanceFactor);
        if (abstractarrowentity instanceof ArrowEntity) {
            ArrowEntity arrow = (ArrowEntity) abstractarrowentity;
            int amplifier = this.isUpgraded() ? 1 : 0;
            arrow.addEffect(new EffectInstance(Effects.POISON, 60, amplifier));
        }

        return abstractarrowentity;
    }
}
