package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.MinionModel;
import com.Polarice3.Goety.common.entities.ally.FriendlyScorchEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class FriendlyScorchRenderer extends BipedRenderer<FriendlyScorchEntity, MinionModel<FriendlyScorchEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/scorch.png");
    protected static final ResourceLocation CHARGING_TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/scorch_charging.png");

    public FriendlyScorchRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new MinionModel<>(), 0.3F);
    }

    protected int getBlockLightLevel(FriendlyScorchEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    public ResourceLocation getTextureLocation(FriendlyScorchEntity entity) {
        return entity.isCharging() ? CHARGING_TEXTURE : TEXTURE;
    }

    protected void scale(FriendlyScorchEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.4F, 0.4F, 0.4F);
    }
}
