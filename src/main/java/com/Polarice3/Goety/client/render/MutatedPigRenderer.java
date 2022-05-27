package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.MutatedPigModel;
import com.Polarice3.Goety.common.entities.neutral.MutatedPigEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class MutatedPigRenderer extends MobRenderer<MutatedPigEntity, MutatedPigModel<MutatedPigEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/mutated/mutatedpig.png");

    public MutatedPigRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new MutatedPigModel<>(), 1.0F);
    }

    protected void scale(MutatedPigEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = entitylivingbaseIn.getMutatedFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        matrixStackIn.scale(f2, f3, f2);
    }

    protected float getWhiteOverlayProgress(MutatedPigEntity livingEntityIn, float partialTicks) {
        float f = livingEntityIn.getMutatedFlashIntensity(partialTicks);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(MutatedPigEntity entity) {
        return TEXTURE;
    }
}
