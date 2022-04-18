package com.Polarice3.Goety.client.render.tileentities;

import com.Polarice3.Goety.common.tileentities.CursedBurnerTileEntity;
import com.Polarice3.Goety.common.tileentities.CursedKilnTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3f;

public class CursedKilnTileEntityRenderer extends TileEntityRenderer<CursedKilnTileEntity> {
    public CursedKilnTileEntityRenderer(TileEntityRendererDispatcher p_i226007_1_) {
        super(p_i226007_1_);
    }

    public void render(CursedKilnTileEntity pBlockEntity, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pCombinedLight, int pCombinedOverlay) {
        NonNullList<ItemStack> nonnulllist = pBlockEntity.getItems();
        Minecraft minecraft = Minecraft.getInstance();
        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = nonnulllist.get(i);
            if (!itemstack.isEmpty()){
                pMatrixStack.pushPose();
                pMatrixStack.translate(0.5F, 0.5F, 0.5F);
                pMatrixStack.scale(1.0F, 1.0F, 1.0F);
                assert minecraft.level != null;
                pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(3 * (minecraft.level.getGameTime() % 360 + pPartialTicks)));
                minecraft.getItemRenderer().renderStatic(itemstack, ItemCameraTransforms.TransformType.GROUND, pCombinedLight, pCombinedOverlay, pMatrixStack, pBuffer);
                pMatrixStack.popPose();
            }
        }
    }

}
