package com.Polarice3.Goety.common.entities.ai;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.util.math.MathHelper;

public class FrostBallGoal extends RangedAttackGoal {
    public IRangedAttackMob ghost;
    public float distance;

    public FrostBallGoal(IRangedAttackMob entity, float distance) {
        super(entity, 1.0D,  20, 40, 20.0F);
        this.ghost = entity;
        this.distance = distance;
    }

    public boolean canUse() {
        if (this.ghost instanceof MobEntity) {
            MobEntity ghost2 = (MobEntity) this.ghost;
            if (super.canUse() && ghost2.getTarget() != null) {
                return ghost2.distanceToSqr(ghost2.getTarget()) >= MathHelper.square(this.distance);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
