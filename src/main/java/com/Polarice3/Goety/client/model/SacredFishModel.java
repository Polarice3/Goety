package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.neutral.SacredFishEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class SacredFishModel extends SegmentedModel<SacredFishEntity> {
	private final ModelRenderer body_front;
	private final ModelRenderer body_back;
	private final ModelRenderer dorsal_back;
	private final ModelRenderer tailfin;
	private final ModelRenderer dorsal_front;
	private final ModelRenderer head;
	private final ModelRenderer leftFin;
	private final ModelRenderer rightFin;

	public SacredFishModel() {
		texWidth = 32;
		texHeight = 32;

		body_front = new ModelRenderer(this);
		body_front.setPos(0.0F, 16.5F, -1.25F);
		body_front.texOffs(0, 0).addBox(-1.5F, -1.0F, -2.75F, 3.0F, 5.0F, 8.0F, 0.0F, false);
		body_front.texOffs(22, 21).addBox(-1.0F, -2.5F, -2.75F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		body_back = new ModelRenderer(this);
		body_back.setPos(0.0F, 1.5F, 9.25F);
		body_front.addChild(body_back);
		body_back.texOffs(0, 13).addBox(-1.5F, -2.5F, -4.0F, 3.0F, 5.0F, 8.0F, 0.0F, false);

		dorsal_back = new ModelRenderer(this);
		dorsal_back.setPos(0.0F, 1.0F, -4.0F);
		body_back.addChild(dorsal_back);
		dorsal_back.texOffs(2, 3).addBox(0.0F, -5.5F, 0.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);

		tailfin = new ModelRenderer(this);
		tailfin.setPos(0.0F, 6.0F, 4.0F);
		body_back.addChild(tailfin);
		tailfin.texOffs(20, 10).addBox(0.0F, -8.5F, 0.0F, 0.0F, 5.0F, 6.0F, 0.0F, false);

		dorsal_front = new ModelRenderer(this);
		dorsal_front.setPos(0.0F, -2.0F, 4.25F);
		body_front.addChild(dorsal_front);
		dorsal_front.texOffs(4, 2).addBox(0.0F, -1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, 1.0F, -4.25F);
		body_front.addChild(head);
		head.texOffs(22, 0).addBox(-1.0F, -2.0F, -1.5F, 2.0F, 4.0F, 3.0F, 0.0F, false);

		leftFin = new ModelRenderer(this);
		leftFin.setPos(2.3191F, 3.5736F, -1.75F);
		body_front.addChild(leftFin);
		setRotationAngle(leftFin, 0.0F, 0.0F, 0.6109F);
		leftFin.texOffs(2, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, 0.0F, false);

		rightFin = new ModelRenderer(this);
		rightFin.setPos(-2.3192F, 3.5736F, -1.75F);
		body_front.addChild(rightFin);
		setRotationAngle(rightFin, 0.0F, 0.0F, -0.6109F);
		rightFin.texOffs(-2, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(SacredFishEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		float f = 1.0F;
		float f1 = 1.0F;
		if (!entity.isInWater()) {
			f = 1.3F;
			f1 = 1.7F;
		}

		this.body_back.yRot = -f * 0.25F * MathHelper.sin(f1 * 0.6F * ageInTicks);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body_front.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.body_front, this.body_back, this.head, this.leftFin, this.rightFin);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}