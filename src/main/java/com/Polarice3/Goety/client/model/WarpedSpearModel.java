package com.Polarice3.Goety.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class WarpedSpearModel extends Model {
    private final ModelRenderer bone;

    public WarpedSpearModel() {
        super(RenderType::entitySolid);
        texWidth = 32;
        texHeight = 32;

        bone = new ModelRenderer(this);
        bone.setPos(0.0F, 9.0F, 0.0F);
        bone.texOffs(0, 0).addBox(-0.5F, -12.0F, -0.5F, 1.0F, 25.0F, 1.0F, 0.0F, false);
        bone.texOffs(4, 0).addBox(-1.5F, -13.0F, -1.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);
        bone.texOffs(4, 4).addBox(-0.5F, -17.0F, -0.5F, 1.0F, 4.0F, 1.0F, 0.0F, false);
        bone.texOffs(4, 5).addBox(0.0F, -16.0F, 0.0F, 0.0F, 3.0F, 1.0F, 0.0F, false);
        bone.texOffs(4, 5).addBox(0.0F, -16.0F, -1.0F, 0.0F, 3.0F, 1.0F, 0.0F, false);
        bone.texOffs(6, 6).addBox(-1.0F, -16.0F, 0.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);
        bone.texOffs(6, 6).addBox(0.0F, -16.0F, 0.0F, 1.0F, 3.0F, 0.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        bone.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}
