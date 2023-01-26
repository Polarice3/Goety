package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ZombieMinionModel;
import com.Polarice3.Goety.common.entities.ally.HuskMinionEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

public class HuskMinionRenderer extends BipedRenderer<HuskMinionEntity, ZombieMinionModel<HuskMinionEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/servants/husk_minion.png");

    public HuskMinionRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new ZombieMinionModel<>(0.0F, false),0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ZombieMinionModel<>(0.5F, true), new ZombieMinionModel<>(1.0F, true)));
    }

    public ResourceLocation getTextureLocation(HuskMinionEntity entity) {
        return TEXTURE;
    }

    @Override
    protected boolean isShaking(HuskMinionEntity p_230495_1_) {
        return p_230495_1_.isUnderWaterConverting();
    }
}
