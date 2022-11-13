package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.UrbhadhachModel;
import com.Polarice3.Goety.client.render.layers.UrbhadhachVisageLayer;
import com.Polarice3.Goety.common.entities.hostile.UrbhadhachEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class UrbhadhachRenderer extends MobRenderer<UrbhadhachEntity, UrbhadhachModel<UrbhadhachEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/urbhadhach.png");

    public UrbhadhachRenderer(EntityRendererManager renderManagerIn){
        super(renderManagerIn, new UrbhadhachModel<>(), 0.5F);
        this.addLayer(new UrbhadhachVisageLayer<>(this));
    }

    protected void scale(UrbhadhachEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f;
        if (entitylivingbaseIn.getHealTime() > 0){
            f = 1.2F;
        } else {
            f = 1.0F;
        }
        matrixStackIn.scale(f, f, f);
    }

    @Override
    public ResourceLocation getTextureLocation(UrbhadhachEntity entity) {
        return TEXTURE;
    }
}
