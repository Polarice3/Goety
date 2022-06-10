package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.LocustModel;
import com.Polarice3.Goety.common.entities.hostile.dead.LocustEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class LocustRenderer extends MobRenderer<LocustEntity, LocustModel<LocustEntity>> {
    private static final ResourceLocation BEE_TEXTURE = Goety.location("textures/entity/dead/locust.png");

    public LocustRenderer(EntityRendererManager p_i226033_1_) {
        super(p_i226033_1_, new LocustModel<>(), 0.4F);
    }

    public ResourceLocation getTextureLocation(LocustEntity pEntity) {
        return BEE_TEXTURE;
    }
}
