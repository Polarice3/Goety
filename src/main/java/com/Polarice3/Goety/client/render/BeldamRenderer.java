package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ModWitchModel;
import com.Polarice3.Goety.client.render.layers.ModWitchItemLayer;
import com.Polarice3.Goety.common.entities.hostile.cultists.BeldamEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BeldamRenderer extends MobRenderer<BeldamEntity, ModWitchModel<BeldamEntity>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/beldam.png");

    public BeldamRenderer(EntityRendererManager p_i46131_1_) {
        super(p_i46131_1_, new ModWitchModel<>(0.0F), 0.5F);
        this.addLayer(new ModWitchItemLayer<>(this));
    }

    public void render(BeldamEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        this.model.setHoldingItem(!pEntity.getMainHandItem().isEmpty());
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public ResourceLocation getTextureLocation(BeldamEntity pEntity) {
        return TEXTURE;
    }

    protected void scale(BeldamEntity pLivingEntity, MatrixStack pMatrixStack, float pPartialTickTime) {
        float f = 0.9375F;
        pMatrixStack.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
