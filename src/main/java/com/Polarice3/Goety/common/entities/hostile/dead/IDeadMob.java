package com.Polarice3.Goety.common.entities.hostile.dead;

import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.world.GameRules;

import java.util.function.Predicate;

public interface IDeadMob {
    Predicate<LivingEntity> DEAD_TARGETS = (livingEntity) -> {
        return livingEntity.getMobType() != CreatureAttribute.UNDEAD
                && livingEntity.getMobType() != CreatureAttribute.WATER
                && !(livingEntity instanceof IDeadMob)
                && !(livingEntity instanceof CreeperEntity && livingEntity.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))
                && !(livingEntity instanceof AbstractCultistEntity)
                && !(livingEntity instanceof WitchEntity)
                && livingEntity.attackable();
    };

    void desiccateTarget(LivingEntity pEntity);

}
