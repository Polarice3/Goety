package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.UrbhadhachModel;
import com.Polarice3.Goety.common.entities.hostile.UrbhadhachEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;

public class UrbhadhachVisageLayer<T extends UrbhadhachEntity, M extends UrbhadhachModel<T>> extends AbstractEyesLayer<T, M> {
    private static final RenderType VISAGE = RenderType.eyes(Goety.location("textures/entity/urbhadhach_overlay.png"));

    public UrbhadhachVisageLayer(IEntityRenderer<T, M> p_i50921_1_) {
        super(p_i50921_1_);
    }

    public RenderType renderType() {
        return VISAGE;
    }
}