package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.DuneSpiderModel;
import com.Polarice3.Goety.client.render.layers.DuneSpiderEyesLayer;
import com.Polarice3.Goety.common.entities.hostile.dead.BlightlingEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BlightlingRenderer<T extends BlightlingEntity> extends MobRenderer<T, DuneSpiderModel<T>> {
    private static final ResourceLocation SPIDER_LOCATION = Goety.location("textures/entity/dead/blightling.png");

    public BlightlingRenderer(EntityRendererManager p_i46139_1_) {
        super(p_i46139_1_, new DuneSpiderModel<>(), 0.8F * 0.4F);
        this.addLayer(new DuneSpiderEyesLayer<>(this));
    }

    protected void scale(BlightlingEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.4F, 0.4F, 0.4F);
    }

    protected float getFlipDegrees(T pLivingEntity) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(T pEntity) {
        return SPIDER_LOCATION;
    }
}
