package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.SpikeModel;
import com.Polarice3.Goety.common.entities.projectiles.SpikeEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class SpikeRenderer extends EntityRenderer<SpikeEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = Goety.location("textures/entity/projectiles/spike.png");
    private final SpikeModel<SpikeEntity> model = new SpikeModel<>();

    public SpikeRenderer(EntityRendererManager p_i47208_1_) {
        super(p_i47208_1_);
    }

    public void render(SpikeEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        float f = pEntity.getAnimationProgress(pPartialTicks);
        if (f != 0.0F) {
            float f1 = 2.0F;
            if (f > 0.9F) {
                f1 = (float)((double)f1 * ((1.0D - (double)f) / (double)0.1F));
            }

            pMatrixStack.pushPose();
            pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F - pEntity.yRot));
            pMatrixStack.scale(-f1, -f1, f1);
            float f2 = 0.03125F;
            pMatrixStack.translate(0.0D, (double)-0.626F, 0.0D);
            pMatrixStack.scale(0.5F, 0.5F, 0.5F);
            this.model.setupAnim(pEntity, f, 0.0F, pPartialTicks/10, pEntity.yRot, pEntity.xRot);
            IVertexBuilder ivertexbuilder = pBuffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
            this.model.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pMatrixStack.popPose();
            super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        }
    }

    public ResourceLocation getTextureLocation(SpikeEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
