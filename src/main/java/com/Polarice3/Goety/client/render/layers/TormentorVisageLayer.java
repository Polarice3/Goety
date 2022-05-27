package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.TormentorModel;
import com.Polarice3.Goety.common.entities.hostile.illagers.TormentorEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.util.ResourceLocation;

public class TormentorVisageLayer<T extends TormentorEntity, M extends TormentorModel<T>> extends AbstractEyesLayer<T, M> {
    private static final RenderType VISAGE = RenderType.eyes(new ResourceLocation(Goety.MOD_ID, "textures/entity/illagers/tormentor_visage.png"));

    public TormentorVisageLayer(IEntityRenderer<T, M> p_i50921_1_) {
        super(p_i50921_1_);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entitylivingbaseIn.isCharging()) {
            super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }

    }

    public RenderType renderType() {
        return VISAGE;
    }
}