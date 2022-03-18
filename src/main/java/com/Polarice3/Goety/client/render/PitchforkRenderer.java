package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.PitchforkModel;
import com.Polarice3.Goety.common.entities.projectiles.PitchforkEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class PitchforkRenderer extends EntityRenderer<PitchforkEntity> {
    public static final ResourceLocation TEXTURE = Goety.location("textures/entity/pitchfork.png");

    private final PitchforkModel model = new PitchforkModel();

    public PitchforkRenderer(EntityRendererManager p_i48828_1_) {
        super(p_i48828_1_);
    }

    public void render(PitchforkEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(pPartialTicks, pEntity.yRotO, pEntity.yRot) - 90.0F));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(pPartialTicks, pEntity.xRotO, pEntity.xRot) + 90.0F));
        IVertexBuilder ivertexbuilder = net.minecraft.client.renderer.ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, pEntity.isFoil());
        this.model.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public ResourceLocation getTextureLocation(PitchforkEntity pEntity) {
        return TEXTURE;
    }
}
