package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.layers.TamedSpiderCollarLayer;
import com.Polarice3.Goety.client.render.layers.TamedSpiderEyesLayer;
import com.Polarice3.Goety.common.entities.ally.TamedSpiderEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.util.ResourceLocation;

public class TamedSpiderRenderer <T extends TamedSpiderEntity> extends MobRenderer<T, SpiderModel<T>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/tamed_spider.png");
    protected static final ResourceLocation POISON_TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/tamed_spider_poison.png");

    public TamedSpiderRenderer(EntityRendererManager p_i46139_1_) {
        super(p_i46139_1_, new SpiderModel<>(), 0.8F);
        this.addLayer(new TamedSpiderEyesLayer<>(this));
        this.addLayer(new TamedSpiderCollarLayer<>(this));
    }

    protected void scale(TamedSpiderEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
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
}
