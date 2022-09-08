package com.Polarice3.Goety.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class TallSkullModel extends GenericHeadModel {
    private final ModelRenderer head;

    public TallSkullModel(float p_i47227_2_) {
        super(0, 0, 64, 64);
        this.head = new ModelRenderer(this);
        this.head.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F);

        ModelRenderer modelrenderer = new ModelRenderer(this);
        modelrenderer.setPos(0.0F, p_i47227_2_ - 2.0F, 0.0F);
        modelrenderer.texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F);
        this.head.addChild(modelrenderer);
    }

    public void setupAnim(float p_225603_1_, float p_225603_2_, float p_225603_3_) {
        this.head.yRot = p_225603_2_ * ((float)Math.PI / 180F);
        this.head.xRot = p_225603_3_ * ((float)Math.PI / 180F);
    }

    public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.head.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }
}
