package com.Polarice3.Goety.common.entities.ai;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class SpiderBreedGoal extends Goal {
    private static final EntityPredicate PARTNER_TARGETING = (new EntityPredicate()).range(8.0D).allowInvulnerable().allowSameTeam().allowUnseeable();
    protected final AnimalEntity animal;
    private final Class<? extends AnimalEntity> partnerClass;
    protected final World level;
    protected AnimalEntity partner;
    private int loveTime;
    private final double speedModifier;

    public SpiderBreedGoal(AnimalEntity p_i1619_1_, double p_i1619_2_) {
        this(p_i1619_1_, p_i1619_2_, p_i1619_1_.getClass());
    }

    public SpiderBreedGoal(AnimalEntity p_i47306_1_, double p_i47306_2_, Class<? extends AnimalEntity> p_i47306_4_) {
        this.animal = p_i47306_1_;
        this.level = p_i47306_1_.level;
        this.partnerClass = p_i47306_4_;
        this.speedModifier = p_i47306_2_;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public boolean canUse() {
        if (!this.animal.isInLove()) {
            return false;
        } else {
            this.partner = this.getFreePartner();
            return this.partner != null;
        }
    }

    public boolean canContinueToUse() {
        return this.partner.isAlive() && this.partner.isInLove() && this.loveTime < 60;
    }

    public void stop() {
        this.partner = null;
        this.loveTime = 0;
    }

    public void tick() {
        this.animal.getLookControl().setLookAt(this.partner, 10.0F, (float)this.animal.getMaxHeadXRot());
        this.animal.getNavigation().moveTo(this.partner, this.speedModifier);
        ++this.loveTime;
        if (this.animal.distanceToSqr(this.partner) < 9.0D) {
            if (this.loveTime >= 60){
                this.breed();
                if (!this.animal.hasEffect(Effects.MOVEMENT_SLOWDOWN)){
                    this.animal.hurt(DamageSource.mobAttack(this.partner), 20.0F);
                    this.animal.heal(20.0F);
                    this.partner.addEffect(new EffectInstance(Effects.WEAKNESS, 1800, 1));
                }
            }
        }

    }

    @Nullable
    private AnimalEntity getFreePartner() {
        List<AnimalEntity> list = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, this.animal, this.animal.getBoundingBox().inflate(8.0D));
        double d0 = Double.MAX_VALUE;
        AnimalEntity animalentity = null;

        for(AnimalEntity animalentity1 : list) {
            if (this.animal.canMate(animalentity1) && this.animal.distanceToSqr(animalentity1) < d0) {
                animalentity = animalentity1;
                d0 = this.animal.distanceToSqr(animalentity1);
            }
        }

        return animalentity;
    }

    protected void breed() {
        this.animal.spawnChildFromBreeding((ServerWorld)this.level, this.partner);
    }
}
