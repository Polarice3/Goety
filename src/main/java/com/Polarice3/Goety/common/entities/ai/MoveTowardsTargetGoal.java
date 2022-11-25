package com.Polarice3.Goety.common.entities.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.List;

public class MoveTowardsTargetGoal<T extends CreatureEntity> extends Goal {
    private final T mob;
    private final EntityPredicate target = (new EntityPredicate().range(128.0D));


    public MoveTowardsTargetGoal(T mob){
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        List<PlayerEntity> list = this.mob.level.getNearbyEntities(PlayerEntity.class, this.target, this.mob, this.mob.getBoundingBox().inflate(128.0D, 32.0D, 128.0D));
        for (PlayerEntity player : list){
            this.mob.setTarget(player);
        }
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity != null) {
            return this.mob.distanceTo(livingEntity) > 32.0D;
        } else {
            return false;
        }
    }

    public void tick(){
        LivingEntity livingentity = this.mob.getTarget();
            if (!this.mob.isPathFinding() && livingentity != null) {
                Vector3d vector3d = livingentity.position();
                this.mob.getNavigation().moveTo(vector3d.x, vector3d.y, vector3d.z, 1.0D);
            }
        }
}
