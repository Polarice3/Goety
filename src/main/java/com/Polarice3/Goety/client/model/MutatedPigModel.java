package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.neutral.MutatedPigEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class MutatedPigModel<T extends MutatedPigEntity> extends EntityModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer legbackright;
    private final ModelRenderer legbackleft;
    private final ModelRenderer legfrontright;
    private final ModelRenderer legfrontleft;

    public MutatedPigModel() {
        texWidth = 64;
        texHeight = 64;

        body = new ModelRenderer(this);
        body.setPos(0.0F, 10.0F, 1.0F);
        setRotationAngle(body, 1.5708F, 0.0F, 0.0F);
        body.texOffs(0, 20).addBox(-6.0F, -6.0F, -2.0F, 12.0F, 12.0F, 8.0F, 0.0F, false);
        body.texOffs(0, 40).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 4.0F, 0.0F, false);


        head = new ModelRenderer(this);
        head.setPos(0.0F, 3.75F, 0.75F);
        head.texOffs(0, 0).addBox(-4.0F, -7.75F, -3.75F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        head.texOffs(16, 16).addBox(-2.0F, -3.75F, -4.75F, 4.0F, 3.0F, 1.0F, 0.0F, false);

        legbackright = new ModelRenderer(this);
        legbackright.setPos(-3.0F, 14.0F, 2.0F);
        legbackright.texOffs(48, 16).addBox(-2.0F, 3.0F, 2.0F, 4.0F, 6.0F, 4.0F, 1.0F, false);

        legbackleft = new ModelRenderer(this);
        legbackleft.setPos(3.0F, 14.0F, 2.0F);
        legbackleft.texOffs(48, 16).addBox(-2.0F, 3.0F, 2.0F, 4.0F, 6.0F, 4.0F, 1.0F, true);

        legfrontright = new ModelRenderer(this);
        legfrontright.setPos(-3.0F, 14.0F, 0.0F);
        legfrontright.texOffs(48, 16).addBox(-2.0F, 3.0F, -6.0F, 4.0F, 6.0F, 4.0F, 1.0F, false);

        legfrontleft = new ModelRenderer(this);
        legfrontleft.setPos(3.0F, 14.0F, 0.0F);
        legfrontleft.texOffs(48, 16).addBox(-2.0F, 3.0F, -6.0F, 4.0F, 6.0F, 4.0F, 1.0F, true);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        legbackright.render(matrixStack, buffer, packedLight, packedOverlay);
        legbackleft.render(matrixStack, buffer, packedLight, packedOverlay);
        legfrontright.render(matrixStack, buffer, packedLight, packedOverlay);
        legfrontleft.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.body.xRot = ((float)Math.PI / 2F);
        this.legbackright.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.legbackleft.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.legfrontright.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.legfrontleft.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
    }

}
