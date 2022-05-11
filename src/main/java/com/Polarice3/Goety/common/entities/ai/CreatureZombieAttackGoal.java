package com.Polarice3.Goety.common.entities.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class CreatureZombieAttackGoal extends MeleeAttackGoal {
    private final CreatureEntity zombie;
    private int raiseArmTicks;

    public CreatureZombieAttackGoal(CreatureEntity zombieIn, double speedIn, boolean longMemoryIn) {
        super(zombieIn, speedIn, longMemoryIn);
        this.zombie = zombieIn;
    }

    public void start() {
        super.start();
        this.raiseArmTicks = 0;
    }

    public void stop() {
        super.stop();
        this.zombie.setAggressive(false);
    }

    public void tick() {
        super.tick();
        ++this.raiseArmTicks;
        this.zombie.setAggressive(this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2);

    }
}
