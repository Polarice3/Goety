package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.common.entities.neutral.AbstractWraithEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractWraithRenderer extends MobRenderer<AbstractWraithEntity, SegmentedModel<AbstractWraithEntity>> {

    public AbstractWraithRenderer(EntityRendererManager p_i50961_1_, SegmentedModel<AbstractWraithEntity> p_i50961_2_, float p_i50961_3_) {
        super(p_i50961_1_, p_i50961_2_, p_i50961_3_);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractWraithEntity entity) {
        return null;
    }

    @Override
    protected void scale(AbstractWraithEntity pLivingEntity, MatrixStack pMatrixStack, float pPartialTickTime) {
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
