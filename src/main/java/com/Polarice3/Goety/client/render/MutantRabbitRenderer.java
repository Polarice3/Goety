package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.MutantRabbitModel;
import com.Polarice3.Goety.common.entities.neutral.MutatedRabbitEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public class MutantRabbitRenderer extends MobRenderer<MutatedRabbitEntity, MutantRabbitModel<MutatedRabbitEntity>> {
    private static final ResourceLocation RABBIT_BROWN_LOCATION = new ResourceLocation(Goety.MOD_ID, "textures/entity/mutatedrabbit_brown.png");
    private static final ResourceLocation RABBIT_WHITE_LOCATION = new ResourceLocation(Goety.MOD_ID, "textures/entity/mutatedrabbit_white.png");
    private static final ResourceLocation RABBIT_BLACK_LOCATION = new ResourceLocation(Goety.MOD_ID, "textures/entity/mutatedrabbit_black.png");
    private static final ResourceLocation RABBIT_GOLD_LOCATION = new ResourceLocation(Goety.MOD_ID, "textures/entity/mutatedrabbit_gold.png");
    private static final ResourceLocation RABBIT_SALT_LOCATION = new ResourceLocation(Goety.MOD_ID, "textures/entity/mutatedrabbit_salt.png");
    private static final ResourceLocation RABBIT_WHITE_SPLOTCHED_LOCATION = new ResourceLocation(Goety.MOD_ID, "textures/entity/mutatedrabbit_splotched.png");

    public MutantRabbitRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new MutantRabbitModel<>(), 0.5F);
    }

    protected void scale(MutatedRabbitEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        if (entitylivingbaseIn.hasIgnited()) {
            float f = entitylivingbaseIn.getMutatedFlashIntensity(partialTickTime);
            float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
            f = MathHelper.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f2 = (1.0F + f * 0.4F) * f1;
            float f3 = (1.0F + f * 0.1F) / f1;
            matrixStackIn.scale(f2 + 2.0F, f3 + 2.0F, f2 + 2.0F);
        } else {
            matrixStackIn.scale(2.0F, 2.0F, 2.0F);
        }
    }

    protected float getWhiteOverlayProgress(MutatedRabbitEntity livingEntityIn, float partialTicks) {
        float f = livingEntityIn.getMutatedFlashIntensity(partialTicks);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
    }

    public ResourceLocation getTextureLocation(MutatedRabbitEntity pEntity) {
        String s = TextFormatting.stripFormatting(pEntity.getName().getString());
        if ("Toast".equals(s)) {
            return RABBIT_WHITE_SPLOTCHED_LOCATION;
        } else {
            switch(pEntity.getRabbitType()) {
                case 0:
                default:
                    return RABBIT_BROWN_LOCATION;
                case 1:
                    return RABBIT_WHITE_LOCATION;
                case 2:
                    return RABBIT_BLACK_LOCATION;
                case 3:
                    return RABBIT_WHITE_SPLOTCHED_LOCATION;
                case 4:
                    return RABBIT_GOLD_LOCATION;
                case 5:
                    return RABBIT_SALT_LOCATION;
            }
        }
    }
}
