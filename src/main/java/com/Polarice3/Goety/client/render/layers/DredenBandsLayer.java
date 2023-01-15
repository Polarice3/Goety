package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.AbstractWraithModel;
import com.Polarice3.Goety.common.entities.ally.DredenMinionEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class DredenBandsLayer extends LayerRenderer<DredenMinionEntity, AbstractWraithModel<DredenMinionEntity>> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(Goety.MOD_ID, "textures/entity/dreden_minion_bands.png");
    private final AbstractWraithModel layerModel = new AbstractWraithModel();

    public DredenBandsLayer(IEntityRenderer<DredenMinionEntity, AbstractWraithModel<DredenMinionEntity>> p_i50919_1_) {
        super(p_i50919_1_);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, DredenMinionEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, TEXTURES, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 1.0F, 1.0F, 1.0F);

    }
}
