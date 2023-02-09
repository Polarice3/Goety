package com.Polarice3.Goety.common.entities.ai;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.SpellConfig;
import com.Polarice3.Goety.common.entities.neutral.IOwned;
import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.Polarice3.Goety.utils.LichdomHelper;
import com.Polarice3.Goety.utils.RobeArmorFinder;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;

import java.util.function.Predicate;

public class SummonTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<LivingEntity> {

    public SummonTargetGoal(MobEntity ownedEntity) {
        super(ownedEntity, LivingEntity.class, 5, true, false, predicate(ownedEntity));
    }

    public SummonTargetGoal(MobEntity ownedEntity, boolean pMustSee, boolean pMustReach) {
        super(ownedEntity, LivingEntity.class, 5, pMustSee, pMustReach, predicate(ownedEntity));
    }

    public boolean canUse() {
        boolean flag = super.canUse();
        if (this.mob instanceof OwnedEntity && ((OwnedEntity) this.mob).isHostile()){
            return !(this.target instanceof IMob) && flag;
        }
        return flag;
    }

    public static Predicate<LivingEntity> predicate(LivingEntity livingEntity){
        if (livingEntity instanceof IOwned){
            IOwned ownedEntity = (IOwned) livingEntity;
            if (ownedEntity.getTrueOwner() instanceof IMob || ownedEntity instanceof IMob || (ownedEntity instanceof OwnedEntity && ((OwnedEntity)ownedEntity).isHostile())){
                return (entity) -> entity instanceof PlayerEntity;
            } else {
                return (entity) ->
                        entity instanceof IMob
                                && !(entity instanceof CreeperEntity && entity.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && SpellConfig.MinionsAttackCreepers.get())
                                && !(entity instanceof SpiderEntity && ownedEntity.getTrueOwner() != null && RobeArmorFinder.FindFelHelm(ownedEntity.getTrueOwner()))
                                && !(entity instanceof IAngerable && ((ownedEntity.getTrueOwner() != null && ((IAngerable) entity).getTarget() != ownedEntity.getTrueOwner()) || ((IAngerable) entity).getTarget() != ownedEntity))
                                && !(entity.getMobType() == CreatureAttribute.UNDEAD && ownedEntity.getTrueOwner() != null && ownedEntity.getTrueOwner() instanceof PlayerEntity && LichdomHelper.isLich((PlayerEntity) ownedEntity.getTrueOwner()) && MainConfig.LichUndeadFriends.get())
                                && !(entity instanceof IOwned && ((IOwned) entity).getTrueOwner() == ownedEntity.getTrueOwner() && ownedEntity.getTrueOwner() != null);
            }
        } else {
            return null;
        }
    }
}
