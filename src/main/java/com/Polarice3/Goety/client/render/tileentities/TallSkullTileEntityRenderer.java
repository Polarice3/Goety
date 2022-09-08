package com.Polarice3.Goety.client.render.tileentities;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.TallSkullModel;
import com.Polarice3.Goety.common.blocks.TallSkullBlock;
import com.Polarice3.Goety.common.blocks.WallTallSkullBlock;
import com.Polarice3.Goety.common.tileentities.TallSkullTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class TallSkullTileEntityRenderer extends TileEntityRenderer<TallSkullTileEntity> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/skeletonvillagerminion.png");

    public TallSkullTileEntityRenderer(TileEntityRendererDispatcher p_i226015_1_) {
        super(p_i226015_1_);
    }

    public void render(TallSkullTileEntity pBlockEntity, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pCombinedLight, int pCombinedOverlay) {
        BlockState blockstate = pBlockEntity.getBlockState();
        boolean flag = blockstate.getBlock() instanceof WallTallSkullBlock;
        Direction direction = flag ? blockstate.getValue(WallTallSkullBlock.FACING) : null;
        float f1 = 22.5F * (float)(flag ? (2 + direction.get2DDataValue()) * 4 : blockstate.getValue(TallSkullBlock.ROTATION));
        renderSkull(direction, f1, pMatrixStack, pBuffer, pCombinedLight);
    }

    public static void renderSkull(@Nullable Direction p_228879_0_, float p_228879_1_, MatrixStack p_228879_5_, IRenderTypeBuffer p_228879_6_, int p_228879_7_) {
        TallSkullModel tallskullmodel = new TallSkullModel(0.0F);
        p_228879_5_.pushPose();
        if (p_228879_0_ == null) {
            p_228879_5_.translate(0.5D, 0.0D, 0.5D);
        } else {
            float f = 0.25F;
            p_228879_5_.translate((double)(0.5F - (float)p_228879_0_.getStepX() * f), f, (double)(0.5F - (float)p_228879_0_.getStepZ() * f));
        }

        p_228879_5_.scale(-1.0F, -1.0F, 1.0F);
        IVertexBuilder ivertexbuilder = p_228879_6_.getBuffer(RenderType.entityCutoutNoCullZOffset(TEXTURE));
        tallskullmodel.setupAnim(0, p_228879_1_, 0.0F);
        tallskullmodel.renderToBuffer(p_228879_5_, ivertexbuilder, p_228879_7_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        p_228879_5_.popPose();
    }
}
