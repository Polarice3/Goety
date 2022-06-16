package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.common.entities.hostile.BoneLordEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;

public class BoneLordRenderer extends BipedRenderer<BoneLordEntity, SkeletonModel<BoneLordEntity>> {
    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public BoneLordRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SkeletonModel<>(), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new SkeletonModel<>(0.5F, true), new SkeletonModel<>(1.0F, true)));
    }

    public void render(BoneLordEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        SkeletonModel<BoneLordEntity> skeletonModel = this.getModel();
        skeletonModel.head.visible = false;
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public ResourceLocation getTextureLocation(BoneLordEntity entity) {
        return SKELETON_LOCATION;
    }
}
