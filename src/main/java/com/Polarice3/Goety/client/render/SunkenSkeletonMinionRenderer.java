package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.SunkenSkeletonModel;
import com.Polarice3.Goety.common.entities.ally.AbstractSMEntity;
import com.Polarice3.Goety.common.entities.ally.SunkenSkeletonMinion;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class SunkenSkeletonMinionRenderer extends BipedRenderer<SunkenSkeletonMinion, SunkenSkeletonModel<SunkenSkeletonMinion>> {
   private static final ResourceLocation TEXTURES = Goety.location("textures/entity/servants/skeleton/sunken_skeleton_minion.png");

   public SunkenSkeletonMinionRenderer(EntityRendererManager p_174382_) {
      super(p_174382_, new SunkenSkeletonModel<>(0.0F), 0.5F);
      this.addLayer(new BipedArmorLayer<>(this, new SunkenSkeletonModel<>(0.5F), new SunkenSkeletonModel<>(1.0F)));
      this.addLayer(new SunkenSkeletonServantLayer<>(this));
   }

   public ResourceLocation getTextureLocation(SunkenSkeletonMinion servant) {
      return TEXTURES;
   }

   protected void setupRotations(SunkenSkeletonMinion pEntityLiving, MatrixStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
      super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
      float f = pEntityLiving.getSwimAmount(pPartialTicks);
      if (f > 0.0F) {
         pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.lerp(f, pEntityLiving.xRot, -10.0F - pEntityLiving.xRot)));
      }

   }

   public static class SunkenSkeletonServantLayer<T extends AbstractSMEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
      private static final ResourceLocation TEXTURES = Goety.location("textures/entity/servants/skeleton/sunken_skeleton_minion_overlay.png");
      private final SunkenSkeletonModel<T> layerModel = new SunkenSkeletonModel<>(0.25F);

      public SunkenSkeletonServantLayer(IEntityRenderer<T, M> p_i50919_1_) {
         super(p_i50919_1_);
      }

      @Override
      public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
         coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, TEXTURES, matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 1.0F, 1.0F, 1.0F);
      }
   }
}