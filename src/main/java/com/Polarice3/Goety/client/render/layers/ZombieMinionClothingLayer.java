package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ZombieMinionModel;
import com.Polarice3.Goety.common.entities.ally.ZombieMinionEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class ZombieMinionClothingLayer extends LayerRenderer<ZombieMinionEntity, ZombieMinionModel<ZombieMinionEntity>> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(Goety.MOD_ID, "textures/entity/zombieminion_overlay.png");
    private final ZombieMinionModel layerModel = new ZombieMinionModel(0.25F, false);

    public ZombieMinionClothingLayer(IEntityRenderer<ZombieMinionEntity, ZombieMinionModel<ZombieMinionEntity>> p_i50919_1_) {
        super(p_i50919_1_);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, ZombieMinionEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, TEXTURES, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 1.0F, 1.0F, 1.0F);

    }
}
