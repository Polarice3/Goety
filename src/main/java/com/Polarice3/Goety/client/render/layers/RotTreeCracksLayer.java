package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.RotTreeModel;
import com.Polarice3.Goety.common.entities.ally.RottreantEntity;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class RotTreeCracksLayer extends LayerRenderer<RottreantEntity, RotTreeModel> {
    private static final Map<RottreantEntity.Cracks, ResourceLocation> resourceLocations = ImmutableMap.of(RottreantEntity.Cracks.LOW, Goety.location("textures/entity/servants/rottree/rottree_crack_low.png"), RottreantEntity.Cracks.MEDIUM, Goety.location("textures/entity/servants/rottree/rottree_crack_medium.png"), RottreantEntity.Cracks.HIGH, Goety.location("textures/entity/servants/rottree/rottree_crack_high.png"));

    public RotTreeCracksLayer(IEntityRenderer<RottreantEntity, RotTreeModel> p_i226040_1_) {
        super(p_i226040_1_);
    }

    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, RottreantEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (!pLivingEntity.isInvisible()) {
            RottreantEntity.Cracks irongolementity$cracks = pLivingEntity.getCracks();
            if (irongolementity$cracks != RottreantEntity.Cracks.NONE) {
                ResourceLocation resourcelocation = resourceLocations.get(irongolementity$cracks);
                renderColoredCutoutModel(this.getParentModel(), resourcelocation, pMatrixStack, pBuffer, pPackedLight, pLivingEntity, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
