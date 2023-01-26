package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.IceChunkModel;
import com.Polarice3.Goety.common.entities.projectiles.IceChunkEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class IceChunkRenderer extends EntityRenderer<IceChunkEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID,"textures/entity/projectiles/ice_chunk.png");
    private final IceChunkModel<IceChunkEntity> model = new IceChunkModel<>();

    public IceChunkRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(IceChunkEntity pEntity, float entityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        pMatrixStack.pushPose();
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F - pEntity.yRot));
        pMatrixStack.scale(-2.0F, -2.0F, 2.0F);
        pMatrixStack.translate(0.0D, -1.45D, 0.0D);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
        this.model.setupAnim(pEntity, 0.0F, 0.0F, pPartialTicks, 0, 0);
        this.model.renderToBuffer(pMatrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
        pMatrixStack.popPose();
        super.render(pEntity, entityYaw, pPartialTicks, pMatrixStack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(IceChunkEntity pEntity) {
        return TEXTURE;
    }
}
