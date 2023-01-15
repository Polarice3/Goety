package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.client.model.GhostFireModel;
import com.Polarice3.Goety.common.entities.projectiles.GhostFireEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;

public class GhostFireRenderer extends EntityRenderer<GhostFireEntity> {
    private final GhostFireModel<GhostFireEntity> model = new GhostFireModel<>();

    public GhostFireRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(GhostFireEntity pEntity, float entityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float f = pEntity.getAnimationProgress(pPartialTicks);
        if (f != 0.0F) {
            float f1 = 1.0F;
            if (f > 0.9F) {
                f1 = (float)((double)f1 * ((1.0D - (double)f) / (double)0.1F));
            }
            if (pEntity.getAnimation() < 13){
                f1 = 0.5F;
            }
            f1 *= 0.8F;
            pMatrixStack.pushPose();
            pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F - pEntity.yRot));
            pMatrixStack.scale(-f1, -1.25F, f1);
            pMatrixStack.translate(0.0D, -1.5D, 0.0D);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
            this.model.setupAnim(pEntity, 0.0F, 0.0F, pPartialTicks/10, 0, 0);
            this.model.renderToBuffer(pMatrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
            pMatrixStack.popPose();
            super.render(pEntity, entityYaw, pPartialTicks, pMatrixStack, bufferIn, packedLightIn);
        }
    }

    protected int getBlockLightLevel(GhostFireEntity pEntity, BlockPos pPos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(GhostFireEntity entity) {
        return entity.getResourceLocation();
    }
}
