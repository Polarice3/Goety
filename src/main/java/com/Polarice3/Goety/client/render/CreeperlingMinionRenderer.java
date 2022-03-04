package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.layers.CreeperlingChargeLayer;
import com.Polarice3.Goety.common.entities.ally.CreeperlingMinionEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class CreeperlingMinionRenderer extends MobRenderer<CreeperlingMinionEntity, SpiderModel<CreeperlingMinionEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/creeperling.png");

    public CreeperlingMinionRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SpiderModel<>(), 0.8F * 0.4F);
        this.addLayer(new CreeperlingChargeLayer(this));
    }

    protected void scale(CreeperlingMinionEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        if (entitylivingbaseIn.isIgnited()) {
            float f = entitylivingbaseIn.getSwelling(partialTickTime);
            float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
            f = MathHelper.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f2 = (1.0F + f * 0.4F) * f1;
            float f3 = (1.0F + f * 0.1F) / f1;
            matrixStackIn.scale(f2 + 0.4F, f3 + 0.4F, f2 + 0.4F);
        } else {
            matrixStackIn.scale(0.4F, 0.4F, 0.4F);
        }
    }

    protected float getWhiteOverlayProgress(CreeperlingMinionEntity pLivingEntity, float pPartialTicks) {
        float f = pLivingEntity.getSwelling(pPartialTicks);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
    }

    protected float getFlipDegrees(CreeperlingMinionEntity pLivingEntity) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(CreeperlingMinionEntity entity) {
        return TEXTURE;
    }
}
