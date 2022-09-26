package com.Polarice3.Goety.common.potions;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class DesiccateEffect extends Effect {

    public DesiccateEffect() {
        super(EffectType.HARMFUL, 5064781);
        addAttributeModifier(Attributes.ATTACK_DAMAGE, "e6a036bf-903a-4574-926e-61a772170d97", -1.0D, AttributeModifier.Operation.ADDITION);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "7ad88b67-392d-4d87-a6a5-08c08c2f86eb", (double)-0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        addAttributeModifier(Attributes.ATTACK_SPEED, "ac113f1f-ef15-4e4f-9c7d-44be1d0e06a9", (double)-0.1F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
