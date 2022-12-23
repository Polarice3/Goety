package com.Polarice3.Goety.common.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;

public class NecroPowerEffect extends ModEffect{
    public NecroPowerEffect() {
        super(EffectType.NEUTRAL, 4393481);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "216a30a7-718d-4670-8cb1-65a6fda477df", (double)0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, "cb415f39-db7c-4f81-acd4-e8c76ab24d13", 3.0D, AttributeModifier.Operation.ADDITION);
        addAttributeModifier(Attributes.ARMOR, "d3587ee7-c989-4f74-b181-589002f3216f", 2.0D, AttributeModifier.Operation.ADDITION);
    }

    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeModifierManager pAttributeMap, int pAmplifier) {
        pLivingEntity.setAbsorptionAmount(pLivingEntity.getAbsorptionAmount() - (float)(4 * (pAmplifier + 1)));
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeModifierManager pAttributeMap, int pAmplifier) {
        pLivingEntity.setAbsorptionAmount(pLivingEntity.getAbsorptionAmount() + (float)(4 * (pAmplifier + 1)));
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }
}
