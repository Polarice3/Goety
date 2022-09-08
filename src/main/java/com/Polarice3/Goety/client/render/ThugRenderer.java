package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ThugModel;
import com.Polarice3.Goety.common.entities.hostile.cultists.ThugEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class ThugRenderer extends MobRenderer<ThugEntity, ThugModel> {
    private static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/thug.png");
    private static final ResourceLocation TEXTURE_ANGRY = Goety.location("textures/entity/cultist/thug_angry.png");

    public ThugRenderer(EntityRendererManager p_i46133_1_) {
        super(p_i46133_1_, new ThugModel(), 0.7F);
    }

    public ResourceLocation getTextureLocation(ThugEntity pEntity) {
        return pEntity.isRaging() ? TEXTURE_ANGRY : TEXTURE;
    }
}
