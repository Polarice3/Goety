package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ZombieMinionModel;
import com.Polarice3.Goety.common.entities.ally.FarmerMinionEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

public class FarmerMinionRenderer extends BipedRenderer<FarmerMinionEntity, ZombieMinionModel<FarmerMinionEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/farmerminion.png");

    public FarmerMinionRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new ZombieMinionModel<>(0.0F, false),0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ZombieMinionModel<>(0.5F, true), new ZombieMinionModel<>(1.0F, true)));
    }

    public ResourceLocation getTextureLocation(FarmerMinionEntity entity) {
        return TEXTURE;
    }
}
