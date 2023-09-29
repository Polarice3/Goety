package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.projectiles.DeathArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class DeathArrowRenderer extends ArrowRenderer<DeathArrowEntity> {
    public static final ResourceLocation ARROW_LOCATION = Goety.location("textures/entity/projectiles/death_arrow.png");

    public DeathArrowRenderer(EntityRendererManager p_i46193_1_) {
        super(p_i46193_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(DeathArrowEntity p_114482_) {
        return ARROW_LOCATION;
    }
}
