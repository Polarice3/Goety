package com.Polarice3.Goety.client.render.tileentities;

import com.Polarice3.Goety.common.blocks.TallSkullBlock;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class ModItemTERenderer extends ItemStackTileEntityRenderer {

    public ModItemTERenderer() {
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemCameraTransforms.TransformType pCamera, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pLight, int pOverlay) {
        Item item = pStack.getItem();

        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            if (block instanceof TallSkullBlock) {
                if(pCamera == ItemCameraTransforms.TransformType.GUI) {
                    pMatrixStack.pushPose();
                    pMatrixStack.translate(0.5F, 0.5F, 0.5F);
                    pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(30));
                    pMatrixStack.mulPose(Vector3f.YN.rotationDegrees(-45));
                    pMatrixStack.translate(-0.5F, -0.5F, -0.5F);
                    pMatrixStack.translate(0.0F, 0.25F, 0.0F);
                    TallSkullTileEntityRenderer.renderSkull(null, 180.0F, pMatrixStack, pBuffer, pLight);
                    pMatrixStack.popPose();

                } else {
                    TallSkullTileEntityRenderer.renderSkull(null, 180.0F, pMatrixStack, pBuffer, pLight);
                }
            }
        }
    }

}
