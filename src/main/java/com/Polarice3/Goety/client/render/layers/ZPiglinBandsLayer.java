package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ZPiglinModel;
import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class ZPiglinBandsLayer<T extends SummonedEntity> extends LayerRenderer<T, ZPiglinModel<T>> {
    private static final ResourceLocation TEXTURES = Goety.location("textures/entity/servants/zpiglin_servant_bands.png");
    private final ZPiglinModel<T> layerModel = new ZPiglinModel<>(0.0F, 64, 64);

    public ZPiglinBandsLayer(IEntityRenderer<T, ZPiglinModel<T>> p_i50919_1_) {
        super(p_i50919_1_);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.getTrueOwner() instanceof PlayerEntity) {
            coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, TEXTURES, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 1.0F, 1.0F, 1.0F);
        }
    }
}
