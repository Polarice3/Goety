package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.layers.DesiccatedClothingLayer;
import com.Polarice3.Goety.client.render.layers.DesiccatedEyesLayer;
import com.Polarice3.Goety.common.entities.hostile.dead.MarcireEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class MarcireRenderer extends BipedRenderer<MarcireEntity, SkeletonModel<MarcireEntity>> {
    private static final ResourceLocation TEXTURES = Goety.location("textures/entity/dead/desiccated.png");

    public MarcireRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SkeletonModel<>(), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new SkeletonModel<>(0.5F, true), new SkeletonModel<>(1.0F, true)));
        this.addLayer(new DesiccatedClothingLayer<>(this));
        this.addLayer(new DesiccatedEyesLayer<>(this));
    }

    public ResourceLocation getTextureLocation(MarcireEntity entity) {
        return TEXTURES;
    }

    protected void setupRotations(MarcireEntity pEntityLiving, MatrixStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        if (!((double)pEntityLiving.animationSpeed < 0.01D)) {
            float f = 13.0F;
            float f1 = pEntityLiving.animationPosition - pEntityLiving.animationSpeed * (1.0F - pPartialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(6.5F * f2));
        }
    }
}
