package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.hostile.cultists.HogLordEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.BoarModel;
import net.minecraft.util.ResourceLocation;

public class HogLordRenderer extends MobRenderer<HogLordEntity, BoarModel<HogLordEntity>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/hoglord.png");

    public HogLordRenderer(EntityRendererManager p_i232470_1_) {
        super(p_i232470_1_, new BoarModel<>(), 0.7F);
    }

    protected void scale(HogLordEntity pLivingEntity, MatrixStack pMatrixStack, float pPartialTickTime) {
        pMatrixStack.scale(1.5F, 1.5F, 1.5F);
    }

    public ResourceLocation getTextureLocation(HogLordEntity pEntity) {
        return TEXTURE;
    }
}
