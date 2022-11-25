package com.Polarice3.Goety.common.entities.ai;

import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.BeldamEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;

import java.util.EnumSet;
import java.util.List;

public class PotionGroupGoal<T extends AbstractCultistEntity> extends Goal {
    private final T mob;
    private BeldamEntity beldam;
    private final double speedModifier;

    public PotionGroupGoal(T p_i47515_1_, double p_i47515_2_) {
        this.mob = p_i47515_1_;
        this.speedModifier = p_i47515_2_;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        List<BeldamEntity> beldams = this.mob.level.getEntitiesOfClass(BeldamEntity.class, this.mob.getBoundingBox().inflate(8.0F, 4.0F, 8.0F));
        for (BeldamEntity beldamEntity : beldams){
            if (beldamEntity.isAlive() && !beldamEntity.isDeadOrDying() && !beldamEntity.isInvisible()){
                this.beldam = beldamEntity;
            }
        }
        if (this.beldam != null){
            return this.mob.getActiveEffects().isEmpty() && this.mob.getTarget() != null;
        }
        return false;
    }

    public boolean canContinueToUse() {
        return this.mob.getActiveEffects().isEmpty()
                && this.mob.getTarget() != null
                && this.beldam != null
                && !this.beldam.isDeadOrDying();
    }

    public void tick() {
        LivingEntity livingentity = this.beldam;
        if (livingentity != null) {
            double d0 = this.mob.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean flag = this.mob.getSensing().canSee(livingentity);

            if (flag && d0 > MathHelper.square(3)) {
                this.mob.getNavigation().moveTo(livingentity, this.speedModifier);
            }
        }
    }
}
