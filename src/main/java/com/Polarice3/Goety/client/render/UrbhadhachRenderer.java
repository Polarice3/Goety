package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.UrbhadhachModel;
import com.Polarice3.Goety.client.render.layers.UrbhadhachVisageLayer;
import com.Polarice3.Goety.common.entities.hostile.UrbhadhachEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class UrbhadhachRenderer extends MobRenderer<UrbhadhachEntity, UrbhadhachModel<UrbhadhachEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/urbhadhach.png");

    public UrbhadhachRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new UrbhadhachModel<>(), 0.5F);
        this.addLayer(new UrbhadhachVisageLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(UrbhadhachEntity entity) {
        return TEXTURE;
    }
}
