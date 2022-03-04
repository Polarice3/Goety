package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.hostile.ParasiteEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EndermiteModel;
import net.minecraft.util.ResourceLocation;

public class ParasiteRenderer extends MobRenderer<ParasiteEntity, EndermiteModel<ParasiteEntity>> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(Goety.MOD_ID, "textures/entity/parasite.png");

    public ParasiteRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new EndermiteModel<>(), 0.3F);
    }

    protected float getFlipDegrees(ParasiteEntity pLivingEntity) {
        return 180.0F;
    }

    public ResourceLocation getTextureLocation(ParasiteEntity entity) {
        return TEXTURES;
    }
}
