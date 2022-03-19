package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.layers.CrimsonSpiderEyesLayer;
import com.Polarice3.Goety.client.render.layers.TamedSpiderCollarLayer;
import com.Polarice3.Goety.client.render.layers.TamedSpiderEyesLayer;
import com.Polarice3.Goety.common.entities.ally.TamedSpiderEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.CrimsonSpiderEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.util.ResourceLocation;

public class CrimsonSpiderRenderer<T extends CrimsonSpiderEntity> extends MobRenderer<T, SpiderModel<T>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/cultist/scarlet_spider.png");

    public CrimsonSpiderRenderer(EntityRendererManager p_i46139_1_) {
        super(p_i46139_1_, new SpiderModel<>(), 0.8F);
        this.addLayer(new CrimsonSpiderEyesLayer<>(this));
    }

    protected void scale(CrimsonSpiderEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.2F, 1.2F, 1.2F);
    }

    protected float getFlipDegrees(T pLivingEntity) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
