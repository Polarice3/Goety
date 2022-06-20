package com.Polarice3.Goety.common.entities.ai;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.ally.LoyalSpiderEntity;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.utils.LichdomHelper;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;

public class AllyTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public AllyTargetGoal(OwnedEntity ownedEntity, Class<T> pClass) {
        super(ownedEntity, pClass, 5, false, false, (entity) ->
                entity instanceof IMob
                        && !(entity instanceof CreeperEntity && ownedEntity.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && MainConfig.MinionsAttackCreepers.get())
                        && !(entity instanceof SpiderEntity && ownedEntity.getTrueOwner() != null && RobeArmorFinder.FindFelHelm(ownedEntity.getTrueOwner()))
                        && !(entity.getMobType() == CreatureAttribute.UNDEAD && ownedEntity.getTrueOwner() != null && ownedEntity.getTrueOwner() instanceof PlayerEntity && LichdomHelper.isLich((PlayerEntity) ownedEntity.getTrueOwner()) && MainConfig.LichUndeadFriends.get())
                        && !(entity instanceof OwnedEntity && ((OwnedEntity) entity).getTrueOwner() == ownedEntity.getTrueOwner()));
    }

    public AllyTargetGoal(LoyalSpiderEntity loyalSpiderEntity, Class<T> pClass){
        super(loyalSpiderEntity, pClass, 5, false, false, (entity) ->
                entity instanceof IMob
                        && !(entity instanceof CreeperEntity && loyalSpiderEntity.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && MainConfig.MinionsAttackCreepers.get())
                        && !(entity instanceof SpiderEntity && loyalSpiderEntity.getTrueOwner() != null && RobeArmorFinder.FindFelHelm(loyalSpiderEntity.getTrueOwner()))
                        && !(entity.getMobType() == CreatureAttribute.UNDEAD && loyalSpiderEntity.getTrueOwner() != null && loyalSpiderEntity.getTrueOwner() instanceof PlayerEntity && LichdomHelper.isLich((PlayerEntity) loyalSpiderEntity.getTrueOwner()) && MainConfig.LichUndeadFriends.get())
                        && !(entity instanceof OwnedEntity && ((OwnedEntity) entity).getTrueOwner() == loyalSpiderEntity.getTrueOwner()));
    }
}
