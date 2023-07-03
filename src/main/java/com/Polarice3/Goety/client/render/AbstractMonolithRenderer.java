package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.client.model.MonolithModel;
import com.Polarice3.Goety.common.entities.neutral.AbstractMonolithEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Map;

public abstract class AbstractMonolithRenderer extends EntityRenderer<AbstractMonolithEntity> {
    private final MonolithModel<AbstractMonolithEntity> model = new MonolithModel<>();

    public AbstractMonolithRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(AbstractMonolithEntity pEntity, float entityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float f = Math.min(AbstractMonolithEntity.getEmergingTime(), pEntity.getAge());
        pMatrixStack.pushPose();
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F - pEntity.yRot));
        pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
        pMatrixStack.translate(0.0D, 0.0D, 0.0D);
        pMatrixStack.scale(1.0F, 1.0F, 1.0F);
        this.model.setupAnim(pEntity, f, 0.0F, pPartialTicks, pEntity.yRot, pEntity.xRot);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.renderType(getTextureLocation(pEntity)));
        this.model.renderToBuffer(pMatrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!pEntity.isEmerging() && !pEntity.isInvisible()) {
            IVertexBuilder vertexconsumer = bufferIn.getBuffer(getActivatedTextureLocation());
            this.model.renderToBuffer(pMatrixStack, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            AbstractMonolithEntity.Crackiness irongolem$crackiness = pEntity.getCrackiness();
            if (irongolem$crackiness != AbstractMonolithEntity.Crackiness.NONE) {
                ResourceLocation resourcelocation = resourceLocations().get(irongolem$crackiness);
                renderColoredCutoutModel(this.model, resourcelocation, pMatrixStack, bufferIn, packedLightIn, 1.0F, 1.0F, 1.0F);
            }
        }
        pMatrixStack.popPose();
        super.render(pEntity, entityYaw, pPartialTicks, pMatrixStack, bufferIn, packedLightIn);
    }

    protected static <T extends LivingEntity> void renderColoredCutoutModel(EntityModel<T> p_117377_, ResourceLocation p_117378_, MatrixStack p_117379_, IRenderTypeBuffer p_117380_, int p_117381_, float p_117383_, float p_117384_, float p_117385_) {
        IVertexBuilder vertexconsumer = p_117380_.getBuffer(RenderType.entityCutoutNoCull(p_117378_));
        p_117377_.renderToBuffer(p_117379_, vertexconsumer, p_117381_, OverlayTexture.NO_OVERLAY, p_117383_, p_117384_, p_117385_, 1.0F);
    }

    public abstract RenderType getActivatedTextureLocation();

    public abstract Map<AbstractMonolithEntity.Crackiness, ResourceLocation> resourceLocations();
}
