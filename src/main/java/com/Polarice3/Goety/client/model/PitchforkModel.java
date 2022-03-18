package com.Polarice3.Goety.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class PitchforkModel extends Model {
    private final ModelRenderer bone;
    private final ModelRenderer head;

    public PitchforkModel() {
        super(RenderType::entitySolid);
        texWidth = 32;
        texHeight = 32;

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 11.0F, 0.0F);
        bone.texOffs(0, 0).addBox(-0.5F, -12.0F, -0.5F, 1.0F, 25.0F, 1.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 14.0F, 0.0F);
        bone.addChild(head);
        head.texOffs(4, 0).addBox(-0.5F, -31.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        head.texOffs(4, 0).addBox(-2.5F, -31.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        head.texOffs(4, 0).addBox(1.5F, -31.0F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        head.texOffs(4, 4).addBox(-2.5F, -28.0F, -0.5F, 5.0F, 1.0F, 1.0F, 0.0F, false);
        head.texOffs(8, 0).addBox(-1.5F, -27.0F, -0.5F, 3.0F, 1.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(MatrixStack pMatrixStack, IVertexBuilder pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        bone.render(pMatrixStack, pBuffer, pPackedLight, pPackedOverlay);
    }
}
