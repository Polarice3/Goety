package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.HarpoonModel;
import com.Polarice3.Goety.common.entities.projectiles.HarpoonEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class HarpoonRenderer extends EntityRenderer<HarpoonEntity> {
   public static final ResourceLocation TEXTURE = Goety.location("textures/entity/projectiles/harpoon.png");
   private final HarpoonModel model;

   public HarpoonRenderer(EntityRendererManager p_174420_) {
      super(p_174420_);
      this.model = new HarpoonModel();
   }

   public void render(HarpoonEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
      pMatrixStack.pushPose();
      pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(pPartialTicks, pEntity.yRotO, pEntity.yRot) - 90.0F));
      pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(pPartialTicks, pEntity.xRotO, pEntity.xRot) + 90.0F));
      float f9 = (float)pEntity.shakeTime - pPartialTicks;
      if (f9 > 0.0F) {
         float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
         pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(f10));
      }
      IVertexBuilder vertexconsumer = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
      this.model.renderToBuffer(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      pMatrixStack.popPose();
      super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
   }

   public ResourceLocation getTextureLocation(HarpoonEntity p_116109_) {
      return TEXTURE;
   }
}