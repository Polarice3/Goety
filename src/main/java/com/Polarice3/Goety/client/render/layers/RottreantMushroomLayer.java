package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.client.model.RotTreeModel;
import com.Polarice3.Goety.common.entities.ally.RottreantEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.math.vector.Vector3f;

public class RottreantMushroomLayer extends LayerRenderer<RottreantEntity, RotTreeModel> {
    public RottreantMushroomLayer(IEntityRenderer<RottreantEntity, RotTreeModel> p_i50919_1_) {
        super(p_i50919_1_);
    }

    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, RottreantEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity.isReadyHarvest() && !pLivingEntity.isInvisible()) {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getInstance().getBlockRenderer();
            BlockState redMush = Blocks.RED_MUSHROOM.defaultBlockState();
            BlockState brownMush = Blocks.BROWN_MUSHROOM.defaultBlockState();
            int i = LivingRenderer.getOverlayCoords(pLivingEntity, 0.0F);
            pMatrixStack.pushPose();
            pMatrixStack.translate(-0.5F, -1.0F, 0.25D);
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(-32.5F));
            pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(15.0F));
            pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
            pMatrixStack.translate(-0.5D, -0.5D, -0.5D);
            blockrendererdispatcher.renderSingleBlock(redMush, pMatrixStack, pBuffer, pPackedLight, i);
            pMatrixStack.popPose();
            pMatrixStack.pushPose();
            pMatrixStack.translate(-0.05F, -0.5F, 0.5D);
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(-95.5F));
            pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(15.0F));
            pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
            pMatrixStack.translate(-0.5D, -0.5D, -0.5D);
            blockrendererdispatcher.renderSingleBlock(redMush, pMatrixStack, pBuffer, pPackedLight, i);
            pMatrixStack.popPose();
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.5F, -1.0F, 0.25D);
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(-45.0F));
            pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(-30.0F));
            pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(8.5F));
            pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
            pMatrixStack.translate(-0.5D, -0.5D, -0.5D);
            blockrendererdispatcher.renderSingleBlock(brownMush, pMatrixStack, pBuffer, pPackedLight, i);
            pMatrixStack.popPose();
            pMatrixStack.pushPose();
            pMatrixStack.translate(0.15F, 0.0F, 0.5D);
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(-100.0F));
            pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(15.0F));
            pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
            pMatrixStack.translate(-0.5D, -0.5D, -0.5D);
            blockrendererdispatcher.renderSingleBlock(brownMush, pMatrixStack, pBuffer, pPackedLight, i);
            pMatrixStack.popPose();
        }
    }
}
