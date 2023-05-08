package com.Polarice3.Goety.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class MasterRobeModel<T extends LivingEntity> extends BipedModel<T> {
	public final ModelRenderer darkrobe;
	public final ModelRenderer Headwear;
	public final ModelRenderer Helmet_r1;
	public final ModelRenderer Helmet_r2;
	public final ModelRenderer Mask;
	public final ModelRenderer Crown;
	public final ModelRenderer Body;
	public final ModelRenderer Body_r1;
	public final ModelRenderer RightArm;
	public final ModelRenderer Pauldron;
	public final ModelRenderer Pauldron_r1;
	public final ModelRenderer LeftArm;
	public final ModelRenderer Pauldron2;
	public final ModelRenderer Pauldron3_r1;
	public final ModelRenderer RightLeg;
	public final ModelRenderer RightFeet;
	public final ModelRenderer LeftLeg;
	public final ModelRenderer LeftFeet;
	public final ModelRenderer Pants;
	public final ModelRenderer Belt;
	public final ModelRenderer bone;
	public final ModelRenderer cube_r1;
	public final ModelRenderer bone2;
	public final ModelRenderer cube_r2;
	public final ModelRenderer bone3;

	public MasterRobeModel(float modelSize) {
		super(modelSize, 0, 64, 128);
		texWidth = 64;
		texHeight = 128;

		darkrobe = new ModelRenderer(this);
		darkrobe.setPos(0.0F, 0.0F, 0.0F);

		Headwear = new ModelRenderer(this);
		Headwear.setPos(0.0F, -4.0F, 0.0F);
		darkrobe.addChild(Headwear);
		Headwear.texOffs(0, 32).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 1.0F, false);

		Helmet_r1 = new ModelRenderer(this);
		Helmet_r1.setPos(0.0F, 0.8995F, 7.1213F);
		Headwear.addChild(Helmet_r1);
		setRotationAngle(Helmet_r1, -0.7854F, 0.0F, 0.0F);
		Helmet_r1.texOffs(40, 32).addBox(-2.0F, -0.5F, -0.5F, 4.0F, 1.0F, 1.0F, 1.0F, false);

		Helmet_r2 = new ModelRenderer(this);
		Helmet_r2.setPos(0.0F, -0.2629F, 5.3561F);
		Headwear.addChild(Helmet_r2);
		setRotationAngle(Helmet_r2, -0.3927F, 0.0F, 0.0F);
		Helmet_r2.texOffs(24, 32).addBox(-3.0F, -2.5F, -1.0F, 6.0F, 5.0F, 2.0F, 1.0F, false);

		Mask = new ModelRenderer(this);
		Mask.setPos(0.0F, 0.0F, 0.0F);
		Headwear.addChild(Mask);
		Mask.texOffs(0, 76).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		Crown = new ModelRenderer(this);
		Crown.setPos(0.0F, 0.0F, 0.0F);
		Headwear.addChild(Crown);
		Crown.texOffs(0, 62).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 6.0F, 8.0F, 1.025F, false);

		Body = new ModelRenderer(this);
		Body.setPos(0.0F, 0.0F, 0.0F);
		darkrobe.addChild(Body);
		Body.texOffs(16, 48).addBox(-4.0F, 1.0F, -2.0F, 8.0F, 10.0F, 4.0F, 1.01F, false);
		Body.texOffs(0, 92).addBox(-5.0F, -1.0F, -4.0F, 10.0F, 6.0F, 8.0F, 0.0F, false);

		Body_r1 = new ModelRenderer(this);
		Body_r1.setPos(0.0F, 0.0F, 0.0F);
		Body.addChild(Body_r1);
		setRotationAngle(Body_r1, 0.2182F, 0.0F, 0.0F);
		Body_r1.texOffs(0, 107).addBox(-5.0F, -3.0F, -5.0F, 10.0F, 4.0F, 10.0F, 0.5F, false);

		RightArm = new ModelRenderer(this);
		RightArm.setPos(-1.5F, 5.0F, 0.0F);
		darkrobe.addChild(RightArm);
		RightArm.texOffs(40, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, false);

		Pauldron = new ModelRenderer(this);
		Pauldron.setPos(-0.5F, -5.0F, 0.0F);
		RightArm.addChild(Pauldron);
		Pauldron.texOffs(44, 64).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 6.0F, 1.0F, false);

		Pauldron_r1 = new ModelRenderer(this);
		Pauldron_r1.setPos(0.0F, -2.0F, 0.0F);
		Pauldron.addChild(Pauldron_r1);
		setRotationAngle(Pauldron_r1, 0.0F, 0.0F, -0.2618F);
		Pauldron_r1.texOffs(34, 106).addBox(-3.0F, -1.0F, -3.0F, 5.0F, 2.0F, 6.0F, 1.5F, false);

		LeftArm = new ModelRenderer(this);
		LeftArm.setPos(1.5F, 5.0F, 0.0F);
		darkrobe.addChild(LeftArm);
		LeftArm.texOffs(40, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.5F, true);

		Pauldron2 = new ModelRenderer(this);
		Pauldron2.setPos(0.5F, -5.0F, 0.0F);
		LeftArm.addChild(Pauldron2);
		Pauldron2.texOffs(36, 35).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 6.0F, 1.0F, false);

		Pauldron3_r1 = new ModelRenderer(this);
		Pauldron3_r1.setPos(0.5F, -1.0F, 0.0F);
		Pauldron2.addChild(Pauldron3_r1);
		setRotationAngle(Pauldron3_r1, 0.0F, 0.0F, 0.2618F);
		Pauldron3_r1.texOffs(34, 106).addBox(-2.5F, -2.0F, -3.0F, 5.0F, 2.0F, 6.0F, 1.5F, true);

		RightLeg = new ModelRenderer(this);
		RightLeg.setPos(0.0F, 6.0F, 0.0F);
		darkrobe.addChild(RightLeg);
		RightLeg.texOffs(0, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.75F, false);

		RightFeet = new ModelRenderer(this);
		RightFeet.setPos(0.0F, 10.0F, 0.0F);
		RightLeg.addChild(RightFeet);
		RightFeet.texOffs(0, 64).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 1.0F, false);

		LeftLeg = new ModelRenderer(this);
		LeftLeg.setPos(0.2F, 6.0F, 0.0F);
		darkrobe.addChild(LeftLeg);
		LeftLeg.texOffs(0, 48).addBox(-2.0F, -6.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.75F, true);

		LeftFeet = new ModelRenderer(this);
		LeftFeet.setPos(0.2F, 10.0F, 0.0F);
		LeftLeg.addChild(LeftFeet);
		LeftFeet.texOffs(0, 64).addBox(-2.2F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 1.0F, false);

		Pants = new ModelRenderer(this);
		Pants.setPos(0.0F, 26.0F, 0.0F);
		darkrobe.addChild(Pants);
		

		Belt = new ModelRenderer(this);
		Belt.setPos(1.0F, -15.0F, 0.0F);
		Pants.addChild(Belt);
		Belt.texOffs(32, 88).addBox(-6.0F, -2.0F, -3.0F, 10.0F, 3.0F, 6.0F, 0.5F, false);

		bone = new ModelRenderer(this);
		bone.setPos(-5.0F, -14.0F, 1.0F);
		Pants.addChild(bone);
		bone.texOffs(48, 118).addBox(-1.0F, 0.0F, -4.0F, 2.0F, 4.0F, 6.0F, 0.0F, false);

		cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(0.0F, 6.0F, -1.0F);
		bone.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, 0.2182F);
		cube_r1.texOffs(34, 116).addBox(-1.0F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);

		bone2 = new ModelRenderer(this);
		bone2.setPos(5.0F, -14.0F, 0.0F);
		Pants.addChild(bone2);
		bone2.texOffs(48, 118).addBox(-1.0F, 0.0F, -3.0F, 2.0F, 4.0F, 6.0F, 0.0F, false);

		cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(1.0F, 6.0F, 0.0F);
		bone2.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, -0.2182F);
		cube_r2.texOffs(34, 116).addBox(-1.0F, -3.0F, -3.0F, 1.0F, 6.0F, 6.0F, 0.0F, false);

		bone3 = new ModelRenderer(this);
		bone3.setPos(0.0F, -12.0F, 5.0F);
		Pants.addChild(bone3);
		setRotationAngle(bone3, 0.3491F, 0.0F, 0.0F);
		bone3.texOffs(45, 74).addBox(-4.0F, -3.0F, -2.0F, 8.0F, 8.0F, 1.0F, 0.0F, false);

		this.head.addChild(Headwear);
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
		this.Headwear.copyFrom(this.head);
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