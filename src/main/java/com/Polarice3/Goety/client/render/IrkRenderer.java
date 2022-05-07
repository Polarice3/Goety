package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.IrkModel;
import com.Polarice3.Goety.common.entities.hostile.IrkEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class IrkRenderer extends MobRenderer<IrkEntity, IrkModel> {
    private static final ResourceLocation VEX_TEXTURE = Goety.location("textures/entity/irk.png");
    private static final ResourceLocation VEX_CHARGING_TEXTURE = Goety.location("textures/entity/irk_charging.png");

    public IrkRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new IrkModel(), 0.3F);
    }

    protected int getBlockLightLevel(IrkEntity entityIn, BlockPos partialTicks) {
        return 15;
    }

    public ResourceLocation getTextureLocation(IrkEntity entity) {
        return entity.isCharging() ? VEX_CHARGING_TEXTURE : VEX_TEXTURE;
    }

    protected void scale(IrkEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.4F, 0.4F, 0.4F);
    }
}
