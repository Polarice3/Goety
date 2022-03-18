package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ACArmorModel;
import com.Polarice3.Goety.client.model.SkeletonVillagerModel;
import com.Polarice3.Goety.common.entities.hostile.cultists.SkeletonVillagerMinionEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class SkeletonVillagerMinionRenderer extends AbstractCultistRenderer<SkeletonVillagerMinionEntity> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/skeletonvillagerminion.png");

    public SkeletonVillagerMinionRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new SkeletonVillagerModel<>(0.0F, 0.5F),0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
    }

    public ResourceLocation getTextureLocation(SkeletonVillagerMinionEntity entity) {
        return TEXTURE;
    }
}
