package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.utilities.SummonCircleEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SummonCircleModel extends SegmentedModel<SummonCircleEntity> {
	private final ModelRenderer summon;
	private final ModelRenderer base;
	private final ModelRenderer sides;

	public SummonCircleModel() {
		texWidth = 32;
		texHeight = 32;

		summon = new ModelRenderer(this);
		summon.setPos(0.0F, 24.0F, 0.0F);

		base = new ModelRenderer(this);
		base.setPos(0.0F, 0.0F, 0.0F);
		summon.addChild(base);

		ModelRenderer cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(0.0F, 0.0F, 0.0F);
		base.addChild(cube_r1);
		setRotationAngle(cube_r1, -1.5708F, 0.0F, 0.0F);
		cube_r1.texOffs(0, 0).addBox(-7.0F, -7.0F, 0.0F, 14.0F, 14.0F, 0.0F, 0.0F, false);

		sides = new ModelRenderer(this);
		sides.setPos(0.0F, 0.0F, 0.0F);
		summon.addChild(sides);
		sides.texOffs(0, 14).addBox(-7.0F, -5.0F, 7.0F, 14.0F, 5.0F, 0.0F, 0.0F, false);
		sides.texOffs(0, 14).addBox(-7.0F, -5.0F, -7.0F, 14.0F, 5.0F, 0.0F, 0.0F, false);
		sides.texOffs(0, 0).addBox(7.0F, -5.0F, -7.0F, 0.0F, 5.0F, 14.0F, 0.0F, false);
		sides.texOffs(0, 0).addBox(-7.0F, -5.0F, -7.0F, 0.0F, 5.0F, 14.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(SummonCircleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		base.yRot += ageInTicks * 0.5F;
		sides.yRot -= ageInTicks * 0.5F;
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		summon.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.summon);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}