package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.AbstractWraithModel;
import com.Polarice3.Goety.client.render.layers.DredenBandsLayer;
import com.Polarice3.Goety.common.entities.ally.DredenMinionEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class DredenMinionRenderer extends MobRenderer<DredenMinionEntity, AbstractWraithModel<DredenMinionEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/dreden.png");

    public DredenMinionRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new AbstractWraithModel<>(), 0.5F);
        this.addLayer(new DredenBandsLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(DredenMinionEntity entity) {
        return TEXTURE;
    }
}
