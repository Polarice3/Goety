package com.Polarice3.Goety.client.render.tileentities;

import com.Polarice3.Goety.common.tileentities.CursedCageTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class CursedCageTileEntityRenderer extends TileEntityRenderer<CursedCageTileEntity> {
    public CursedCageTileEntityRenderer(TileEntityRendererDispatcher p_i226007_1_) {
        super(p_i226007_1_);
    }

    public void render(CursedCageTileEntity pBlockEntity, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pCombinedLight, int pCombinedOverlay) {
        ItemStack itemStack = pBlockEntity.getItem();
        Minecraft minecraft = Minecraft.getInstance();
        if (!itemStack.isEmpty()){
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.5F, 0.25F, 0.5F);
            pMatrixStack.scale(1.0F, 1.0F, 1.0F);
            assert minecraft.level != null;
            if (pBlockEntity.getSpinning() > 0){
                pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(12 * (minecraft.level.getGameTime() % 360 + pPartialTicks)));
            } else {
                pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(3 * (minecraft.level.getGameTime() % 360 + pPartialTicks)));
            }
            minecraft.getItemRenderer().renderStatic(itemStack, ItemCameraTransforms.TransformType.GROUND, pCombinedLight, pCombinedOverlay, pMatrixStack, pBuffer);
            pMatrixStack.popPose();
        }
    }

}
