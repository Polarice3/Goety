package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.neutral.AbstractMonolithEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class MonolithModel<T extends AbstractMonolithEntity> extends EntityModel<T> {
	private final ModelRenderer monolith;

	public MonolithModel() {
		texWidth = 64;
		texHeight = 64;

		monolith = new ModelRenderer(this);
		monolith.setPos(0.0F, 24.0F, 0.0F);
		monolith.texOffs(0, 0).addBox(-7.0F, -50.0F, -7.0F, 14.0F, 50.0F, 14.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entity.isEmerging()) {
			this.monolith.y = (AbstractMonolithEntity.getEmergingTime()) - limbSwing;
		} else {
			this.monolith.y = 0;
		}
	}

	@Override
	public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
		monolith.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}