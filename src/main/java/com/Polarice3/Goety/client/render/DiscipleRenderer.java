package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ACArmorModel;
import com.Polarice3.Goety.client.model.AbstractCultistModel;
import com.Polarice3.Goety.common.entities.hostile.cultists.DiscipleEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class DiscipleRenderer extends AbstractCultistRenderer<DiscipleEntity>{
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/disciple.png");

    public DiscipleRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new AbstractCultistModel<>(0.0F, 0.0F), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(DiscipleEntity entity) {
        return TEXTURE;
    }
}
