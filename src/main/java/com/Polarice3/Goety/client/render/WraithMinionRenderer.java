package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.WraithModel;
import com.Polarice3.Goety.client.render.layers.WraithBandsLayer;
import com.Polarice3.Goety.client.render.layers.WraithGlowLayer;
import com.Polarice3.Goety.client.render.layers.WraithSecretLayer;
import com.Polarice3.Goety.common.entities.neutral.AbstractWraithEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class WraithMinionRenderer extends AbstractWraithRenderer {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/wraith.png");

    public WraithMinionRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new WraithModel<>(), 0.5F);
        this.addLayer(new WraithGlowLayer(this));
        this.addLayer(new WraithBandsLayer(this));
        this.addLayer(new WraithSecretLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractWraithEntity entity) {
        return TEXTURE;
    }
}
