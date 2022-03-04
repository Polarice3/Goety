package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.ally.SpiderlingMinionEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SpiderEyesLayer;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.util.ResourceLocation;

public class SpiderlingMinionRenderer <T extends SpiderlingMinionEntity> extends MobRenderer<T, SpiderModel<T>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/spiderling.png");

    public SpiderlingMinionRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SpiderModel<>(), 0.8F * 0.4F);
        this.addLayer(new SpiderEyesLayer<>(this));
    }

    protected void scale(SpiderlingMinionEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.4F, 0.4F, 0.4F);
    }

    protected float getFlipDegrees(T pLivingEntity) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
