package com.Polarice3.Goety.utils;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class EntityHelper {

    public static class MoveHelperController extends MovementController {
        public MoveHelperController(MobEntity vex) {
            super(vex);
        }

        public void tick() {
            if (this.operation == Action.MOVE_TO) {
                Vector3d vector3d = new Vector3d(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
                double d0 = vector3d.length();
                if (d0 < this.mob.getBoundingBox().getSize()) {
                    this.operation = Action.WAIT;
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().scale(0.5D));
                } else {
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(vector3d.scale(this.speedModifier * 0.05D / d0)));
                    if (this.mob.getTarget() == null) {
                        Vector3d vector3d1 = this.mob.getDeltaMovement();
                        this.mob.yRot = -((float) MathHelper.atan2(vector3d1.x, vector3d1.z)) * (180F / (float)Math.PI);
                    } else {
                        double d2 = this.mob.getTarget().getX() - this.mob.getX();
                        double d1 = this.mob.getTarget().getZ() - this.mob.getZ();
                        this.mob.yRot = -((float)MathHelper.atan2(d2, d1)) * (180F / (float)Math.PI);
                    }
                    this.mob.yBodyRot = this.mob.yRot;
                }

            }
        }
    }
    
}
