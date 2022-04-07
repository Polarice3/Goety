package com.Polarice3.Goety.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class BoomerModel<T extends Entity> extends SegmentedModel<T> {
	private final ModelRenderer body;
	private final ModelRenderer head;
	private final ModelRenderer leg0;
	private final ModelRenderer leg1;
	private final ModelRenderer leg2;
	private final ModelRenderer leg3;

	public BoomerModel() {
		this(0.0F);
	}

	public BoomerModel(float p_i46366_1_) {
		texWidth = 64;
		texHeight = 32;

		body = new ModelRenderer(this);
		body.setPos(0.0F, 24.0F, 0.0F);
		body.texOffs(16, 14).addBox(-3.0F, -18.0F, -2.0F, 6.0F, 8.0F, 4.0F, p_i46366_1_, false);
		body.texOffs(36, 0).addBox(-4.0F, -10.0F, -3.0F, 8.0F, 4.0F, 6.0F, p_i46366_1_, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, -18.0F, 0.0F);
		body.addChild(head);
		head.texOffs(0, 0).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 6.0F, 8.0F, p_i46366_1_, false);

		leg0 = new ModelRenderer(this);
		leg0.setPos(-2.0F, -6.0F, 4.0F);
		body.addChild(leg0);
		leg0.texOffs(0, 16).addBox(-3.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, p_i46366_1_, false);

		leg1 = new ModelRenderer(this);
		leg1.setPos(2.0F, -6.0F, 4.0F);
		body.addChild(leg1);
		leg1.texOffs(0, 16).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, p_i46366_1_, false);

		leg2 = new ModelRenderer(this);
		leg2.setPos(-2.0F, -6.0F, -4.0F);
		body.addChild(leg2);
		leg2.texOffs(0, 16).addBox(-3.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, p_i46366_1_, false);

		leg3 = new ModelRenderer(this);
		leg3.setPos(2.0F, -6.0F, -4.0F);
		body.addChild(leg3);
		leg3.texOffs(0, 16).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, p_i46366_1_, false);
	}

	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.head, this.body, this.leg0, this.leg1, this.leg2, this.leg3);
	}

	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
		this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
		this.leg0.xRot = MathHelper.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
		this.leg1.xRot = MathHelper.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
		this.leg2.xRot = MathHelper.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
		this.leg3.xRot = MathHelper.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
	}
	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		body.render(matrixStack, buffer, packedLight, packedOverlay);
	}
}