package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.TormentorModel;
import com.Polarice3.Goety.client.render.layers.TormentorVisageLayer;
import com.Polarice3.Goety.common.entities.hostile.illagers.TormentorEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class TormentorRenderer extends BipedRenderer<TormentorEntity, TormentorModel<TormentorEntity>> {
    private static final ResourceLocation TEXTURE = Goety.location("textures/entity/tormentor.png");
    private static final ResourceLocation TEXTURE_CHARGE = Goety.location("textures/entity/tormentor_charge.png");

    public TormentorRenderer(EntityRendererManager p_i47190_1_) {
        super(p_i47190_1_, new TormentorModel<>(0.0F, 0.5F), 0.5F);
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new TormentorVisageLayer<>(this));
    }

    protected int getBlockLightLevel(TormentorEntity pEntity, BlockPos pPos) {
        return 15;
    }

    public ResourceLocation getTextureLocation(TormentorEntity pEntity) {
        return pEntity.isCharging() ? TEXTURE_CHARGE : TEXTURE;
    }

    protected void scale(TormentorEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
