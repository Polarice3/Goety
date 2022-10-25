package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ACArmorModel;
import com.Polarice3.Goety.client.model.AbstractCultistModel;
import com.Polarice3.Goety.client.model.ApostleModel;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class ApostleRenderer extends AbstractCultistRenderer<ApostleEntity>{
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/apostle.png");
    protected static final ResourceLocation TEXTURE2 = Goety.location("textures/entity/cultist/apostle_second.png");

    public ApostleRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ApostleModel<>(0.0F, 0.0F), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
        this.addLayer(new HeldItemLayer<ApostleEntity, AbstractCultistModel<ApostleEntity>>(this) {
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, ApostleEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entitylivingbaseIn.getArmPose() != AbstractCultistEntity.ArmPose.CROSSED) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }

            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(ApostleEntity entity) {
        return entity.isSecondPhase() ? TEXTURE2 : TEXTURE;
    }
}
