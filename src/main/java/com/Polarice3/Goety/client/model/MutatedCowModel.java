package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.neutral.MutatedCowEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class MutatedCowModel<T extends MutatedCowEntity> extends EntityModel<T> {
        private final ModelRenderer body;
        private final ModelRenderer head;
        private final ModelRenderer head2;
        private final ModelRenderer leg0;
        private final ModelRenderer leg1;
        private final ModelRenderer leg2;
        private final ModelRenderer leg3;

        public MutatedCowModel() {
            texWidth = 64;
            texHeight = 64;

            body = new ModelRenderer(this);
            body.setPos(0.0F, 5.0F, 2.0F);
            body.texOffs(0, 30).addBox(-8.0F, -10.0F, -7.0F, 16.0F, 18.0F, 16.0F, 0.0F, false);
            body.texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F, 0.0F, false);

            head = new ModelRenderer(this);
            head.setPos(0.0F, 7.0F, -8.0F);
            head.texOffs(0, 0).addBox(0.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F, 0.0F, false);
            head.texOffs(22, 0).addBox(-1.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
            head.texOffs(22, 0).addBox(8.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);

            head2 = new ModelRenderer(this);
            head2.setPos(-6.0F, 0.0F, 0.0F);
            head.addChild(head2);
            head2.texOffs(28, 0).addBox(-2.0F, -4.0F, -6.0F, 6.0F, 8.0F, 6.0F, 0.0F, false);
            head2.texOffs(50, 0).addBox(-2.0F, -5.0F, -4.0F, -1.0F, 3.0F, 1.0F, 0.0F, false);
            head2.texOffs(50, 0).addBox(5.0F, -5.0F, -4.0F, -1.0F, 3.0F, 1.0F, 0.0F, false);

            leg0 = new ModelRenderer(this);
            leg0.setPos(-4.0F, 12.0F, 7.0F);
            leg0.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

            leg1 = new ModelRenderer(this);
            leg1.setPos(4.0F, 12.0F, 7.0F);
            leg1.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

            leg2 = new ModelRenderer(this);
            leg2.setPos(-4.0F, 12.0F, -6.0F);
            leg2.texOffs(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

            leg3 = new ModelRenderer(this);
            leg3.setPos(4.0F, 12.0F, -6.0F);
            leg3.texOffs(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
        }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        leg0.render(matrixStack, buffer, packedLight, packedOverlay);
        leg1.render(matrixStack, buffer, packedLight, packedOverlay);
        leg2.render(matrixStack, buffer, packedLight, packedOverlay);
        leg3.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.body.xRot = ((float)Math.PI / 2F);
        this.leg0.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leg1.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg2.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leg3.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }
}
