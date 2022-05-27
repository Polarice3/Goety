package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ACArmorModel;
import com.Polarice3.Goety.client.model.ConquillagerModel;
import com.Polarice3.Goety.common.entities.hostile.illagers.ConquillagerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class ConquillagerRenderer<T extends ConquillagerEntity> extends MobRenderer<T, ConquillagerModel<T>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/illagers/conquillager.png");

    public ConquillagerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ConquillagerModel<>(0.0F, 0.0F), 0.5F);
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
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

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return TEXTURE;
    }
}
