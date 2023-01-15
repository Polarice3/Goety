package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.AbstractWraithModel;
import com.Polarice3.Goety.client.render.layers.WraithGlowLayer;
import com.Polarice3.Goety.common.entities.hostile.WraithEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class WraithRenderer extends MobRenderer<WraithEntity, AbstractWraithModel<WraithEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/wraith.png");

    public WraithRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new AbstractWraithModel<>(), 0.5F);
        this.addLayer(new WraithGlowLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(WraithEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(WraithEntity pLivingEntity, MatrixStack pMatrixStack, float pPartialTickTime) {
        float f = pLivingEntity.getAnimationProgress(pPartialTickTime);
        if (f != 0.0F) {
            float f1 = 1.0F;
            if (f > 0.9F) {
                f1 = (float)((double)f1 * ((1.0D - (double)f) / (double)0.1F));
            }
            pMatrixStack.scale(f1, 1.0F, f1);
        }
    }
}