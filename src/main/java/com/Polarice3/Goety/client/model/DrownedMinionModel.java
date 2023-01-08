package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.ally.ZombieMinionEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class DrownedMinionModel<T extends ZombieMinionEntity> extends ZombieMinionModel<T> {
    public DrownedMinionModel(float p_i48915_1_, float p_i48915_2_, int p_i48915_3_, int p_i48915_4_) {
        super(p_i48915_1_, p_i48915_2_, p_i48915_3_, p_i48915_4_);
        this.rightArm = new ModelRenderer(this, 32, 48);
        this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_i48915_1_);
        this.rightArm.setPos(-5.0F, 2.0F + p_i48915_2_, 0.0F);
        this.rightLeg = new ModelRenderer(this, 16, 48);
        this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, p_i48915_1_);
        this.rightLeg.setPos(-1.9F, 12.0F + p_i48915_2_, 0.0F);
    }

    public DrownedMinionModel(float p_i49398_1_, boolean p_i49398_2_) {
        super(p_i49398_1_, 0.0F, 64, p_i49398_2_ ? 32 : 64);
    }

    public void prepareMobModel(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        ItemStack itemstack = pEntity.getItemInHand(Hand.MAIN_HAND);
        if (itemstack.getItem() == Items.TRIDENT && pEntity.isAggressive()) {
            if (pEntity.getMainArm() == HandSide.RIGHT) {
                this.rightArmPose = BipedModel.ArmPose.THROW_SPEAR;
            } else {
                this.leftArmPose = BipedModel.ArmPose.THROW_SPEAR;
            }
        }

        super.prepareMobModel(pEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        if (this.leftArmPose == BipedModel.ArmPose.THROW_SPEAR) {
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float)Math.PI;
            this.leftArm.yRot = 0.0F;
        }

        if (this.rightArmPose == BipedModel.ArmPose.THROW_SPEAR) {
            this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float)Math.PI;
            this.rightArm.yRot = 0.0F;
        }

        if (this.swimAmount > 0.0F) {
            this.rightArm.xRot = this.rotlerpRad(this.swimAmount, this.rightArm.xRot, -2.5132742F) + this.swimAmount * 0.35F * MathHelper.sin(0.1F * pAgeInTicks);
            this.leftArm.xRot = this.rotlerpRad(this.swimAmount, this.leftArm.xRot, -2.5132742F) - this.swimAmount * 0.35F * MathHelper.sin(0.1F * pAgeInTicks);
            this.rightArm.zRot = this.rotlerpRad(this.swimAmount, this.rightArm.zRot, -0.15F);
            this.leftArm.zRot = this.rotlerpRad(this.swimAmount, this.leftArm.zRot, 0.15F);
            this.leftLeg.xRot -= this.swimAmount * 0.55F * MathHelper.sin(0.1F * pAgeInTicks);
            this.rightLeg.xRot += this.swimAmount * 0.55F * MathHelper.sin(0.1F * pAgeInTicks);
            this.head.xRot = 0.0F;
        }

    }
}
