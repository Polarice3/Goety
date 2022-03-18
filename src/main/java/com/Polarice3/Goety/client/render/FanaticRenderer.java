package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ACArmorModel;
import com.Polarice3.Goety.client.model.AbstractCultistModel;
import com.Polarice3.Goety.common.entities.hostile.cultists.FanaticEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class FanaticRenderer extends AbstractCultistRenderer<FanaticEntity>{
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/fanatic.png");
    protected static final ResourceLocation FANATIC_0 = Goety.location("textures/entity/cultist/fanatic/fanatic_0.png");
    protected static final ResourceLocation FANATIC_1 = Goety.location("textures/entity/cultist/fanatic/fanatic_1.png");
    protected static final ResourceLocation FANATIC_2 = Goety.location("textures/entity/cultist/fanatic/fanatic_2.png");
    protected static final ResourceLocation FANATIC_3 = Goety.location("textures/entity/cultist/fanatic/fanatic_3.png");

    public FanaticRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new AbstractCultistModel<>(0.0F, 0.5F), 0.5F);
        this.addLayer(new HeldItemLayer<FanaticEntity, AbstractCultistModel<FanaticEntity>>(this) {
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, FanaticEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entitylivingbaseIn.isAggressive()) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }

            }
        });
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
    }

    @Override
    public ResourceLocation getTextureLocation(FanaticEntity entity) {
        return entity.getResourceLocation();
    }
}
