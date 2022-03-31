package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.LoyalSpiderModel;
import com.Polarice3.Goety.client.render.layers.LoyalSpiderCollarLayer;
import com.Polarice3.Goety.client.render.layers.LoyalSpiderEyesLayer;
import com.Polarice3.Goety.client.render.layers.LoyalSpiderSaddleLayer;
import com.Polarice3.Goety.common.entities.ally.LoyalSpiderEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class LoyalSpiderRenderer<T extends LoyalSpiderEntity> extends MobRenderer<T, LoyalSpiderModel<T>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/loyal_spider.png");
    protected static final ResourceLocation POISON_TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/loyal_spider_poison.png");

    public LoyalSpiderRenderer(EntityRendererManager p_i46139_1_) {
        super(p_i46139_1_, new LoyalSpiderModel<>(), 0.8F);
        this.addLayer(new LoyalSpiderEyesLayer<>(this));
        this.addLayer(new LoyalSpiderCollarLayer<>(this));
        this.addLayer(new LoyalSpiderSaddleLayer<>(this));
    }

    protected void scale(LoyalSpiderEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        if (entitylivingbaseIn.isRideable()){
            matrixStackIn.scale(1.2F, 1.2F, 1.2F);
        } else if (entitylivingbaseIn.isPoison() && !entitylivingbaseIn.isBaby() && !entitylivingbaseIn.isRideable()){
            matrixStackIn.scale(0.7F, 0.7F, 0.7F);
        } else {
            float f = entitylivingbaseIn.isBaby() ? 0.4F : 1.0F;
            matrixStackIn.scale(f, f, f);
        }
    }

    protected float getFlipDegrees(T pLivingEntity) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(T entity) {
        return entity.isPoison() ? POISON_TEXTURE : TEXTURE;
    }

    protected void setupRotations(T pEntityLiving, MatrixStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        if (pEntityLiving.isSitting()){
            pMatrixStack.translate(0, -0.5, 0);
        } else {
            pMatrixStack.translate(0, 0, 0);
        }

    }
}
