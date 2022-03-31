package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.client.model.LoyalSpiderModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class LoyalSpiderEyesLayer<T extends Entity, M extends LoyalSpiderModel<T>> extends AbstractEyesLayer<T, M> {
    private static final RenderType SPIDER_EYES = RenderType.eyes(new ResourceLocation("textures/entity/spider_eyes.png"));

    public LoyalSpiderEyesLayer(IEntityRenderer<T, M> p_i50921_1_) {
        super(p_i50921_1_);
    }

    public RenderType renderType() {
        return SPIDER_EYES;
    }
}