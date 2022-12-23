package com.Polarice3.Goety.common.entities.ai;

import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

import java.util.function.Predicate;

public class TargetHostileOwnedGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public TargetHostileOwnedGoal(MobEntity golem, Class<T> pClass) {
        super(golem, pClass, 5, false, false, predicate());
    }

    public static Predicate<LivingEntity> predicate(){
        return (entity) ->
                entity instanceof OwnedEntity
                        && ((OwnedEntity) entity).isHostile();
    }
}
