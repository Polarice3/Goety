package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.FelFlyModel;
import com.Polarice3.Goety.common.entities.ally.FelFlyEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class FelFlyRenderer extends MobRenderer<FelFlyEntity, FelFlyModel<FelFlyEntity>> {
    private static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/fel_fly.png");

    public FelFlyRenderer(EntityRendererManager p_i226033_1_) {
        super(p_i226033_1_, new FelFlyModel<>(), 0.2F);
    }

    public ResourceLocation getTextureLocation(FelFlyEntity pEntity) {
        return TEXTURE;
    }

    protected void scale(FelFlyEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
    }
}
