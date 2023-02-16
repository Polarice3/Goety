package com.Polarice3.Goety.common.entities.neutral;

import com.Polarice3.Goety.AttributesConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Customizable Attribute codes based from @AlexModGuy
 */
public interface ICustomAttributes {
    Map<EntityType<? extends LivingEntity>, AttributeModifierMap> ATTRIBUTE_MODIFIER_MAP = new HashMap<>();

    static <T extends LivingEntity & ICustomAttributes> void applyAttributesForEntity(EntityType<? extends LivingEntity> type, T entity) {
        entity.attributes = new AttributeModifierManager(getAttributesForEntity(type, entity));
        entity.setHealth(entity.getMaxHealth());
    }

    static<T extends ICustomAttributes> AttributeModifierMap getAttributesForEntity(EntityType<? extends LivingEntity> type, T entity) {
        AttributeModifierMap originalAttributes = GlobalEntityTypeAttributes.getSupplier(type);
        if (!AttributesConfig.OverrideAttributes.get()){
            return originalAttributes;
        }
        if (ATTRIBUTE_MODIFIER_MAP.containsKey(type)) {
            return ATTRIBUTE_MODIFIER_MAP.get(type);
        }
        AttributeModifierMap.MutableAttribute originalMutable = new AttributeModifierMap.MutableAttribute(originalAttributes);
        if (entity.getConfiguredAttributes() != null) {
            originalMutable.combine(entity.getConfiguredAttributes());
        }
        AttributeModifierMap newAttributeMap = originalMutable.build();
        ATTRIBUTE_MODIFIER_MAP.put(type, newAttributeMap);
        return newAttributeMap;
    }

    AttributeModifierMap.MutableAttribute getConfiguredAttributes();
}
