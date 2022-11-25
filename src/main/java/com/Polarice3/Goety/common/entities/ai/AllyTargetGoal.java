package com.Polarice3.Goety.common.entities.ai;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.neutral.IOwned;
import com.Polarice3.Goety.utils.LichdomHelper;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;

import java.util.function.Predicate;

public class AllyTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public AllyTargetGoal(MobEntity ownedEntity, Class<T> pClass) {
        super(ownedEntity, pClass, 5, false, false, predicate(ownedEntity));
    }

    public static Predicate<LivingEntity> predicate(LivingEntity livingEntity){
        if (livingEntity instanceof IOwned){
            IOwned ownedEntity = (IOwned) livingEntity;
            if (ownedEntity.getTrueOwner() instanceof MonsterEntity){
                return (entity) -> entity instanceof PlayerEntity
                        && !(entity instanceof IOwned && ((IOwned) entity).getTrueOwner() == ownedEntity.getTrueOwner());
            } else {
                return (entity) ->
                        entity instanceof IMob
                                && !(entity instanceof CreeperEntity && entity.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && MainConfig.MinionsAttackCreepers.get())
                                && !(entity instanceof SpiderEntity && ownedEntity.getTrueOwner() != null && RobeArmorFinder.FindFelHelm(ownedEntity.getTrueOwner()))
                                && !(entity.getMobType() == CreatureAttribute.UNDEAD && ownedEntity.getTrueOwner() != null && ownedEntity.getTrueOwner() instanceof PlayerEntity && LichdomHelper.isLich((PlayerEntity) ownedEntity.getTrueOwner()) && MainConfig.LichUndeadFriends.get())
                                && !(entity instanceof IOwned && ((IOwned) entity).getTrueOwner() == ownedEntity.getTrueOwner());
            }
        } else {
            return null;
        }
    }
}
