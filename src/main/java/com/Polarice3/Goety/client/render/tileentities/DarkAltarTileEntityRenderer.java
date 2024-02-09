package com.Polarice3.Goety.client.render.tileentities;

import com.Polarice3.Goety.common.blocks.tiles.DarkAltarTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class DarkAltarTileEntityRenderer extends TileEntityRenderer<DarkAltarTileEntity> {
    public DarkAltarTileEntityRenderer(TileEntityRendererDispatcher p_i226007_1_) {
        super(p_i226007_1_);
    }

    public void render(DarkAltarTileEntity pBlockEntity, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pCombinedLight, int pCombinedOverlay) {
        pBlockEntity.itemStackHandler.ifPresent(handler -> {
            ItemStack stack = handler.getStackInSlot(0);
            Minecraft minecraft = Minecraft.getInstance();
            if (!stack.isEmpty()) {
                pMatrixStack.pushPose();
                pMatrixStack.translate(0.5F, 0.85F, 0.5F);
                pMatrixStack.scale(1.0F, 1.0F, 1.0F);
                assert minecraft.level != null;
                pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(3 * (minecraft.level.getGameTime() % 360 + pPartialTicks)));
                minecraft.getItemRenderer().renderStatic(stack, ItemCameraTransforms.TransformType.GROUND, pCombinedLight, pCombinedOverlay, pMatrixStack, pBuffer);
                pMatrixStack.popPose();
            }
        });
    }

}
