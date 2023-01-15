package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.AbstractWraithModel;
import com.Polarice3.Goety.common.entities.hostile.DredenEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class DredenRenderer extends MobRenderer<DredenEntity, AbstractWraithModel<DredenEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/dreden.png");

    public DredenRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new AbstractWraithModel<>(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(DredenEntity entity) {
        return TEXTURE;
    }
}
