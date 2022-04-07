package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.client.model.BoomerModel;
import com.Polarice3.Goety.common.entities.hostile.BoomerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class BoomerChargeLayer extends EnergyLayer<BoomerEntity, BoomerModel<BoomerEntity>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final BoomerModel<BoomerEntity> model = new BoomerModel<>(2.0F);

    public BoomerChargeLayer(IEntityRenderer<BoomerEntity, BoomerModel<BoomerEntity>> p_i50947_1_) {
        super(p_i50947_1_);
    }

    protected float xOffset(float p_225634_1_) {
        return p_225634_1_ * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected EntityModel<BoomerEntity> model() {
        return this.model;
    }
}
