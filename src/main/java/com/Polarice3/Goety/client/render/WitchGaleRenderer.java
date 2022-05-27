package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.WitchGaleModel;
import com.Polarice3.Goety.common.entities.projectiles.WitchGaleEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class WitchGaleRenderer extends EntityRenderer<WitchGaleEntity> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(Goety.MOD_ID,"textures/entity/projectiles/cripplegust.png");
    private final WitchGaleModel<WitchGaleEntity> model = new WitchGaleModel<>();

    public WitchGaleRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    protected float getBob(WitchGaleEntity pLivingBase, float pPartialTicks) {
        return (float)pLivingBase.tickCount + pPartialTicks;
    }

    public void render(WitchGaleEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float f7 = this.getBob(entityIn, partialTicks);
        matrixStackIn.pushPose();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.renderType(TEXTURES));
        this.model.setupAnim(entityIn, 0.0F, 0.0F, f7, 0, 0);
        matrixStackIn.translate(0.0D, (double)(entityIn.getBbHeight() + (entityIn.isUpgraded() ? 1.5F: 0.5F)), 0.0D);
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        this.scale(entityIn, matrixStackIn);
        this.model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void scale(WitchGaleEntity entityIn, MatrixStack matrixStackIn) {
        if (entityIn.isUpgraded()){
            matrixStackIn.scale(2.0F, 2.0F, 2.0F);
        } else {
            matrixStackIn.scale(1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(WitchGaleEntity pEntity) {
        return TEXTURES;
    }
}
