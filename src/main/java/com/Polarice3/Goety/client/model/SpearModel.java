package com.Polarice3.Goety.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SpearModel extends Model {
    private final ModelRenderer bone;
    private final ModelRenderer head;

    public SpearModel() {
        super(RenderType::entitySolid);
        texWidth = 32;
        texHeight = 32;

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 9.0F, 0.0F);
        bone.texOffs(0, 0).addBox(-0.5F, -12.0F, -0.5F, 1.0F, 25.0F, 1.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 15.0F, 0.0F);
        bone.addChild(head);
        head.texOffs(6, 2).addBox(0.0F, -30.0F, 0.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);
        head.texOffs(4, 0).addBox(-0.5F, -31.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        head.texOffs(4, 1).addBox(0.0F, -30.0F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F, false);
        head.texOffs(4, 1).addBox(0.0F, -30.0F, -1.0F, 0.0F, 3.0F, 1.0F, 0.0F, false);
        head.texOffs(6, 2).addBox(-1.0F, -30.0F, 0.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        bone.render(matrixStack, buffer, packedLight, packedOverlay);
    }

}
