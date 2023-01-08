package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.DrownedMinionModel;
import com.Polarice3.Goety.common.entities.ally.DrownedMinionEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class DrownedMinionOuterLayer<T extends DrownedMinionEntity> extends LayerRenderer<T, DrownedMinionModel<T>> {
    private static final ResourceLocation DROWNED_OUTER_LAYER_LOCATION = Goety.location("textures/entity/servants/drowned_minion_outer_layer.png");
    private final DrownedMinionModel<T> model = new DrownedMinionModel<>(0.25F, 0.0F, 64, 64);

    public DrownedMinionOuterLayer(IEntityRenderer<T, DrownedMinionModel<T>> p_i50943_1_) {
        super(p_i50943_1_);
    }

    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, DROWNED_OUTER_LAYER_LOCATION, pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch, pPartialTicks, 1.0F, 1.0F, 1.0F);
    }
}
