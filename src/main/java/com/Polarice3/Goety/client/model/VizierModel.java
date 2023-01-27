package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class VizierModel extends SegmentedModel<VizierEntity> implements IHasArm, IHasHead {
    private final ModelRenderer body;
    private final ModelRenderer cape;
    private final ModelRenderer head;
    private final ModelRenderer nose;
    private final ModelRenderer Hat;
    private final ModelRenderer mustache;
    private final ModelRenderer arms;
    private final ModelRenderer leg0;
    private final ModelRenderer leg1;
    private final ModelRenderer rightArm;
    private final ModelRenderer leftArm;

    public VizierModel(float scaleFactor, float p_i47227_2_) {
        texWidth = 64;
        texHeight = 128;

        body = new ModelRenderer(this);
        body.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        body.texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, scaleFactor, false);
        body.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 20.0F, 6.0F, scaleFactor + 0.5F, false);

        cape = new ModelRenderer(this);
        cape.setPos(0.0F, 0.0F + p_i47227_2_, 3.5F);
        cape.texOffs(44, 62).addBox(-4.0F, 0.0F, -0.5F, 8.0F, 20.0F, 1.0F, scaleFactor + 0.5F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        body.addChild(head);
        head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, scaleFactor, false);
        head.texOffs(0, 81).addBox(-5.0F, -3.0F, -4.0F, 10.0F, 4.0F, 9.0F, scaleFactor, false);

        nose = new ModelRenderer(this);
        nose.setPos(0.0F, -2.0F + p_i47227_2_, 0.0F);
        head.addChild(nose);
        nose.texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, scaleFactor, false);

        Hat = new ModelRenderer(this);
        Hat.setPos(0.0F, 0.0F, 0.0F);
        head.addChild(Hat);
        Hat.texOffs(0, 64).addBox(-5.0F, -13.0F, -5.0F, 10.0F, 7.0F, 10.0F, scaleFactor, false);

        mustache = new ModelRenderer(this);
        mustache.setPos(0.0F, 0.0F, 0.0F);
        head.addChild(mustache);
        mustache.texOffs(35, 0).addBox(-6.0F, -5.0F, -4.0F, 12.0F, 6.0F, 0.0F, 0.0F, false);

        arms = new ModelRenderer(this);
        arms.setPos(0.0F, 2.0F + p_i47227_2_, 0.0F);
        body.addChild(arms);
        arms.texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, scaleFactor, false);
        arms.texOffs(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, scaleFactor, false);
        arms.texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, scaleFactor, false);

        leg0 = new ModelRenderer(this);
        leg0.setPos(-2.0F, 12.0F + p_i47227_2_, 0.0F);
        body.addChild(leg0);
        leg0.texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scaleFactor, false);

        leg1 = new ModelRenderer(this);
        leg1.setPos(2.0F, 12.0F + p_i47227_2_, 0.0F);
        body.addChild(leg1);
        leg1.texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scaleFactor, false);

        rightArm = new ModelRenderer(this);
        rightArm.setPos(-5.0F, 2.0F + p_i47227_2_, 0.0F);
        body.addChild(rightArm);
        rightArm.texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scaleFactor, false);

        leftArm = new ModelRenderer(this);
        leftArm.setPos(5.0F, 2.0F + p_i47227_2_, 0.0F);
        body.addChild(leftArm);
        leftArm.texOffs(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, scaleFactor, false);
    }

    @Override
    public void setupAnim(VizierEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.arms.y = 3.0F;
        this.arms.z = -1.0F;
        this.arms.xRot = -0.75F;
        if (this.riding) {
            this.rightArm.xRot = (-(float)Math.PI / 5F);
            this.rightArm.yRot = 0.0F;
            this.rightArm.zRot = 0.0F;
            this.leftArm.xRot = (-(float)Math.PI / 5F);
            this.leftArm.yRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.leg0.xRot = -1.4137167F;
            this.leg0.yRot = ((float)Math.PI / 10F);
            this.leg0.zRot = 0.07853982F;
            this.leg1.xRot = -1.4137167F;
            this.leg1.yRot = (-(float)Math.PI / 10F);
            this.leg1.zRot = -0.07853982F;
        } else {
            this.rightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            this.rightArm.yRot = 0.0F;
            this.rightArm.zRot = 0.0F;
            this.leftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
            this.leftArm.yRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.leg0.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
            this.leg0.yRot = 0.0F;
            this.leg0.zRot = 0.0F;
            this.leg1.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
            this.leg1.yRot = 0.0F;
            this.leg1.zRot = 0.0F;
        }

        if (entityIn.isCharging()) {
            if (entityIn.getMainHandItem().isEmpty()) {
                this.rightArm.xRot = ((float)Math.PI * 1.5F);
                this.leftArm.xRot = ((float)Math.PI * 1.5F);
            } else if (entityIn.getMainArm() == HandSide.RIGHT) {
                this.rightArm.xRot = 3.7699115F;
            } else {
                this.leftArm.xRot = 3.7699115F;
            }
        }

        AbstractIllagerEntity.ArmPose abstractillagerentity$armpose = entityIn.getArmPose();
        if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.SPELLCASTING) {
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
        } else if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
            this.rightArm.z = 0.0F;
            this.rightArm.x = -5.0F;
            this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
            this.rightArm.zRot = 2.670354F;
            this.rightArm.yRot = 0.0F;
            this.leftArm.z = 0.0F;
            this.leftArm.x = 5.0F;
            this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
            this.leftArm.zRot = -2.3561945F;
            this.leftArm.yRot = 0.0F;
        }

        if (entityIn.deathTime > 0 && entityIn.isDeadOrDying()){
            this.body.yRot += ageInTicks;
        } else {
            this.body.yRot = 0.0F;
        }

        boolean flag = abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CROSSED;
        this.arms.visible = flag;
        this.leftArm.visible = !flag;
        this.rightArm.visible = !flag;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void renderCape(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay) {
        this.cape.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
    }

    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.arms, this.leg0, this.leg1, this.leftArm, this.rightArm);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    private ModelRenderer getArm(HandSide p_191216_1_) {
        return p_191216_1_ == HandSide.LEFT ? this.leftArm : this.rightArm;
    }

    @Override
    public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
        this.getArm(sideIn).translateAndRotate(matrixStackIn);
    }

    @Override
    public ModelRenderer getHead() {
        return null;
    }

}
