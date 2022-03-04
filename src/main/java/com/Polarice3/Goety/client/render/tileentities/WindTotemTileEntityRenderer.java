package com.Polarice3.Goety.client.render.tileentities;

import com.Polarice3.Goety.common.tileentities.WindTotemTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class WindTotemTileEntityRenderer extends TileEntityRenderer<WindTotemTileEntity> {
    public WindTotemTileEntityRenderer(TileEntityRendererDispatcher p_i226016_1_) {
        super(p_i226016_1_);
    }

    @Override
    public void render(WindTotemTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {

    }
}
