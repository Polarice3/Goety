package com.Polarice3.Goety.client.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class TrapRenderer extends EntityRenderer<Entity> {

    public TrapRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    public ResourceLocation getTextureLocation(Entity entity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}
