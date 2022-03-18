package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.hostile.cultists.ThugEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ThugModel extends SegmentedModel<ThugEntity>{
	private final ModelRenderer Jugger;
	private final ModelRenderer Body;
	private final ModelRenderer BodyChest_r1;
	private final ModelRenderer RLegParts;
	private final ModelRenderer RThigh;
	private final ModelRenderer RLeg;
	private final ModelRenderer LLegParts;
	private final ModelRenderer LThigh;
	private final ModelRenderer LLeg;
	private final ModelRenderer RArmParts;
	private final ModelRenderer RUpperArm;
	private final ModelRenderer RArm;
	private final ModelRenderer LArmParts;
	private final ModelRenderer LUpperArm;
	private final ModelRenderer LArm;
	private final ModelRenderer head;
	private final ModelRenderer nose;

	public ThugModel() {
		texWidth = 64;
		texHeight = 128;

		Jugger = new ModelRenderer(this);
		Jugger.setPos(0.0F, 24.0F, 0.0F);


		Body = new ModelRenderer(this);
		Body.setPos(0.0F, -22.1723F, 0.3765F);
		Jugger.addChild(Body);
		Body.texOffs(0, 39).addBox(-5.0F, -4.8277F, -1.3765F, 10.0F, 8.0F, 7.0F, 0.0F, false);
		Body.texOffs(22, 55).addBox(-6.0F, 3.1723F, -2.3765F, 12.0F, 5.0F, 9.0F, 0.0F, false);

		BodyChest_r1 = new ModelRenderer(this);
		BodyChest_r1.setPos(0.0F, -2.8447F, -0.7471F);
		Body.addChild(BodyChest_r1);
		setRotationAngle(BodyChest_r1, 0.2618F, 0.0F, 0.0F);
		BodyChest_r1.texOffs(0, 21).addBox(-6.0F, -7.0F, -2.5F, 12.0F, 8.0F, 10.0F, 0.0F, false);

		RLegParts = new ModelRenderer(this);
		RLegParts.setPos(-4.0F, -14.0F, 0.0F);
		Jugger.addChild(RLegParts);


		RThigh = new ModelRenderer(this);
		RThigh.setPos(1.0F, 4.0F, 1.5F);
		RLegParts.addChild(RThigh);
		RThigh.texOffs(25, 88).addBox(-4.0F, -4.0F, -2.5F, 6.0F, 6.0F, 7.0F, 0.0F, false);

		RLeg = new ModelRenderer(this);
		RLeg.setPos(1.0F, 11.0F, 1.5F);
		RLegParts.addChild(RLeg);
		RLeg.texOffs(0, 82).addBox(-4.0F, -5.0F, -1.5F, 6.0F, 8.0F, 6.0F, 0.0F, false);

		LLegParts = new ModelRenderer(this);
		LLegParts.setPos(4.0F, -14.0F, 0.0F);
		Jugger.addChild(LLegParts);


		LThigh = new ModelRenderer(this);
		LThigh.setPos(2.0F, 5.5F, 0.5F);
		LLegParts.addChild(LThigh);
		LThigh.texOffs(25, 88).addBox(-5.0F, -5.5F, -1.5F, 6.0F, 6.0F, 7.0F, 0.0F, true);

		LLeg = new ModelRenderer(this);
		LLeg.setPos(1.0F, 12.0937F, -2.9226F);
		LLegParts.addChild(LLeg);
		LLeg.texOffs(0, 82).addBox(-4.0F, -6.0937F, 2.9226F, 6.0F, 8.0F, 6.0F, 0.0F, true);

		RArmParts = new ModelRenderer(this);
		RArmParts.setPos(-6.0F, -30.0F, 0.0F);
		Jugger.addChild(RArmParts);


		RUpperArm = new ModelRenderer(this);
		RUpperArm.setPos(-1.0F, 6.0F, -0.5F);
		RArmParts.addChild(RUpperArm);
		RUpperArm.texOffs(27, 71).addBox(-5.0F, -8.0F, -1.5F, 6.0F, 10.0F, 7.0F, 0.0F, false);

		RArm = new ModelRenderer(this);
		RArm.setPos(-1.0F, 12.1805F, -2.1288F);
		RArmParts.addChild(RArm);
		setRotationAngle(RArm, -0.2618F, 0.0F, 0.0F);
		RArm.texOffs(0, 64).addBox(-5.0F, -6.0F, -1.5F, 6.0F, 10.0F, 7.0F, 0.0F, false);

		LArmParts = new ModelRenderer(this);
		LArmParts.setPos(6.0F, -30.0F, 0.0F);
		Jugger.addChild(LArmParts);


		LUpperArm = new ModelRenderer(this);
		LUpperArm.setPos(5.0F, 5.0F, -0.5F);
		LArmParts.addChild(LUpperArm);
		LUpperArm.texOffs(27, 71).addBox(-5.0F, -7.0F, -1.5F, 6.0F, 10.0F, 7.0F, 0.0F, true);

		LArm = new ModelRenderer(this);
		LArm.setPos(2.0F, 13.1805F, -1.1288F);
		LArmParts.addChild(LArm);
		setRotationAngle(LArm, -0.2618F, 0.0F, 0.0F);
		LArm.texOffs(0, 64).addBox(-2.0F, -7.0F, -2.5F, 6.0F, 10.0F, 7.0F, 0.0F, true);

		head = new ModelRenderer(this);
		head.setPos(0.0F, -30.0F, -4.0F);
		Jugger.addChild(head);
		head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 11.0F, 8.0F, 0.0F, false);
		head.texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 12.0F, 8.0F, 0.5F, false);

		nose = new ModelRenderer(this);
		nose.setPos(0.0F, -2.0F, 0.0F);
		head.addChild(nose);
		nose.texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		Jugger.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.RArmParts, this.LArmParts, this.RLegParts, this.LLegParts, this.Body, this.head);
	}

	@Override
	public void setupAnim(ThugEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
		this.RLegParts.xRot = MathHelper.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount * 0.5F;
		this.RLegParts.yRot = 0.0F;
		this.RLegParts.zRot = 0.0F;
		this.LLegParts.xRot = MathHelper.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount * 0.5F;
		this.LLegParts.yRot = 0.0F;
		this.LLegParts.zRot = 0.0F;
	}

	public void prepareMobModel(ThugEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
		int i = pEntity.getAttackTick();
		if (i > 0) {
			this.RArmParts.xRot = -2.0F + 1.5F * MathHelper.triangleWave((float)i - pPartialTick, 10.0F);
			this.LArmParts.xRot = -2.0F + 1.5F * MathHelper.triangleWave((float)i - pPartialTick, 10.0F);
		} else {
			this.RArmParts.xRot = (-0.2F + 1.5F * MathHelper.triangleWave(pLimbSwing, 13.0F)) * pLimbSwingAmount;
			this.LArmParts.xRot = (-0.2F - 1.5F * MathHelper.triangleWave(pLimbSwing, 13.0F)) * pLimbSwingAmount;
		}

	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}