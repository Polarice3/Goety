package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.common.entities.utilities.LightningTrapEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

public class LightningTrapRenderer extends EntityRenderer<LightningTrapEntity> {

    public LightningTrapRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    public ResourceLocation getTextureLocation(LightningTrapEntity entity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}
