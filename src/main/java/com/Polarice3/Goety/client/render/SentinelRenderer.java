package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.SentinelModel;
import com.Polarice3.Goety.common.entities.hostile.dead.SentinelEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SentinelRenderer extends MobRenderer<SentinelEntity, SentinelModel> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/dead/sentinel.png");

    public SentinelRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SentinelModel(), 0.7F);
    }

    public ResourceLocation getTextureLocation(SentinelEntity entity) {
        return TEXTURE;
    }

    protected void setupRotations(SentinelEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float yRot, float partialTicks) {
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, yRot, partialTicks);
        if (!((double)entityLiving.animationSpeed < 0.01D)) {
            float f = 13.0F;
            float f1 = entityLiving.animationPosition - entityLiving.animationSpeed * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(6.5F * f2));
        }
    }
}
