package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.DredenModel;
import com.Polarice3.Goety.common.entities.ally.DredenMinionEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class DredenMinionRenderer extends MobRenderer<DredenMinionEntity, DredenModel<DredenMinionEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/dreden_minion.png");

    public DredenMinionRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new DredenModel<>(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(DredenMinionEntity entity) {
        return TEXTURE;
    }
}
