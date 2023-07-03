package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.neutral.AbstractMonolithEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class ObsidianMonolithRenderer extends AbstractMonolithRenderer{
    private static final ResourceLocation TEXTURE_LOCATION = Goety.location("textures/entity/monolith/obsidian_monolith.png");
    private static final RenderType RENDER_TYPE = RenderType.eyes(Goety.location("textures/entity/monolith/obsidian_monolith_glow.png"));
    private static final Map<AbstractMonolithEntity.Crackiness, ResourceLocation> resourceLocations = ImmutableMap.of(AbstractMonolithEntity.Crackiness.LOW, Goety.location("textures/entity/monolith/obsidian_monolith_crack_1.png"), AbstractMonolithEntity.Crackiness.MEDIUM, Goety.location("textures/entity/monolith/obsidian_monolith_crack_2.png"), AbstractMonolithEntity.Crackiness.HIGH, Goety.location("textures/entity/monolith/obsidian_monolith_crack_3.png"));

    public ObsidianMonolithRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public RenderType getActivatedTextureLocation() {
        return RENDER_TYPE;
    }

    @Override
    public Map<AbstractMonolithEntity.Crackiness, ResourceLocation> resourceLocations() {
        return resourceLocations;
    }

    public ResourceLocation getTextureLocation(AbstractMonolithEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
