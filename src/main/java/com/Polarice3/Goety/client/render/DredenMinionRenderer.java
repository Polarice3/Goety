package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.DredenModel;
import com.Polarice3.Goety.client.render.layers.DredenBandsLayer;
import com.Polarice3.Goety.common.entities.neutral.AbstractWraithEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class DredenMinionRenderer extends AbstractWraithRenderer {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/dreden.png");

    public DredenMinionRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new DredenModel<>(), 0.5F);
        this.addLayer(new DredenBandsLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractWraithEntity entity) {
        return TEXTURE;
    }
}
