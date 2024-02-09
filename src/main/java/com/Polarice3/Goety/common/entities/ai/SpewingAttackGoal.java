package com.Polarice3.Goety.common.entities.ai;

import com.Polarice3.Goety.api.entities.IBreathing;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

/**
 * Spewing Attack Goal Codes based of codes from @TeamTwilight
 */
public class SpewingAttackGoal<T extends MobEntity & IBreathing> extends Goal {
    private final T attacker;
    private LivingEntity attackTarget;
    private double spewX;
    private double spewY;
    private double spewZ;
    private final int maxDuration;
    private final float attackChance;
    private final float spewingRange;
    private int durationLeft;

    public SpewingAttackGoal(T pLivingEntity, float pRange, int pSeconds, float pFloatChance) {
        this.attacker = pLivingEntity;
        this.spewingRange = pRange;
        this.maxDuration = pSeconds;
        this.attackChance = pFloatChance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    public boolean canUse() {
        this.attackTarget = this.attacker.getTarget();
        if (this.attackTarget != null
                && this.attacker.distanceTo(attackTarget) <= this.spewingRange
                && this.attacker.getSensing().canSee(attackTarget)) {
            return this.attacker.getRandom().nextFloat() < this.attackChance;
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        if (this.attackTarget != null){
            this.spewX = this.attackTarget.getX();
            this.spewY = this.attackTarget.getY() + this.attackTarget.getEyeHeight();
            this.spewZ = this.attackTarget.getZ();
        }
        this.durationLeft = this.maxDuration;
        this.attacker.setBreathing(true);
    }

    @Override
    public boolean canContinueToUse() {
        return this.durationLeft > 0 && this.attacker.isAlive() && !this.attacker.isDeadOrDying() && this.attackTarget.isAlive()
                && this.attacker.distanceTo(attackTarget) <= this.spewingRange
                && this.attacker.getSensing().canSee(attackTarget);
    }

    @Override
    public void tick() {
        if (this.durationLeft > 0) {
            --this.durationLeft;
        }

        if (this.attackTarget != null) {
            Vector3d vector3d = new Vector3d(this.attackTarget.getX() - this.spewX, (this.attackTarget.getY() + this.attackTarget.getEyeHeight()) - this.spewY, this.attackTarget.getZ() - this.spewZ);

            vector3d = vector3d.normalize();

            double speed = 0.25D;

            this.spewX += vector3d.x * speed;
            this.spewY += vector3d.y * speed;
            this.spewZ += vector3d.z * speed;
        }

        this.attacker.getLookControl().setLookAt(spewX, spewY, spewZ, 100.0F, 100.0F);
        this.rotateAttacker(spewX, spewY, spewZ, 100.0F, 100.0F);

        int duration = this.maxDuration - this.durationLeft;
        if (duration > 5) {
            for (Entity entity : MobUtil.getTargets(this.attacker, this.spewingRange)) {
                if (entity != null) {
                    this.attacker.doBreathing(entity);
                }
            }
        }
    }

    @Override
    public void stop() {
        this.durationLeft = 0;
        this.attackTarget = null;
        this.attacker.setBreathing(false);
    }

    public void rotateAttacker(double x, double y, double z, float pDeltaYaw, float pDeltaPitch) {
        double xOffset = x - this.attacker.getX();
        double zOffset = z - this.attacker.getZ();
        double yOffset = (this.attacker.getY() + 0.25) - y;

        double distance = MathHelper.sqrt(xOffset * xOffset + zOffset * zOffset);
        float xyAngle = (float) ((Math.atan2(zOffset, xOffset) * 180D) / Math.PI) - 90F;
        float zdAngle = (float) (-((Math.atan2(yOffset, distance) * 180D) / Math.PI));
        this.attacker.xRot = -this.updateRotation(this.attacker.xRot, zdAngle, pDeltaPitch);
        this.attacker.yRot = this.updateRotation(this.attacker.yRot, xyAngle, pDeltaYaw);

    }

    private float updateRotation(float origin, float target, float maxDelta) {
        float delta = MathHelper.wrapDegrees(target - origin);

        if (delta > maxDelta) {
            delta = maxDelta;
        }

        if (delta < -maxDelta) {
            delta = -maxDelta;
        }

        return origin + delta;
    }
}
