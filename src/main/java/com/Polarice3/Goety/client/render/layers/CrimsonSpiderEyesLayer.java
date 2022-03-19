package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class CrimsonSpiderEyesLayer<T extends Entity, M extends SpiderModel<T>> extends AbstractEyesLayer<T, M> {
    private static final RenderType SPIDER_EYES = RenderType.eyes(new ResourceLocation(Goety.MOD_ID, "textures/entity/cultist/scarlet_spider_eyes.png"));

    public CrimsonSpiderEyesLayer(IEntityRenderer<T, M> p_i50921_1_) {
        super(p_i50921_1_);
    }

    public RenderType renderType() {
        return SPIDER_EYES;
    }
}