package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ACArmorModel;
import com.Polarice3.Goety.client.model.SkeletonVillagerModel;
import com.Polarice3.Goety.common.entities.ally.SkeletonPillagerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class SkeletonPillagerRenderer extends BipedRenderer<SkeletonPillagerEntity, SkeletonVillagerModel<SkeletonPillagerEntity>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/skeleton/skeleton_pillager.png");

    public SkeletonPillagerRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new SkeletonVillagerModel<>(0.0F, 0.5F),0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
    }

    protected void scale(SkeletonPillagerEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }

    public ResourceLocation getTextureLocation(SkeletonPillagerEntity entity) {
        return TEXTURE;
    }
}
