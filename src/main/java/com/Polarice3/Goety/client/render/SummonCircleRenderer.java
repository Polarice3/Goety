package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.SummonCircleModel;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.common.entities.utilities.SummonCircleEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class SummonCircleRenderer extends EntityRenderer<SummonCircleEntity> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(Goety.MOD_ID,"textures/entity/summon_circle.png");
    private static final ResourceLocation TEXTURES_APOSTLE = new ResourceLocation(Goety.MOD_ID,"textures/entity/cultist/apostle_summon_circle.png");
    private final SummonCircleModel model = new SummonCircleModel();

    public SummonCircleRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    public void render(SummonCircleEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.renderType(this.getTextureLocation(entityIn)));
        this.model.setupAnim(entityIn, 0.0F, 0.0F, partialTicks/10, entityIn.yRot, entityIn.xRot);
        matrixStackIn.scale(1.5F, 1.5F, 1.5F);
        matrixStackIn.translate(0.0D, 1.6D, 0.0D);
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        this.model.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(SummonCircleEntity pEntity) {
        if(pEntity.getTrueOwner() instanceof ApostleEntity){
            return TEXTURES_APOSTLE;
        } else {
            return TEXTURES;
        }
    }
}
