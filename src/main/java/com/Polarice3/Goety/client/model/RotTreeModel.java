package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.ally.RottreantEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class RotTreeModel extends SegmentedModel<RottreantEntity> {
    private final ModelRenderer rotTree;
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer rightArm;
    private final ModelRenderer leftArm;
    private final ModelRenderer upperBody;
    private final ModelRenderer lowerBody;
    private final ModelRenderer rightLeg;
    private final ModelRenderer leftLeg;
    private final ModelRenderer root;
    private final ModelRenderer root2;

    public RotTreeModel() {
        texWidth = 128;
        texHeight = 128;

        rotTree = new ModelRenderer(this);
        rotTree.setPos(0.0F, -7.0F, 0.0F);

        upperBody = new ModelRenderer(this);
        upperBody.setPos(0.0F, 17.0F, 0.0F);
        rotTree.addChild(upperBody);

        body = new ModelRenderer(this);
        body.setPos(0.0F, -14.0F, 0.0F);
        upperBody.addChild(body);
        body.texOffs(0, 50).addBox(-4.5F, 5.0F, -3.0F, 9.0F, 3.0F, 4.0F, 0.5F, false);
        body.texOffs(0, 58).addBox(-6.5F, 9.0F, -3.0F, 13.0F, 4.0F, 6.0F, 0.5F, false);

        ModelRenderer cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(4.0F, -7.39F, 6.2485F);
        body.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.4363F, 0.2618F, 0.0F);
        cube_r1.texOffs(79, 0).addBox(0.0F, -6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 0.0F, false);

        ModelRenderer cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(-4.0F, -7.39F, 6.2485F);
        body.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.4363F, -0.2618F, 0.0F);
        cube_r2.texOffs(79, 0).addBox(0.0F, -6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 0.0F, false);

        ModelRenderer body_r1 = new ModelRenderer(this);
        body_r1.setPos(0.0F, 0.0F, 0.0F);
        body.addChild(body_r1);
        setRotationAngle(body_r1, 0.2618F, 0.0F, 0.0F);
        body_r1.texOffs(0, 37).addBox(-5.5F, -1.0F, -5.0F, 11.0F, 6.0F, 6.0F, 0.5F, false);

        ModelRenderer body_r2 = new ModelRenderer(this);
        body_r2.setPos(0.0F, -2.0F, -0.5F);
        body.addChild(body_r2);
        setRotationAngle(body_r2, 0.4363F, 0.0F, 0.0F);
        body_r2.texOffs(0, 17).addBox(-9.0F, -7.0F, -8.5F, 18.0F, 8.0F, 11.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, -20.0F, -6.0F);
        upperBody.addChild(head);
        head.texOffs(0, 0).addBox(-4.0F, -8.0F, -5.5F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        head.texOffs(0, 81).addBox(-4.0F, -8.0F, -5.5F, 8.0F, 8.0F, 8.0F, 0.25F, false);

        ModelRenderer collar_r1 = new ModelRenderer(this);
        collar_r1.setPos(0.0F, 0.5F, -1.5F);
        head.addChild(collar_r1);
        setRotationAngle(collar_r1, 0.4363F, 0.0F, 0.0F);
        collar_r1.texOffs(0, 68).addBox(-5.0F, -4.0F, -4.0F, 10.0F, 3.0F, 10.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(4.0F, -9.0F, 2.0F);
        head.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.0F, 0.2618F, 0.0F);
        cube_r3.texOffs(104, 0).addBox(1.0F, -6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 0.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-4.0F, -9.0F, 2.0F);
        head.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, -0.2618F, 0.0F);
        cube_r4.texOffs(104, 0).addBox(-1.0F, -6.0F, -6.0F, 0.0F, 12.0F, 12.0F, 0.0F, false);

        rightArm = new ModelRenderer(this);
        rightArm.setPos(-8.0F, -19.0F, -4.0F);
        upperBody.addChild(rightArm);
        rightArm.texOffs(59, 24).addBox(-5.0F, 3.5F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
        rightArm.texOffs(53, 41).addBox(-6.0F, -4.5F, -3.0F, 6.0F, 8.0F, 6.0F, 0.0F, true);
        rightArm.texOffs(78, 41).addBox(-6.0F, -4.5F, -3.0F, 6.0F, 8.0F, 6.0F, 0.5F, true);
        rightArm.texOffs(53, 57).addBox(-7.0F, 14.5F, -4.0F, 8.0F, 12.0F, 8.0F, 0.0F, true);
        rightArm.texOffs(85, 57).addBox(-7.0F, 14.5F, -4.0F, 8.0F, 12.0F, 8.0F, 0.5F, true);

        leftArm = new ModelRenderer(this);
        leftArm.setPos(8.0F, -19.0F, -4.0F);
        upperBody.addChild(leftArm);
        leftArm.texOffs(59, 24).addBox(1.0F, 3.5F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        leftArm.texOffs(53, 41).addBox(1.0F, -4.5F, -3.0F, 6.0F, 8.0F, 6.0F, 0.0F, false);
        leftArm.texOffs(53, 57).addBox(-1.0F, 14.5F, -4.0F, 8.0F, 12.0F, 8.0F, 0.0F, false);
        leftArm.texOffs(78, 41).addBox(1.0F, -4.5F, -3.0F, 6.0F, 8.0F, 6.0F, 0.5F, false);
        leftArm.texOffs(85, 57).addBox(-1.0F, 14.5F, -4.0F, 8.0F, 12.0F, 8.0F, 0.5F, false);

        lowerBody = new ModelRenderer(this);
        lowerBody.setPos(0.0F, 0.0F, 0.0F);
        rotTree.addChild(lowerBody);


        rightLeg = new ModelRenderer(this);
        rightLeg.setPos(-4.0F, 16.0F, 0.0F);
        lowerBody.addChild(rightLeg);
        rightLeg.texOffs(37, 0).addBox(-2.5F, -2.0F, -1.0F, 4.0F, 8.0F, 3.0F, 0.0F, false);
        rightLeg.texOffs(52, 0).addBox(-3.0F, 5.0F, -2.0F, 5.0F, 10.0F, 5.0F, 0.5F, true);
        rightLeg.texOffs(108, 41).addBox(-3.0F, 5.0F, -2.0F, 5.0F, 10.0F, 5.0F, 1.0F, true);

        root = new ModelRenderer(this);
        root.setPos(0.0F, 2.0F, 0.0F);
        rightLeg.addChild(root);
        root.texOffs(100, 0).addBox(-4.0F, 13.0F, -3.0F, 7.0F, 1.0F, 7.0F, 0.5F, true);

        leftLeg = new ModelRenderer(this);
        leftLeg.setPos(4.0F, 16.0F, 0.0F);
        lowerBody.addChild(leftLeg);
        leftLeg.texOffs(37, 0).addBox(-1.5F, -2.0F, -1.0F, 4.0F, 8.0F, 3.0F, 0.0F, true);
        leftLeg.texOffs(52, 0).addBox(-2.0F, 5.0F, -2.0F, 5.0F, 10.0F, 5.0F, 0.5F, false);
        leftLeg.texOffs(108, 41).addBox(-2.0F, 5.0F, -2.0F, 5.0F, 10.0F, 5.0F, 1.0F, false);

        root2 = new ModelRenderer(this);
        root2.setPos(2.0F, 2.0F, 0.0F);
        leftLeg.addChild(root2);
        root2.texOffs(100, 0).addBox(-5.0F, 13.0F, -3.0F, 7.0F, 1.0F, 7.0F, 0.5F, true);
    }

    @Override
    public void setupAnim(RottreantEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch){
        boolean flag = pEntity.getFallFlyingTicks() > 4;
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
        this.body.yRot = 0.0F;
        this.rightLeg.xRot = -1.5F * MathHelper.triangleWave(pLimbSwing, 13.0F) * pLimbSwingAmount;
        this.leftLeg.xRot = 1.5F * MathHelper.triangleWave(pLimbSwing, 13.0F) * pLimbSwingAmount;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;
        float f = 1.0F;
        if (flag) {
            f = (float)pEntity.getDeltaMovement().lengthSqr();
            f = f / 0.2F;
            f = f * f * f;
        }

        if (f < 1.0F) {
            f = 1.0F;
        }
        boolean isTree = pEntity.isStaying();
        if (isTree){
            this.rightArm.xRot = 0.0F;
            this.leftArm.xRot = 0.0F;
            this.rightArm.zRot = 2.3561945F;
            this.leftArm.zRot = -2.3561945F;
        } else {
            this.rightArm.xRot = MathHelper.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 2.0F * pLimbSwingAmount * 0.5F / f;
            this.leftArm.xRot = MathHelper.cos(pLimbSwing * 0.6662F) * 2.0F * pLimbSwingAmount * 0.5F / f;
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.setupAttackAnimation(pEntity);
            ModelHelper.bobArms(this.rightArm, this.leftArm, pAgeInTicks);
        }
        this.upperBody.xRot = 0.0F;
        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;
        this.root.visible = isTree;
        this.root2.visible = isTree;
    }

    protected HandSide getAttackArm(RottreantEntity pEntity) {
        HandSide handside = pEntity.getMainArm();
        return pEntity.swingingArm == Hand.MAIN_HAND ? handside : handside.getOpposite();
    }

    protected ModelRenderer getArm(HandSide pSide) {
        return pSide == HandSide.LEFT ? this.leftArm : this.rightArm;
    }

    protected void setupAttackAnimation(RottreantEntity pEntity) {
        if (!(this.attackTime <= 0.0F)) {
            HandSide handside = this.getAttackArm(pEntity);
            ModelRenderer modelrenderer = this.getArm(handside);
            float f = this.attackTime;
            this.body.yRot = MathHelper.sin(MathHelper.sqrt(f) * ((float)Math.PI * 2F)) * 0.2F;
            if (handside == HandSide.LEFT) {
                this.body.yRot *= -1.0F;
            }

            this.rightArm.x = -MathHelper.cos(this.body.yRot) * 8.0F;
            this.leftArm.x = MathHelper.cos(this.body.yRot) * 8.0F;
            this.rightArm.yRot += this.body.yRot;
            this.leftArm.yRot += this.body.yRot;
            this.leftArm.xRot += this.body.yRot;
            f = 1.0F - this.attackTime;
            f = f * f;
            f = f * f;
            f = 1.0F - f;
            float f1 = MathHelper.sin(f * (float)Math.PI);
            float f2 = MathHelper.sin(this.attackTime * (float)Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
            modelrenderer.xRot = (float)((double)modelrenderer.xRot - ((double)f1 * 1.2D + (double)f2));
            modelrenderer.yRot += this.body.yRot * 2.0F;
            modelrenderer.zRot += MathHelper.sin(this.attackTime * (float)Math.PI) * -0.4F;
        }
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.rotTree);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
