package com.Polarice3.Goety.client.model;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.MathHelper;

public class ZPiglinModel<T extends MobEntity> extends PlayerModel<T> {
    public final ModelRenderer earRight;
    public final ModelRenderer earLeft;
    private final ModelRenderer bodyDefault;
    private final ModelRenderer headDefault;
    private final ModelRenderer leftArmDefault;
    private final ModelRenderer rightArmDefault;

    public ZPiglinModel(float p_i232336_1_, int p_i232336_2_, int p_i232336_3_) {
        super(p_i232336_1_, false);
        this.texWidth = p_i232336_2_;
        this.texHeight = p_i232336_3_;
        this.body = new ModelRenderer(this, 16, 16);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, p_i232336_1_);
        this.head = new ModelRenderer(this);
        this.head.texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F, p_i232336_1_);
        this.head.texOffs(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F, p_i232336_1_);
        this.head.texOffs(2, 4).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, p_i232336_1_);
        this.head.texOffs(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F, p_i232336_1_);
        this.earRight = new ModelRenderer(this);
        this.earRight.setPos(4.5F, -6.0F, 0.0F);
        this.earRight.texOffs(51, 6).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, p_i232336_1_);
        this.head.addChild(this.earRight);
        this.earLeft = new ModelRenderer(this);
        this.earLeft.setPos(-4.5F, -6.0F, 0.0F);
        this.earLeft.texOffs(39, 6).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F, p_i232336_1_);
        this.head.addChild(this.earLeft);
        this.hat = new ModelRenderer(this);
        this.bodyDefault = this.body.createShallowCopy();
        this.headDefault = this.head.createShallowCopy();
        this.leftArmDefault = this.leftArm.createShallowCopy();
        this.rightArmDefault = this.leftArm.createShallowCopy();
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.body.copyFrom(this.bodyDefault);
        this.head.copyFrom(this.headDefault);
        this.leftArm.copyFrom(this.leftArmDefault);
        this.rightArm.copyFrom(this.rightArmDefault);
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        float f = ((float)Math.PI / 6F);
        float f1 = pAgeInTicks * 0.1F + pLimbSwing * 0.5F;
        float f2 = 0.08F + pLimbSwingAmount * 0.4F;
        this.earRight.zRot = (-(float)Math.PI / 6F) - MathHelper.cos(f1 * 1.2F) * f2;
        this.earLeft.zRot = ((float)Math.PI / 6F) + MathHelper.cos(f1) * f2;
        ModelHelper.animateZombieArms(this.leftArm, this.rightArm, pEntity.isAggressive(), this.attackTime, pAgeInTicks);

        this.leftPants.copyFrom(this.leftLeg);
        this.rightPants.copyFrom(this.rightLeg);
        this.leftSleeve.copyFrom(this.leftArm);
        this.rightSleeve.copyFrom(this.rightArm);
        this.jacket.copyFrom(this.body);
        this.hat.copyFrom(this.head);
    }
}
