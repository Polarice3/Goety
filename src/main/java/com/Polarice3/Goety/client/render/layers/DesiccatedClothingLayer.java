package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.hostile.dead.MarcireEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

public class DesiccatedClothingLayer<T extends MobEntity & IRangedAttackMob, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    private static final ResourceLocation TEXTURES = Goety.location("textures/entity/dead/desiccated_overlay.png");
    private static final ResourceLocation WIZARD = Goety.location("textures/entity/dead/marcire_overlay.png");
    private final SkeletonModel<T> layerModel = new SkeletonModel<>(0.25F, true);

    public DesiccatedClothingLayer(IEntityRenderer<T, M> p_i50919_1_) {
        super(p_i50919_1_);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ResourceLocation resourceLocation;
        if (entitylivingbaseIn instanceof MarcireEntity){
            resourceLocation = WIZARD;
        } else {
            resourceLocation = TEXTURES;
        }
        coloredCutoutModelCopyLayerRender(this.getParentModel(), layerModel, resourceLocation, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 1.0F, 1.0F, 1.0F);

    }
}
