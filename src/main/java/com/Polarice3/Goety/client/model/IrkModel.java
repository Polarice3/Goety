package com.Polarice3.Goety.client.model;


import com.Polarice3.Goety.common.entities.hostile.IrkEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class IrkModel extends SegmentedModel<IrkEntity> {
	private final ModelRenderer head;
	private final ModelRenderer body;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer leg0;
	private final ModelRenderer leftwing;
	private final ModelRenderer rightwing;

	public IrkModel() {
		texWidth = 64;
		texHeight = 64;

		head = new ModelRenderer(this);
		head.setPos(0.0F, 0.0F, -3.0F);
		head.texOffs(0, 0).addBox(-4.0F, -8.0F, -5.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		body = new ModelRenderer(this);
		body.setPos(0.0F, 0.0F, -2.0F);
		setRotationAngle(body, 0.2182F, 0.0F, 0.0F);
		body.texOffs(16, 16).addBox(-3.0F, -1.0F, -2.0F, 6.0F, 8.0F, 2.0F, 0.0F, false);

		RightArm = new ModelRenderer(this);
		RightArm.setPos(-4.0F, 1.0F, -4.0F);
		RightArm.texOffs(40, 16).addBox(-2.0F, -1.0F, -2.0F, 3.0F, 8.0F, 4.0F, 0.0F, false);

		LeftArm = new ModelRenderer(this);
		LeftArm.setPos(3.0F, 1.0F, -4.0F);
		LeftArm.texOffs(40, 16).addBox(0.0F, -1.0F, -2.0F, 3.0F, 8.0F, 4.0F, 0.0F, true);

		leg0 = new ModelRenderer(this);
		leg0.setPos(0.1F, 7.0F, -2.0F);
		leg0.texOffs(0, 16).addBox(-4.0F, -1.0F, 0.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		leg0.texOffs(33, 0).addBox(-3.0F, 0.0F, -1.0F, 6.0F, 6.0F, 3.0F, 0.0F, false);

		leftwing = new ModelRenderer(this);
		leftwing.setPos(0.0F, 0.0F, -2.0F);
		setRotationAngle(leftwing, 0.2618F, 0.0F, 0.0F);
		leftwing.texOffs(0, 32).addBox(0.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F, 0.0F, true);

		rightwing = new ModelRenderer(this);
		rightwing.setPos(0.0F, 0.0F, -2.0F);
		setRotationAngle(rightwing, 0.2618F, 0.0F, 0.0F);
		rightwing.texOffs(0, 32).addBox(-20.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(IrkEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		float f = 1.0F;
		this.leg0.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
		this.leg0.yRot = 0.0F;
		this.leg0.zRot = 0.0F;
		this.leg0.xRot += ((float)Math.PI / 5F);
		this.RightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.RightArm.yRot = 0.0F;
		this.RightArm.zRot = 0.0F;
		this.LeftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.LeftArm.yRot = 0.0F;
		this.LeftArm.zRot = 0.0F;
		this.rightwing.z = 2.0F;
		this.leftwing.z = 2.0F;
		this.rightwing.y = 1.0F;
		this.leftwing.y = 1.0F;
		this.rightwing.yRot = 0.47123894F + MathHelper.cos(ageInTicks * 0.8F) * (float)Math.PI * 0.05F;
		this.leftwing.yRot = -this.rightwing.yRot;
		this.leftwing.zRot = -0.47123894F;
		this.leftwing.xRot = 0.47123894F;
		this.rightwing.xRot = 0.47123894F;
		this.rightwing.zRot = 0.47123894F;
		if (entity.isCharging()){
			ModelHelper.animateZombieArms(this.LeftArm, this.RightArm, true, this.attackTime, ageInTicks);
		}
	}

	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.head, this.body, this.leg0, this.LeftArm, this.RightArm, this.leftwing, this.rightwing);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		head.render(matrixStack, buffer, packedLight, packedOverlay);
		body.render(matrixStack, buffer, packedLight, packedOverlay);
		RightArm.render(matrixStack, buffer, packedLight, packedOverlay);
		LeftArm.render(matrixStack, buffer, packedLight, packedOverlay);
		leg0.render(matrixStack, buffer, packedLight, packedOverlay);
		leftwing.render(matrixStack, buffer, packedLight, packedOverlay);
		rightwing.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}