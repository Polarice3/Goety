package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.common.entities.ally.CreeperlingMinionEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SpiderModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CreeperlingChargeLayer extends EnergyLayer<CreeperlingMinionEntity, SpiderModel<CreeperlingMinionEntity>> {
    private static final ResourceLocation POWER_LOCATION = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final SpiderModel<CreeperlingMinionEntity> model = new SpiderModel<>();

    public CreeperlingChargeLayer(IEntityRenderer<CreeperlingMinionEntity, SpiderModel<CreeperlingMinionEntity>> p_i50947_1_) {
        super(p_i50947_1_);
    }

    protected float xOffset(float p_225634_1_) {
        return p_225634_1_ * 0.01F;
    }

    protected ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected EntityModel<CreeperlingMinionEntity> model() {
        return this.model;
    }
}
