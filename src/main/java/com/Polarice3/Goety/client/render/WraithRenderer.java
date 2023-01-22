package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.WraithModel;
import com.Polarice3.Goety.client.render.layers.WraithGlowLayer;
import com.Polarice3.Goety.common.entities.neutral.AbstractWraithEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class WraithRenderer extends AbstractWraithRenderer {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/wraith.png");

    public WraithRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new WraithModel<>(), 0.5F);
        this.addLayer(new WraithGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractWraithEntity entity) {
        return TEXTURE;
    }

}
