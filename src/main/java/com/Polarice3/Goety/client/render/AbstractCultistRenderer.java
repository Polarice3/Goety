package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.client.model.AbstractCultistModel;
import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public abstract class AbstractCultistRenderer<T extends AbstractCultistEntity> extends MobRenderer<T, AbstractCultistModel<T>> {
    protected AbstractCultistRenderer(EntityRendererManager p_i50966_1_, AbstractCultistModel<T> p_i50966_2_, float p_i50966_3_) {
        super(p_i50966_1_, p_i50966_2_, p_i50966_3_);
        this.addLayer(new HeadLayer<>(this));
    }

    protected void scale(T entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }

    protected void setupRotations(T pEntityLiving, MatrixStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        float f = pEntityLiving.getSwimAmount(pPartialTicks);
        if (f > 0.0F) {
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.lerp(f, pEntityLiving.xRot, -10.0F - pEntityLiving.xRot)));
        }

    }

}
