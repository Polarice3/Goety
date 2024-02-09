package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class SunkenSkeletonModel<T extends OwnedEntity> extends BipedModel<T> {

    public SunkenSkeletonModel(float p_i1148_1_) {
        super(p_i1148_1_);
        texWidth = 64;
        texHeight = 32;

        this.body = new ModelRenderer(this);
        this.body.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F)
                .texOffs(12, 29).addBox(-5.0F, 4.0F, -2.0F, 1.0F, 1.0F, 0.0F)
                .texOffs(12, 30).addBox(2.0F, 12.0F, -2.0F, 2.0F, 2.0F, 0.0F).setPos(0.0F, 0.0F, 0.0F);

        this.head = new ModelRenderer(this);
        this.head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F)
                .texOffs(32, 0).addBox(-1.0F, -16.0F, -4.0F, 9.0F, 10.0F, 0.0F)
                .texOffs(48, 0).addBox(-1.0F, -3.0F, 2.0F, 6.0F, 0.0F, 4.0F).setPos(0.0F, 0.0F, 0.0F);

        this.rightArm = new ModelRenderer(this);
        this.rightArm.texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F).setPos(-5.0F, 2.0F, 0.0F);

        this.leftArm = new ModelRenderer(this);
        this.leftArm.texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F).setPos(5.0F, 2.0F, 0.0F);
        this.leftArm.mirror = true;

        this.rightLeg = new ModelRenderer(this);
        this.rightLeg.texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F)
                .texOffs(24, 4).addBox(-2.0F, 4.0F, -2.0F, 2.0F, 0.0F, 4.0F).setPos(-2.0F, 12.0F, 0.0F);

        this.leftLeg = new ModelRenderer(this);
        this.leftLeg.texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F).setPos(2.0F, 12.0F, 0.0F);
        this.leftLeg.mirror = true;
    }

    public void prepareMobModel(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        this.rightArmPose = BipedModel.ArmPose.EMPTY;
        this.leftArmPose = BipedModel.ArmPose.EMPTY;
        ItemStack itemstack = pEntity.getItemInHand(Hand.MAIN_HAND);
        if (itemstack.getItem() == Items.BOW && pEntity.isAggressive()) {
            if (pEntity.getMainArm() == HandSide.RIGHT) {
                this.rightArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = BipedModel.ArmPose.BOW_AND_ARROW;
            }
        }
        if (pEntity.isAggressive()) {
            if (itemstack.getItem() == Items.CROSSBOW) {
                if (pEntity.getMainArm() == HandSide.RIGHT) {
                    this.rightArmPose = ArmPose.CROSSBOW_HOLD;
                    if (pEntity.isChargingCrossbow()) {
                        this.rightArmPose = ArmPose.CROSSBOW_CHARGE;
                    }
                } else {
                    this.leftArmPose = ArmPose.CROSSBOW_HOLD;
                    if (pEntity.isChargingCrossbow()) {
                        this.leftArmPose = ArmPose.CROSSBOW_CHARGE;
                    }
                }
            }
        }

        super.prepareMobModel(pEntity, pLimbSwing, pLimbSwingAmount, pPartialTick);
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        ItemStack itemstack = pEntity.getMainHandItem();
        if (pEntity.isAggressive() && (!(itemstack.getItem() instanceof ShootableItem))) {
            float f = MathHelper.sin(this.attackTime * (float)Math.PI);
            float f1 = MathHelper.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.yRot = -(0.1F - f * 0.6F);
            this.leftArm.yRot = 0.1F - f * 0.6F;
            this.rightArm.xRot = (-(float)Math.PI / 2F);
            this.leftArm.xRot = (-(float)Math.PI / 2F);
            this.rightArm.xRot -= f * 1.2F - f1 * 0.4F;
            this.leftArm.xRot -= f * 1.2F - f1 * 0.4F;
            ModelHelper.bobArms(this.rightArm, this.leftArm, pAgeInTicks);
        }
        boolean flag2 = pEntity.getMainArm() == HandSide.RIGHT;
        boolean flag3 = flag2 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
        if (flag2 != flag3) {
            this.posingLeftArm(pEntity);
            this.posingRightArm(pEntity);
        } else {
            this.posingRightArm(pEntity);
            this.posingLeftArm(pEntity);
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
        this.hat.visible = false;
    }

    public void translateToHand(HandSide pSide, MatrixStack pMatrixStack) {
        float f = pSide == HandSide.RIGHT ? 1.0F : -1.0F;
        ModelRenderer modelrenderer = this.getArm(pSide);
        modelrenderer.x += f;
        modelrenderer.translateAndRotate(pMatrixStack);
        modelrenderer.x -= f;
    }

    public void posingRightArm(T p_241654_1_) {
        switch (this.rightArmPose) {
            case EMPTY:
                this.rightArm.yRot = 0.0F;
                break;
            case BLOCK:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F;
                this.rightArm.yRot = (-(float) Math.PI / 6F);
                break;
            case ITEM:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float) Math.PI / 10F);
                this.rightArm.yRot = 0.0F;
                break;
            case THROW_SPEAR:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) Math.PI;
                this.rightArm.yRot = 0.0F;
                break;
            case BOW_AND_ARROW:
                this.rightArm.yRot = -0.1F + this.head.yRot;
                this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
                this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
                this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
                break;
            case CROSSBOW_CHARGE:
                ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, p_241654_1_, true);
                break;
            case CROSSBOW_HOLD:
                ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
        }

    }

    public void posingLeftArm(T p_241655_1_) {
        switch (this.leftArmPose) {
            case EMPTY:
                this.leftArm.yRot = 0.0F;
                break;
            case BLOCK:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
                this.leftArm.yRot = ((float) Math.PI / 6F);
                break;
            case ITEM:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float) Math.PI / 10F);
                this.leftArm.yRot = 0.0F;
                break;
            case THROW_SPEAR:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) Math.PI;
                this.leftArm.yRot = 0.0F;
                break;
            case BOW_AND_ARROW:
                this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
                this.leftArm.yRot = 0.1F + this.head.yRot;
                this.rightArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
                this.leftArm.xRot = (-(float) Math.PI / 2F) + this.head.xRot;
                break;
            case CROSSBOW_CHARGE:
                ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, p_241655_1_, false);
                break;
            case CROSSBOW_HOLD:
                ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
        }

    }
}
