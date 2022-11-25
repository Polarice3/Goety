package com.Polarice3.Goety.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class RobeModel<T extends LivingEntity> extends BipedModel<T> {
    private final ModelRenderer darkrobe;
    public final ModelRenderer hat;
    public final ModelRenderer Helmet_r1;
    public final ModelRenderer Helmet_r2;
    public final ModelRenderer Mask;
    public final ModelRenderer Body;
    public final ModelRenderer RightArm;
    public final ModelRenderer Pauldron;
    public final ModelRenderer LeftArm;
    public final ModelRenderer Pauldron2;
    public final ModelRenderer RightLeg;
    public final ModelRenderer LeftLeg;
    public final ModelRenderer RightFeet;
    public final ModelRenderer LeftFeet;
    public final ModelRenderer Pants;
    public final ModelRenderer cube_r1;
    public final ModelRenderer cube_r2;
    public final ModelRenderer cube_r3;
    public final ModelRenderer Belt;

    public RobeModel(float modelSize) {
        super(modelSize, 0, 64, 128);
        texWidth = 64;
        texHeight = 128;

        darkrobe = new ModelRenderer(this);
        darkrobe.setPos(0.0F, 0.0F, 0.0F);

        hat = new ModelRenderer(this);
        hat.setPos(0.0F, -4.0F, 0.0F);
        darkrobe.addChild(hat);
        hat.texOffs(0, 32).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

        Helmet_r1 = new ModelRenderer(this);
        Helmet_r1.setPos(0.0F, 0.8995F, 7.1213F);
        hat.addChild(Helmet_r1);
        setRotationAngle(Helmet_r1, -0.7854F, 0.0F, 0.0F);
        Helmet_r1.texOffs(40, 32).addBox(-2.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, 1.0F, false);

        Helmet_r2 = new ModelRenderer(this);
        Helmet_r2.setPos(0.0F, -0.2629F, 5.3561F);
        hat.addChild(Helmet_r2);
        setRotationAngle(Helmet_r2, -0.3927F, 0.0F, 0.0F);
        Helmet_r2.texOffs(24, 32).addBox(-3.0F, -2.5F, -1.0F, 6.0F, 5.0F, 2.0F, 1.0F, false);

        Mask = new ModelRenderer(this);
        Mask.setPos(0.0F, 0.0F, 0.0F);
        hat.addChild(Mask);
        Mask.texOffs(0, 76).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        Body = new ModelRenderer(this);
        Body.setPos(0.0F, 0.0F, 0.0F);
        darkrobe.addChild(Body);
        Body.texOffs(16, 48).addBox(-4.0F, 1.0F, -2.0F, 8.0F, 24.0F, 4.0F, 1.01F, false);
        Body.texOffs(0, 92).addBox(-6.0F, -1.0F, -4.0F, 12.0F, 8.0F, 8.0F, -0.75F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setPos(-1.5F, 5.0F, 0.0F);
        darkrobe.addChild(RightArm);
        RightArm.texOffs(40, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

        Pauldron = new ModelRenderer(this);
        Pauldron.setPos(-0.5F, -5.0F, 0.0F);
        RightArm.addChild(Pauldron);
        Pauldron.texOffs(44, 64).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 6.0F, 1.0F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setPos(1.5F, 5.0F, 0.0F);
        darkrobe.addChild(LeftArm);
        LeftArm.texOffs(40, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);

        Pauldron2 = new ModelRenderer(this);
        Pauldron2.setPos(0.5F, -5.0F, 0.0F);
        LeftArm.addChild(Pauldron2);
        Pauldron2.texOffs(36, 35).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 6.0F, 1.0F, false);

        RightLeg = new ModelRenderer(this);
        RightLeg.setPos(0.0F, 6.0F, 0.0F);
        darkrobe.addChild(RightLeg);
        RightLeg.texOffs(0, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        RightFeet = new ModelRenderer(this);
        RightFeet.setPos(0.0F, 10.0F, 0.0F);
        RightLeg.addChild(RightFeet);
        RightFeet.texOffs(0, 64).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 1.0F, false);

        LeftLeg = new ModelRenderer(this);
        LeftLeg.setPos(0.2F, 6.0F, 0.0F);
        darkrobe.addChild(LeftLeg);
        LeftLeg.texOffs(0, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

        LeftFeet = new ModelRenderer(this);
        LeftFeet.setPos(0.2F, 10.0F, 0.0F);
        LeftLeg.addChild(LeftFeet);
        LeftFeet.texOffs(0, 64).addBox(-2.2F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 1.0F, false);

        Pants = new ModelRenderer(this);
        Pants.setPos(0.0F, 26.0F, 0.0F);
        darkrobe.addChild(Pants);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-5.3126F, -11.4235F, 0.0F);
        Pants.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, 0.2618F);
        cube_r1.texOffs(50, 98).addBox(-0.5F, -3.0F, -3.0F, 1.0F, 8.0F, 6.0F, 0.0F, true);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(5.3126F, -11.4235F, 0.0F);
        Pants.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, -0.2618F);
        cube_r2.texOffs(50, 98).addBox(-0.5F, -3.0F, -3.0F, 1.0F, 8.0F, 6.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(0.0F, -11.1647F, 4.3467F);
        Pants.addChild(cube_r3);
        setRotationAngle(cube_r3, 0.2618F, 0.0F, 0.0F);
        cube_r3.texOffs(45, 74).addBox(-4.0F, -4.0F, -1.5F, 8.0F, 8.0F, 1.0F, 0.0F, true);

        Belt = new ModelRenderer(this);
        Belt.setPos(1.0F, -15.0F, 0.0F);
        Pants.addChild(Belt);
        Belt.texOffs(32, 88).addBox(-6.0F, -2.0F, -3.0F, 10.0F, 3.0F, 6.0F, 0.0F, false);

        this.head.addChild(hat);
        this.body.addChild(Body);
        this.body.addChild(Pants);
        this.leftArm.addChild(LeftArm);
        this.rightArm.addChild(RightArm);
        this.leftLeg.addChild(LeftLeg);
        this.rightLeg.addChild(RightLeg);
        this.leftLeg.addChild(LeftFeet);
        this.rightLeg.addChild(RightFeet);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.hat.copyFrom(this.head);
        this.Body.copyFrom(this.body);
        this.Pants.copyFrom(this.body);
        this.RightArm.copyFrom(this.rightArm);
        this.LeftArm.copyFrom(this.leftArm);
        this.RightLeg.copyFrom(this.rightLeg);
        this.LeftLeg.copyFrom(this.leftLeg);
        this.RightFeet.copyFrom(this.rightLeg);
        this.LeftFeet.copyFrom(this.leftLeg);
    }


    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        super.renderToBuffer(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
