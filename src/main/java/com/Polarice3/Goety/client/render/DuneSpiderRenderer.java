package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.DuneSpiderModel;
import com.Polarice3.Goety.client.render.layers.DuneSpiderEyesLayer;
import com.Polarice3.Goety.common.entities.hostile.DuneSpiderEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class DuneSpiderRenderer <T extends DuneSpiderEntity> extends MobRenderer<T, DuneSpiderModel<T>> {
    private static final ResourceLocation SPIDER_LOCATION = Goety.location("textures/entity/spiders/dune_spider.png");

    public DuneSpiderRenderer(EntityRendererManager p_i46139_1_) {
        super(p_i46139_1_, new DuneSpiderModel<>(), 0.8F);
        this.addLayer(new DuneSpiderEyesLayer<>(this));
    }

    protected float getFlipDegrees(T pLivingEntity) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(T pEntity) {
        return SPIDER_LOCATION;
    }
}
