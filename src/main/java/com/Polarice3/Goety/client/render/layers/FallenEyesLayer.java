package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.monster.ZombieEntity;

public class FallenEyesLayer <T extends ZombieEntity, M extends ZombieModel<T>> extends AbstractEyesLayer<T, M> {
    private static final RenderType EYES = RenderType.eyes(Goety.location("textures/entity/dead/fallen_eyes.png"));

    public FallenEyesLayer(IEntityRenderer<T, M> p_i50921_1_) {
        super(p_i50921_1_);
    }

    public RenderType renderType() {
        return EYES;
    }
}
