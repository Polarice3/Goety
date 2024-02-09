package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.AttributesConfig;
import net.minecraft.entity.EntityType;
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
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class StrayMinionEntity extends AbstractSMEntity {

    public StrayMinionEntity(EntityType<? extends AbstractSMEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.StrayServantHealth.get())
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.StrayServantDamage.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.STRAY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.STRAY_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.STRAY_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.STRAY_STEP;
    }

    protected AbstractArrowEntity getMobArrow(ItemStack pArrowStack, float pDistanceFactor) {
        AbstractArrowEntity abstractarrowentity = super.getMobArrow(pArrowStack, pDistanceFactor);
        if (abstractarrowentity instanceof ArrowEntity) {
            ((ArrowEntity)abstractarrowentity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 600));
        }

        return abstractarrowentity;
    }
}
