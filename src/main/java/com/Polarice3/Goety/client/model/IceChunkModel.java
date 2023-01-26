package com.Polarice3.Goety.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class IceChunkModel<T extends Entity> extends SegmentedModel<T> {
	private final ModelRenderer chunk;

	public IceChunkModel() {
		texWidth = 64;
		texHeight = 64;

		chunk = new ModelRenderer(this);
		chunk.setPos(0.0F, 20.0F, 0.0F);
		chunk.texOffs(0, 20).addBox(-8.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		chunk.texOffs(24, 12).addBox(-8.0F, -4.0F, 0.0F, 8.0F, 4.0F, 8.0F, 0.0F, false);
		chunk.texOffs(0, 0).addBox(0.0F, -8.0F, -8.0F, 8.0F, 12.0F, 8.0F, 0.0F, false);
		chunk.texOffs(32, 24).addBox(0.0F, -8.0F, 0.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
		chunk.texOffs(32, 0).addBox(0.0F, -4.0F, 4.0F, 8.0F, 8.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		chunk.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public Iterable<ModelRenderer> parts() {
		return ImmutableList.of(this.chunk);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}