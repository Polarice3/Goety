package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.LoyalSpiderModel;
import com.Polarice3.Goety.common.entities.ally.LoyalSpiderEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class LoyalSpiderSaddleLayer <T extends LoyalSpiderEntity> extends LayerRenderer<T, LoyalSpiderModel<T>> {
    private static final ResourceLocation SADDLE_LOCATION = new ResourceLocation(Goety.MOD_ID, "textures/entity/loyal_spider_saddle.png");
    private final LoyalSpiderModel<T> model = new LoyalSpiderModel<>();

    public LoyalSpiderSaddleLayer(IEntityRenderer<T, LoyalSpiderModel<T>> p_i50914_1_) {
        super(p_i50914_1_);
    }

    @Override
    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity.isSaddled()) {
            this.getParentModel().copyPropertiesTo(this.model);
            this.model.prepareMobModel(pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks);
            this.model.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            IVertexBuilder ivertexbuilder = pBuffer.getBuffer(RenderType.entityCutoutNoCull(SADDLE_LOCATION));
            this.model.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
