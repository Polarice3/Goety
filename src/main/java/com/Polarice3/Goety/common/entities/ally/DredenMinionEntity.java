package com.Polarice3.Goety.common.entities.ally;

import com.Polarice3.Goety.common.entities.neutral.AbstractDredenEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class DredenMinionEntity extends AbstractDredenEntity {

    public DredenMinionEntity(EntityType<? extends AbstractDredenEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, new WanderGoal(this, 1.0D, 10));
    }

}
