package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.PenanceModel;
import com.Polarice3.Goety.common.entities.bosses.PenanceEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class PenanceRenderer extends MobRenderer<PenanceEntity, PenanceModel<PenanceEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/penance.png");

    public PenanceRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new PenanceModel<>(), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(PenanceEntity entity) {
        return TEXTURE;
    }
}
