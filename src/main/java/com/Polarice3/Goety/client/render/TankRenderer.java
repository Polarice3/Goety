package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.TankModel;
import com.Polarice3.Goety.client.render.layers.TankCracksLayer;
import com.Polarice3.Goety.common.entities.neutral.AbstractTankEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class TankRenderer extends MobRenderer<AbstractTankEntity, TankModel<AbstractTankEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/tank.png");

    public TankRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new TankModel<>(), 1.0F);
        this.addLayer(new TankCracksLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractTankEntity entity) {
        return TEXTURE;
    }
}
