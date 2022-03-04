package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.TankModel;
import com.Polarice3.Goety.common.entities.ally.FriendlyTankEntity;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class TankCracksLayer <T extends MobEntity> extends LayerRenderer<T, TankModel<T>> {
    private static final Map<FriendlyTankEntity.Cracks, ResourceLocation> field_229134_a_ = ImmutableMap.of(FriendlyTankEntity.Cracks.LOW, new ResourceLocation(Goety.MOD_ID, "textures/entity/tank_damage_low.png"), FriendlyTankEntity.Cracks.MEDIUM, new ResourceLocation(Goety.MOD_ID, "textures/entity/tank_damage_medium.png"), FriendlyTankEntity.Cracks.HIGH, new ResourceLocation(Goety.MOD_ID, "textures/entity/tank_damage_high.png"));

    public TankCracksLayer(IEntityRenderer<T, TankModel<T>> entityRendererIn) {
        super(entityRendererIn);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn instanceof FriendlyTankEntity) {
            FriendlyTankEntity friendlyTank = (FriendlyTankEntity) entitylivingbaseIn;
            if (!entitylivingbaseIn.isInvisible()) {
                FriendlyTankEntity.Cracks tankentity$cracks = friendlyTank.func_226512_l_();
                if (tankentity$cracks != FriendlyTankEntity.Cracks.NONE) {
                    ResourceLocation resourcelocation = field_229134_a_.get(tankentity$cracks);
                    renderColoredCutoutModel(this.getParentModel(), resourcelocation, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }
}
