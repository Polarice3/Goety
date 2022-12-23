package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ACArmorModel;
import com.Polarice3.Goety.client.model.AbstractCultistModel;
import com.Polarice3.Goety.common.entities.hostile.cultists.ReturnedEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

public class ReturnedRenderer extends AbstractCultistRenderer<ReturnedEntity> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/returned.png");

    public ReturnedRenderer(EntityRendererManager p_i50966_1_) {
        super(p_i50966_1_, new AbstractCultistModel<>(0.0F, 0.5F), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
    }

    @Override
    public ResourceLocation getTextureLocation(ReturnedEntity pEntity) {
        return TEXTURE;
    }
}
