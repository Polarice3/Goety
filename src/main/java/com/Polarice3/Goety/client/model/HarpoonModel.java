package com.Polarice3.Goety.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class HarpoonModel extends Model {
   private final ModelRenderer pole = new ModelRenderer(32, 32, 0, 10);

   public HarpoonModel() {
      super(RenderType::entitySolid);
      this.pole.addBox(-0.5F, 0.0F, -0.5F, 1.0F, 21.0F, 1.0F);
      ModelRenderer modelrenderer = new ModelRenderer(32, 32, 0, 0);
      modelrenderer.addBox(-0.5F, 2.0F, -2.5F, 1.0F, 1.0F, 2.0F);
      this.pole.addChild(modelrenderer);
      ModelRenderer modelrenderer1 = new ModelRenderer(32, 32, 2, 0);
      modelrenderer1.addBox(-0.5F, 3.0F, -4.5F, 1.0F, 1.0F, 4.0F);
      this.pole.addChild(modelrenderer1);
      ModelRenderer modelrenderer2 = new ModelRenderer(32, 32, 12, 0);
      modelrenderer2.addBox(-0.5F, 4.0F, -4.5F, 1.0F, 3.0F, 1.0F, 0.0F);
      this.pole.addChild(modelrenderer2);
      ModelRenderer modelrenderer3 = new ModelRenderer(32, 32, 16, 0);
      modelrenderer3.addBox(-0.5F, 6.0F, -3.5F, 1.0F, 1.0F, 2.0F);
      this.pole.addChild(modelrenderer3);
      ModelRenderer modelrenderer4 = new ModelRenderer(32, 32, 22, 0);
      modelrenderer4.addBox(-0.5F, 5.0F, -2.5F, 1.0F, 1.0F, 1.0F);
      this.pole.addChild(modelrenderer4);
      this.pole.yRot = -1.5708F;
   }

   public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
      this.pole.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
   }
}