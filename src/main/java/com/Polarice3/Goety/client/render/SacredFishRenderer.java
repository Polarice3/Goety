package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.SacredFishModel;
import com.Polarice3.Goety.common.entities.neutral.SacredFishEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class SacredFishRenderer extends MobRenderer<SacredFishEntity, SacredFishModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/sacredfish.png");

    public SacredFishRenderer(EntityRendererManager p_i48862_1_) {
        super(p_i48862_1_, new SacredFishModel(), 0.4F);
    }

    public ResourceLocation getTextureLocation(SacredFishEntity pEntity) {
        return TEXTURE;
    }

    protected void setupRotations(SacredFishEntity pEntityLiving, MatrixStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        float f = 1.0F;
        float f1 = 1.0F;
        if (!pEntityLiving.isInWater()) {
            f = 1.3F;
            f1 = 1.7F;
        }

        float f2 = f * 4.3F * MathHelper.sin(f1 * 0.6F * pAgeInTicks);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(f2));
        pMatrixStack.translate(0.0D, 0.0D, (double)-0.4F);
        if (!pEntityLiving.isInWater()) {
            pMatrixStack.translate((double)0.2F, (double)0.1F, 0.0D);
            pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
        }

    }
}
