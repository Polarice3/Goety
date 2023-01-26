package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.Polarice3.Goety.common.items.equipment.PitchforkItem;
import com.Polarice3.Goety.utils.ModMathHelper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class AbstractCultistModel<T extends AbstractCultistEntity> extends BipedModel<T> {
    public ModelRenderer clothes;
    public ModelRenderer arms;
    public ModelRenderer all;

    public AbstractCultistModel(float modelSize, float p_i47227_2_ ) {
        this(modelSize, p_i47227_2_, 64, 64);
    }

    public AbstractCultistModel(float modelSize, float p_i47227_2_ , int p_i51059_2_, int p_i51059_3_) {
        super(modelSize);

        this.all = new ModelRenderer(this).setTexSize(p_i51059_2_, p_i51059_3_);
        this.all.setPos(0.0F, 0.0F, 0.0F);

        this.head = new ModelRenderer(this).setTexSize(p_i51059_2_, p_i51059_3_);
        this.head.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, modelSize);

        this.hat = new ModelRenderer(this, 32, 0).setTexSize(p_i51059_2_, p_i51059_3_);
        this.hat.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, modelSize + 0.45F);
        this.hat.visible = true;

        ModelRenderer modelrenderer = new ModelRenderer(this).setTexSize(p_i51059_2_, p_i51059_3_);
        modelrenderer.setPos(0.0F, p_i47227_2_ - 2.0F, 0.0F);
        modelrenderer.texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, modelSize);
        this.head.addChild(modelrenderer);
        this.all.addChild(this.head);

        this.body = new ModelRenderer(this).setTexSize(p_i51059_2_, p_i51059_3_);
        this.body.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.body.texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, modelSize);
        this.clothes = new ModelRenderer(this).setTexSize(p_i51059_2_, p_i51059_3_);
        this.clothes.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.clothes.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, modelSize + 0.5F);
        this.all.addChild(this.body);
        this.all.addChild(this.clothes);

        this.arms = new ModelRenderer(this).setTexSize(p_i51059_2_, p_i51059_3_);
        this.arms.setPos(0.0F, 0.0F + p_i47227_2_ + 2.0F, 0.0F);
        this.arms.texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, modelSize);
        ModelRenderer modelrenderer1 = new ModelRenderer(this, 44, 22).setTexSize(p_i51059_2_, p_i51059_3_);
        modelrenderer1.mirror = true;
        modelrenderer1.addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, modelSize);
        this.arms.addChild(modelrenderer1);
        this.arms.texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, modelSize);

        this.rightLeg = new ModelRenderer(this, 0, 22).setTexSize(p_i51059_2_, p_i51059_3_);
        this.rightLeg.setPos(-2.0F, 12.0F + p_i47227_2_, 0.0F);
        this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.all.addChild(this.rightLeg);

        this.leftLeg = new ModelRenderer(this, 0, 22).setTexSize(p_i51059_2_, p_i51059_3_);
        this.leftLeg.mirror = true;
        this.leftLeg.setPos(2.0F, 12.0F + p_i47227_2_, 0.0F);
        this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.all.addChild(this.leftLeg);

        this.rightArm = new ModelRenderer(this, 40, 46).setTexSize(p_i51059_2_, p_i51059_3_);
        this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.rightArm.setPos(-5.0F, 2.0F + p_i47227_2_, 0.0F);

        this.leftArm = new ModelRenderer(this, 40, 46).setTexSize(p_i51059_2_, p_i51059_3_);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.leftArm.setPos(5.0F, 2.0F + p_i47227_2_, 0.0F);
    }

    @Override
    protected Iterable<ModelRenderer> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.arms, this.clothes, this.all));
    }

    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.hat.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.hat.xRot = headPitch * ((float)Math.PI / 180F);
        this.arms.z = -1.0F;
        this.arms.xRot = -0.75F;
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
            this.arms.y = 3.0F;
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

        AbstractCultistEntity.ArmPose armPose = entityIn.getArmPose();
        switch (armPose){
            case CROSSED:
                this.rightArm.xRot = 0;
                this.leftArm.xRot = 0;
                break;
            case ATTACKING:
                if (!entityIn.getMainHandItem().isEmpty() && !(entityIn.getMainHandItem().getItem() instanceof ShootableItem)) {
                    ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, entityIn, this.attackTime, ageInTicks);
                }
                break;
            case BOMB_AND_WEAPON:
                if (!entityIn.getMainHandItem().isEmpty() && !(entityIn.getMainHandItem().getItem() instanceof ShootableItem)) {
                    if (entityIn.getMainArm() == HandSide.RIGHT) {
                        ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, entityIn, this.attackTime, ageInTicks);
                    } else {
                        ModelHelper.swingWeaponDown(this.leftArm, this.rightArm, entityIn, this.attackTime, ageInTicks);
                    }
                }
                if (entityIn.getMainArm() == HandSide.RIGHT){
                    this.leftArm.z = 0.0F;
                    this.leftArm.x = 5.0F;
                    this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
                    this.leftArm.zRot = -2.3561945F;
                    this.leftArm.yRot = 0.0F;
                } else {
                    this.rightArm.z = 0.0F;
                    this.rightArm.x = -5.0F;
                    this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
                    this.rightArm.zRot = 2.3561945F;
                    this.rightArm.yRot = 0.0F;
                }
                break;
            case BOW_AND_ARROW:
                this.rightArm.yRot = -0.1F + this.head.yRot;
                this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
                this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
                this.leftArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
                break;
            case THROW_SPEAR:
                if (entityIn.getMainArm() == HandSide.RIGHT) {
                    this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float)Math.PI;
                    this.rightArm.yRot = 0.0F;
                } else {
                    this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float)Math.PI;
                    this.leftArm.yRot = 0.0F;
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
                break;
            case PRAYING:
                this.rightArm.xRot = ModMathHelper.modelDegrees(125);
                this.leftArm.xRot = ModMathHelper.modelDegrees(125);
                this.head.xRot = ModMathHelper.modelDegrees(30);
                break;
            case SPELL_AND_WEAPON:
                if (!entityIn.getMainHandItem().isEmpty()) {
                    if (entityIn.getMainArm() == HandSide.RIGHT) {
                        ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, entityIn, this.attackTime, ageInTicks);
                    } else {
                        ModelHelper.swingWeaponDown(this.leftArm, this.rightArm, entityIn, this.attackTime, ageInTicks);
                    }
                }
                if (entityIn.getMainArm() == HandSide.RIGHT){
                    this.leftArm.z = 0.0F;
                    this.leftArm.x = 5.0F;
                    this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
                    this.leftArm.zRot = -2.3561945F;
                    this.leftArm.yRot = 0.0F;
                } else {
                    this.rightArm.z = 0.0F;
                    this.rightArm.x = -5.0F;
                    this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
                    this.rightArm.zRot = 2.3561945F;
                    this.rightArm.yRot = 0.0F;
                }
                break;
            case TORCH_AND_WEAPON:
                if (!entityIn.getMainHandItem().isEmpty() && !(entityIn.getMainHandItem().getItem() instanceof ShootableItem)) {
                    if (entityIn.getMainArm() == HandSide.RIGHT) {
                        ModelHelper.swingWeaponDown(this.rightArm, this.leftArm, entityIn, this.attackTime, ageInTicks);
                    } else {
                        ModelHelper.swingWeaponDown(this.leftArm, this.rightArm, entityIn, this.attackTime, ageInTicks);
                    }
                }
                if (entityIn.getMainArm() == HandSide.RIGHT){
                    this.leftArm.xRot = -(2.0F + MathHelper.cos(ageInTicks * 0.09F) * 0.15F);
                } else {
                    this.rightArm.xRot = -(2.0F + MathHelper.cos(ageInTicks * 0.09F) * 0.15F);
                }
                break;
            case DYING:
                if (entityIn.isDeadOrDying()){
                    this.rightArm.z = 0.0F;
                    this.rightArm.x = -5.0F;
                    this.leftArm.z = 0.0F;
                    this.leftArm.x = 5.0F;
                    this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
                    this.leftArm.xRot = -MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
                    this.rightArm.zRot = 2.3561945F;
                    this.leftArm.zRot = -2.3561945F;
                    this.rightLeg.xRot = 0.0F;
                    this.rightLeg.yRot = 0.0F;
                    this.rightLeg.zRot = 0.0F;
                    this.leftLeg.xRot = 0.0F;
                    this.leftLeg.yRot = 0.0F;
                    this.leftLeg.zRot = 0.0F;
                    this.head.xRot = -ModMathHelper.modelDegrees(25.0F);
                }
        }

        if (this.leftArmPose == ArmPose.THROW_SPEAR) {
            this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float)Math.PI;
            this.leftArm.yRot = 0.0F;
        }

        if (this.rightArmPose == ArmPose.THROW_SPEAR) {
            this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float)Math.PI;
            this.rightArm.yRot = 0.0F;
        }

        boolean flag = armPose == AbstractCultistEntity.ArmPose.CROSSED;
        this.arms.visible = flag;
        this.leftArm.visible = !flag;
        this.rightArm.visible = !flag;
        boolean flag2 = entityIn.getItemBySlot(EquipmentSlotType.CHEST).getItem() instanceof ArmorItem
                || entityIn.getItemBySlot(EquipmentSlotType.LEGS).getItem() instanceof ArmorItem;
        this.clothes.visible = !flag2;
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
        ItemStack itemstack = entityIn.getItemInHand(Hand.MAIN_HAND);
        if (itemstack.getItem() == Items.BOW && entityIn.isAggressive()) {
            if (entityIn.getMainArm() == HandSide.RIGHT) {
                this.rightArmPose = ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = ArmPose.BOW_AND_ARROW;
            }
        }
        if (itemstack.getItem() instanceof PitchforkItem && entityIn.isAggressive()) {
            if (entityIn.getMainArm() == HandSide.RIGHT) {
                this.rightArmPose = ArmPose.THROW_SPEAR;
            } else {
                this.leftArmPose = ArmPose.THROW_SPEAR;
            }
        }
        super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
    }

    private void RightArmPoses (Hand hand, T entityIn){
        ItemStack itemstack = entityIn.getItemInHand(hand);
        UseAction useAction = itemstack.getUseAnimation();
        if (entityIn.getArmPose() != AbstractCultistEntity.ArmPose.CROSSED){
            switch (useAction){
                case CROSSBOW:
                    this.rightArmPose = ArmPose.CROSSBOW_HOLD;
                    if (entityIn.isUsingItem()) {
                        this.rightArmPose = ArmPose.CROSSBOW_CHARGE;
                    }
                    break;
                case BOW:
                    this.rightArmPose = ArmPose.BOW_AND_ARROW;
                    break;
                case SPEAR:
                    this.rightArmPose = ArmPose.THROW_SPEAR;
                    break;
                default:
                    this.rightArmPose = ArmPose.EMPTY;
                    if (!itemstack.isEmpty()) {
                        this.rightArmPose = ArmPose.ITEM;
                    }
                    break;
            }
        }
    }

    private void LeftArmPoses (Hand hand, T entityIn){
        ItemStack itemstack = entityIn.getItemInHand(hand);
        UseAction useAction = itemstack.getUseAnimation();
        if (entityIn.getArmPose() != AbstractCultistEntity.ArmPose.CROSSED){
            switch (useAction){
                case CROSSBOW:
                    this.leftArmPose = ArmPose.CROSSBOW_HOLD;
                    if (entityIn.isUsingItem()) {
                        this.leftArmPose = ArmPose.CROSSBOW_CHARGE;
                    }
                    break;
                case BOW:
                    this.leftArmPose = ArmPose.BOW_AND_ARROW;
                    break;
                case SPEAR:
                    this.leftArmPose = ArmPose.THROW_SPEAR;
                    break;
                default:
                    this.leftArmPose = ArmPose.EMPTY;
                    if (!itemstack.isEmpty()) {
                        this.leftArmPose = ArmPose.ITEM;
                    }
                    break;
            }
        }
    }

    private ModelRenderer getthisArm(HandSide p_191216_1_) {
        return p_191216_1_ == HandSide.LEFT ? this.leftArm : this.rightArm;
    }

    public ModelRenderer func_205062_a() {
        return this.hat;
    }

    public ModelRenderer getHead() {
        return this.head;
    }

    public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
        this.getthisArm(sideIn).translateAndRotate(matrixStackIn);
    }
}
