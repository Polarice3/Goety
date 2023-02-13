package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.WraithModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

public class BlightGlowLayer<T extends MobEntity, M extends WraithModel<T>> extends AbstractEyesLayer<T, M> {
    private static final RenderType RENDER_TYPE = RenderType.eyes(new ResourceLocation(Goety.MOD_ID, "textures/entity/dead/blight_eyes.png"));

    public BlightGlowLayer(IEntityRenderer<T, M> p_i50919_1_) {
        super(p_i50919_1_);
    }

    @Override
    public RenderType renderType() {
        return RENDER_TYPE;
    }
}
