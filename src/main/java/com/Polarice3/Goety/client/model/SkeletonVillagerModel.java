package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.neutral.OwnedEntity;
import com.google.common.collect.ImmutableList;
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

public class SkeletonVillagerModel <T extends OwnedEntity> extends VillagerMinionModel<T>  {

    public SkeletonVillagerModel(float modelSize, float p_i47227_2_) {
        super(modelSize, p_i47227_2_);

        this.hat = new ModelRenderer(this);
        this.hat.texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.5F);
        this.hat.setPos(0.0F, 0.0F, 0.0F);
        this.hat.visible = false;

        this.body = new ModelRenderer(this);
        this.body.texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F).texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, 0.05F);
        this.body.setPos(0.0F, 0.0F, 0.0F);

        this.clothes = new ModelRenderer(this);
        this.clothes.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F).texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, 0.05F);
        this.clothes.setPos(0.0F, 0.0F, 0.0F);

        this.rightArm = new ModelRenderer(this);
        this.rightArm.texOffs(40, 46).addBox(-0.9F, -1.9F, -1.0F, 2.0F, 12.0F, 2.0F)
                .texOffs(48, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F).setPos(-5.0F, 2.0F, 0.0F);

        this.leftArm = new ModelRenderer(this);
        this.leftArm.mirror = true;
        this.leftArm.texOffs(40, 46).addBox(-1.1F, -1.9F, -1.0F, 2.0F, 12.0F, 2.0F)
                .texOffs(48, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F).setPos(5.0F, 2.0F, 0.0F);

        this.rightLeg = new ModelRenderer(this);
        this.rightLeg.texOffs(0, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F).setPos(-2.0F, 12.0F, 0.0F);
        this.leftLeg = new ModelRenderer(this);
        this.leftLeg.mirror = true;
        this.leftLeg.texOffs(0, 22).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F).setPos(2.0F, 12.0F, 0.0F);
    }

    protected Iterable<ModelRenderer> bodyParts() {
        return ImmutableList.of(this.body, this.clothes, this.rightArm, this.leftArm, this.rightLeg, this.leftLeg, this.hat);
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
    }

    public void translateToHand(HandSide pSide, MatrixStack pMatrixStack) {
        float f = pSide == HandSide.RIGHT ? 1.0F : -1.0F;
        ModelRenderer modelrenderer = this.getArm(pSide);
        modelrenderer.x += f;
        modelrenderer.translateAndRotate(pMatrixStack);
        modelrenderer.x -= f;
    }
}
