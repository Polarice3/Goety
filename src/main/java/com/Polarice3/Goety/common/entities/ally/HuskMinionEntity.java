package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.AttributesConfig;
import com.Polarice3.Goety.init.ModEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class HuskMinionEntity extends ZombieMinionEntity{
    public HuskMinionEntity(EntityType<? extends SummonedEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, AttributesConfig.HuskServantHealth.get())
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.23F)
                .add(Attributes.ATTACK_DAMAGE, AttributesConfig.HuskServantDamage.get())
                .add(Attributes.ARMOR, AttributesConfig.HuskServantArmor.get());
    }

    public AttributeModifierMap.MutableAttribute getConfiguredAttributes(){
        return setCustomAttributes();
    }

    protected boolean isSunSensitive() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.HUSK_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.HUSK_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.HUSK_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.HUSK_STEP;
    }

    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag && this.getMainHandItem().isEmpty() && pEntity instanceof LivingEntity) {
            float f = this.level.getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            ((LivingEntity)pEntity).addEffect(new EffectInstance(Effects.HUNGER, 140 * (int)f));
        }

        return flag;
    }

    protected boolean convertsInWater() {
        return true;
    }

    protected void doUnderWaterConversion() {
        this.convertToZombieType(ModEntityType.ZOMBIE_MINION.get());
        if (!this.isSilent()) {
            this.level.levelEvent((PlayerEntity)null, 1041, this.blockPosition(), 0);
        }

    }

}
