package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.common.entities.projectiles.BlightFireEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class BlightFireRenderer extends GhostFireRenderer {

    public BlightFireRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    public ResourceLocation getTextureLocation(BlightFireEntity entity) {
        return entity.getResourceLocation();
    }
}
