package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.DuneSpiderModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.entity.Entity;

public class DuneSpiderEyesLayer<T extends Entity, M extends DuneSpiderModel<T>> extends AbstractEyesLayer<T, M> {
    private static final RenderType SPIDER_EYES = RenderType.eyes(Goety.location("textures/entity/dead/dune_spider_eyes.png"));

    public DuneSpiderEyesLayer(IEntityRenderer<T, M> p_i50921_1_) {
        super(p_i50921_1_);
    }

    public RenderType renderType() {
        return SPIDER_EYES;
    }
}