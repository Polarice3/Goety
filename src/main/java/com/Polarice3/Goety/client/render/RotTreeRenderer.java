package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.RotTreeModel;
import com.Polarice3.Goety.client.render.layers.RotTreeCracksLayer;
import com.Polarice3.Goety.common.entities.ally.RottreantEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RotTreeRenderer extends MobRenderer<RottreantEntity, RotTreeModel> {
    private static final ResourceLocation HAUNTED_LOCATION = Goety.location("textures/entity/servants/rottree/rottree_haunted.png");
    private static final ResourceLocation MURK_LOCATION = Goety.location("textures/entity/servants/rottree/rottree_murk.png");
    private static final ResourceLocation GLOOM_LOCATION = Goety.location("textures/entity/servants/rottree/rottree_gloom.png");

    public RotTreeRenderer(EntityRendererManager p_i46133_1_) {
        super(p_i46133_1_, new RotTreeModel(), 0.7F);
        this.addLayer(new RotTreeCracksLayer(this));
    }

    public ResourceLocation getTextureLocation(RottreantEntity pEntity) {
        switch (pEntity.getWoodType()){
            case MURK:
                return MURK_LOCATION;
            case GLOOM:
                return GLOOM_LOCATION;
            default:
                return HAUNTED_LOCATION;
        }
    }
}
