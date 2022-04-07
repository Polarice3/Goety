package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.BoomerModel;
import com.Polarice3.Goety.client.render.layers.BoomerChargeLayer;
import com.Polarice3.Goety.common.entities.hostile.BoomerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class BoomerRenderer extends MobRenderer<BoomerEntity, BoomerModel<BoomerEntity>> {
    private static final ResourceLocation TEXTURES = Goety.location("textures/entity/boomer.png");

    public BoomerRenderer(EntityRendererManager p_i46186_1_) {
        super(p_i46186_1_, new BoomerModel<>(), 0.5F);
        this.addLayer(new BoomerChargeLayer(this));
    }

    protected void scale(BoomerEntity pLivingEntity, MatrixStack pMatrixStack, float pPartialTickTime) {
        float f = pLivingEntity.getSwelling(pPartialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        pMatrixStack.scale(f2, f3, f2);
    }

    protected float getWhiteOverlayProgress(BoomerEntity pLivingEntity, float pPartialTicks) {
        float f = pLivingEntity.getSwelling(pPartialTicks);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(BoomerEntity pEntity) {
        return TEXTURES;
    }
}
