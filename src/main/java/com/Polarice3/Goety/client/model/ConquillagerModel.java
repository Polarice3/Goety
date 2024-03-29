package com.Polarice3.Goety.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class ConquillagerModel<T extends AbstractIllagerEntity> extends BipedModel<T> {
    public final ModelRenderer clothes;
    public final ModelRenderer cube_r1;
    public final ModelRenderer all;

    public ConquillagerModel(float modelSize, float p_i47227_2_) {
        super(modelSize);
        texWidth = 64;
        texHeight = 128;

        this.all = new ModelRenderer(this);
        this.all.setPos(0.0F, 0.0F, 0.0F);

        this.head = new ModelRenderer(this);
        this.head.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, modelSize);

        this.hat = new ModelRenderer(this);
        this.hat.setPos(0.0F, 0.0F, 0.0F);
        this.hat.texOffs(0, 63).addBox(-5.0F, -12.0F, -5.0F, 10.0F, 4.0F, 10.0F, 0.0F, false);

        this.cube_r1 = new ModelRenderer(this);
        this.cube_r1.setPos(3.0F, -12.0F, -6.0F);
        this.hat.addChild(this.cube_r1);
        setRotationAngle(this.cube_r1, 0.0F, 0.4363F, 0.0F);
        this.cube_r1.texOffs(31, 64).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, 0.0F, false);

        ModelRenderer nose = new ModelRenderer(this);
        nose.setPos(0.0F, p_i47227_2_ - 2.0F, 0.0F);
        nose.texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, modelSize);
        this.head.addChild(nose);
        this.all.addChild(this.head);

        this.body = new ModelRenderer(this);
        this.body.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.body.texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, modelSize);
        this.body.texOffs(2, 78).addBox(-5.0F, -0.5F, -5.0F, 10.0F, 2.0F, 10.0F, 0.0F, false);
        this.clothes = new ModelRenderer(this);
        this.clothes.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.clothes.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, modelSize + 0.5F);
        this.all.addChild(this.body);
        this.all.addChild(this.clothes);

        this.rightLeg = new ModelRenderer(this, 0, 22);
        this.rightLeg.setPos(-2.0F, 12.0F + p_i47227_2_, 0.0F);
        this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.all.addChild(this.rightLeg);

        this.leftLeg = new ModelRenderer(this, 0, 22);
        this.leftLeg.mirror = true;
        this.leftLeg.setPos(2.0F, 12.0F + p_i47227_2_, 0.0F);
        this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.all.addChild(this.leftLeg);

        this.rightArm = new ModelRenderer(this, 40, 46);
        this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.rightArm.texOffs(32, 0).addBox(-4.0F, -4.0F, -3.0F, 5.0F, 5.0F, 6.0F, 0.0F, false);
        this.rightArm.setPos(-5.0F, 2.0F + p_i47227_2_, 0.0F);

        this.leftArm = new ModelRenderer(this, 40, 46);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.leftArm.texOffs(32, 0).addBox(-1.0F, -4.0F, -3.0F, 5.0F, 5.0F, 6.0F, 0.0F, false);
        this.leftArm.setPos(5.0F, 2.0F + p_i47227_2_, 0.0F);
    }

    @Override
    protected Iterable<ModelRenderer> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.clothes, this.all));
    }

    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.hat.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.hat.xRot = headPitch * ((float)Math.PI / 180F);
        if (this.riding) {
            this.rightArm.xRot = (-(float)Math.PI / 5F);
            this.rightArm.yRot = 0.0F;
            this.rightArm.zRot = 0.0F;
            this.leftArm.xRot = (-(float)Math.PI / 5F);
            this.leftArm.yRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = ((float)Math.PI / 10F);
            this.rightLeg.zRot = 0.07853982F;
            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = (-(float)Math.PI / 10F);
            this.leftLeg.zRot = -0.07853982F;
        } else {
            this.rightArm.y = 2.0F;
            this.leftArm.y = 2.0F;
            this.rightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            this.rightArm.yRot = 0.0F;
            this.rightArm.zRot = 0.0F;
            this.leftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
            this.leftArm.yRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightLeg.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
            this.rightLeg.yRot = 0.0F;
            this.rightLeg.zRot = 0.0F;
            this.leftLeg.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
            this.leftLeg.yRot = 0.0F;
            this.leftLeg.zRot = 0.0F;
        }

        AbstractIllagerEntity.ArmPose abstractprotectorentity$armpose = entityIn.getArmPose();
        switch (abstractprotectorentity$armpose){
            case CROSSED:
                this.rightArm.xRot = 0;
                this.leftArm.xRot = 0;
                break;
            case ATTACKING:
                if (!entityIn.getMainHandItem().isEmpty() && !(entityIn.getMainHandItem().getItem() instanceof ShootableItem)) {
                    ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, entityIn, this.attackTime, ageInTicks);
                }
                break;
            case CROSSBOW_CHARGE:
                ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, entityIn, true);
                break;
            case CROSSBOW_HOLD:
                ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
                break;
            case SPELLCASTING:
                this.rightArm.z = 0.0F;
                this.rightArm.x = -5.0F;
                this.leftArm.z = 0.0F;
                this.leftArm.x = 5.0F;
                this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
                this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
                this.rightArm.zRot = 2.3561945F;
                this.leftArm.zRot = -2.3561945F;
                this.rightArm.yRot = 0.0F;
                this.leftArm.yRot = 0.0F;
        }
        boolean flag2 = entityIn.getItemBySlot(EquipmentSlotType.CHEST).getItem() instanceof ArmorItem
                || entityIn.getItemBySlot(EquipmentSlotType.LEGS).getItem() instanceof ArmorItem;
        this.clothes.visible = !flag2;
        boolean flag3 = entityIn.getItemBySlot(EquipmentSlotType.HEAD).getItem() instanceof ArmorItem;
        this.hat.visible = !flag3;
    }

    public boolean isAggressive(T entityIn) {
        return entityIn.isAggressive();
    }

    public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;
        if (entityIn.getMainArm() == HandSide.RIGHT) {
            this.RightArmPoses(Hand.MAIN_HAND, entityIn);
            this.LeftArmPoses(Hand.OFF_HAND, entityIn);
        } else {
            this.RightArmPoses(Hand.OFF_HAND, entityIn);
            this.LeftArmPoses(Hand.MAIN_HAND, entityIn);
        }
        super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
    }

    private void RightArmPoses (Hand hand, T entityIn){
        ItemStack itemstack = entityIn.getItemInHand(hand);
        UseAction useAction = itemstack.getUseAnimation();
        if (useAction == UseAction.CROSSBOW) {
            this.rightArmPose = ArmPose.CROSSBOW_HOLD;
            if (entityIn.isUsingItem()) {
                this.rightArmPose = ArmPose.CROSSBOW_CHARGE;
            }
        } else {
            this.rightArmPose = ArmPose.EMPTY;
            if (!itemstack.isEmpty()) {
                this.rightArmPose = ArmPose.ITEM;
            }
        }
    }

    private void LeftArmPoses (Hand hand, T entityIn){
        ItemStack itemstack = entityIn.getItemInHand(hand);
        UseAction useAction = itemstack.getUseAnimation();
        if (useAction == UseAction.CROSSBOW) {
            this.leftArmPose = ArmPose.CROSSBOW_HOLD;
            if (entityIn.isUsingItem()) {
                this.leftArmPose = ArmPose.CROSSBOW_CHARGE;
            }
        } else {
            this.leftArmPose = ArmPose.EMPTY;
            if (!itemstack.isEmpty()) {
                this.leftArmPose = ArmPose.ITEM;
            }
        }
    }

    private ModelRenderer getthisArm(HandSide p_191216_1_) {
        return p_191216_1_ == HandSide.LEFT ? this.leftArm : this.rightArm;
    }

    public ModelRenderer getHead() {
        return this.head;
    }

    public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
        this.getthisArm(sideIn).translateAndRotate(matrixStackIn);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

}
