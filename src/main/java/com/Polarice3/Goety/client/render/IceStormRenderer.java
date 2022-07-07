package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.projectiles.IceStormEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class IceStormRenderer extends EntityRenderer<IceStormEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID,"textures/entity/projectiles/ice_storm.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucentCull(TEXTURE);
    private final GenericHeadModel model = new GenericHeadModel();

    public IceStormRenderer(EntityRendererManager p_i46129_1_) {
        super(p_i46129_1_);
    }

    protected int getBlockLightLevel(IceStormEntity pEntity, BlockPos pPos) {
        return 15;
    }

    public void render(IceStormEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        float f = MathHelper.rotlerp(pEntity.yRotO, pEntity.yRot, pPartialTicks);
        float f1 = MathHelper.lerp(pPartialTicks, pEntity.xRotO, pEntity.xRot);
        pMatrixStack.scale(-4.0F, -4.0F, 4.0F);
        this.model.setupAnim(0.0F, f, f1);
        IVertexBuilder ivertexbuilder1 = pBuffer.getBuffer(RENDER_TYPE);
        this.model.renderToBuffer(pMatrixStack, ivertexbuilder1, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.45F);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public ResourceLocation getTextureLocation(IceStormEntity pEntity) {
        return TEXTURE;
    }
}
