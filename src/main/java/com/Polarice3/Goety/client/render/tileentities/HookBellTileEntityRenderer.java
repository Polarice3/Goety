package com.Polarice3.Goety.client.render.tileentities;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.tileentities.HookBellTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.BellTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HookBellTileEntityRenderer extends TileEntityRenderer<HookBellTileEntity> {
   public static ResourceLocation TEXTURE = Goety.location("textures/entity/hook_bell/bell_body.png");
   private final ModelRenderer bellBody = new ModelRenderer(32, 32, 0, 0);

   public HookBellTileEntityRenderer(TileEntityRendererDispatcher p_i226005_1_) {
      super(p_i226005_1_);
      this.bellBody.addBox(-3.0F, -6.0F, -3.0F, 6.0F, 7.0F, 6.0F);
      this.bellBody.setPos(8.0F, 12.0F, 8.0F);
      ModelRenderer modelrenderer = new ModelRenderer(32, 32, 0, 13);
      modelrenderer.addBox(4.0F, 4.0F, 4.0F, 8.0F, 2.0F, 8.0F);
      modelrenderer.setPos(-8.0F, -12.0F, -8.0F);
      this.bellBody.addChild(modelrenderer);
   }

   public void render(HookBellTileEntity pBlockEntity, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pCombinedLight, int pCombinedOverlay) {
      float f = (float)pBlockEntity.ticks + pPartialTicks;
      float f1 = 0.0F;
      float f2 = 0.0F;
      if (pBlockEntity.shaking) {
         float f3 = MathHelper.sin(f / (float)Math.PI) / (4.0F + f / 3.0F);
         if (pBlockEntity.clickDirection == Direction.NORTH) {
            f1 = -f3;
         } else if (pBlockEntity.clickDirection == Direction.SOUTH) {
            f1 = f3;
         } else if (pBlockEntity.clickDirection == Direction.EAST) {
            f2 = -f3;
         } else if (pBlockEntity.clickDirection == Direction.WEST) {
            f2 = f3;
         }
      }

      this.bellBody.xRot = f1;
      this.bellBody.zRot = f2;
      IVertexBuilder ivertexbuilder = pBuffer.getBuffer(RenderType.entitySolid(TEXTURE));
      this.bellBody.render(pMatrixStack, ivertexbuilder, pCombinedLight, pCombinedOverlay);
   }
}