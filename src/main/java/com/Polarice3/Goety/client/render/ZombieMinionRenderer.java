package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ZombieMinionModel;
import com.Polarice3.Goety.common.entities.ally.ZombieMinionEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

public class ZombieMinionRenderer extends BipedRenderer<ZombieMinionEntity, ZombieMinionModel<ZombieMinionEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/zombieminion.png");

    public ZombieMinionRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new ZombieMinionModel<>(0.0F, false),0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ZombieMinionModel<>(0.5F, true), new ZombieMinionModel<>(1.0F, true)));
    }

    public ResourceLocation getTextureLocation(ZombieMinionEntity entity) {
        return TEXTURE;
    }

    @Override
    protected boolean isShaking(ZombieMinionEntity p_230495_1_) {
        return p_230495_1_.isUnderWaterConverting();
    }
}
