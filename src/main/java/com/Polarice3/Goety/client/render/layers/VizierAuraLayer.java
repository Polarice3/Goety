package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.VizierModel;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class VizierAuraLayer extends EnergyLayer<VizierEntity, VizierModel> {
    private static final ResourceLocation VIZIER_ARMOR = new ResourceLocation(Goety.MOD_ID, "textures/entity/vizierarmor.png");
    private final VizierModel druidModel = new VizierModel(0.5F, 0.5F);

    public VizierAuraLayer(IEntityRenderer<VizierEntity, VizierModel> p_i50915_1_) {
        super(p_i50915_1_);
    }

    protected float xOffset(float p_225634_1_) {
        return MathHelper.cos(p_225634_1_ * 0.02F) * 3.0F;
    }

    protected ResourceLocation getTextureLocation() {
        return VIZIER_ARMOR;
    }

    protected EntityModel<VizierEntity> model() {
        return this.druidModel;
    }
}
