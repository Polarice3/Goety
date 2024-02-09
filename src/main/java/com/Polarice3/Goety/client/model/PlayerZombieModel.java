package com.Polarice3.Goety.client.model;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.entity.MobEntity;

public class PlayerZombieModel<T extends MobEntity> extends PlayerModel<T> {
    public PlayerZombieModel(float p_i46304_1_) {
        super(p_i46304_1_, false);
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        ModelHelper.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive(pEntity), this.attackTime, pAgeInTicks);
        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);
    }

    public boolean isAggressive(T entityIn) {
        return entityIn.isAggressive();
    }
}
