package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ACArmorModel;
import com.Polarice3.Goety.client.model.ApostleModel;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class ApostleRenderer extends AbstractCultistRenderer<ApostleEntity>{
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/apostle.png");

    public ApostleRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ApostleModel<>(0.0F, 0.0F), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ApostleEntity entity) {
        return TEXTURE;
    }
}
