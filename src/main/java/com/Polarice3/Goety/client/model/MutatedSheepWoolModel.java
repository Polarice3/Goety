package com.Polarice3.Goety.client.model;


import com.Polarice3.Goety.common.entities.neutral.MutatedSheepEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class MutatedSheepWoolModel <T extends MutatedSheepEntity> extends QuadrupedModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer legbackright;
    private final ModelRenderer legbackleft;
    private final ModelRenderer legfrontright;
    private final ModelRenderer legfrontleft;

    public MutatedSheepWoolModel() {
        super(12, 0.0F, false, 8.0F, 4.0F, 2.0F, 2.0F, 24);
        texWidth = 64;
        texHeight = 64;

        body = new ModelRenderer(this);
        body.setPos(0.0F, 5.0F, 2.0F);
        body.texOffs(20, 8).addBox(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 12.0F, 2.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 6.0F, -8.0F);
        head.texOffs(0, 0).addBox(-3.0F, -1.0F, -6.0F, 6.0F, 6.0F, 8.0F, 1.0F, false);

        legbackright = new ModelRenderer(this);
        legbackright.setPos(-3.0F, 12.0F, 7.0F);
        legbackright.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        legbackleft = new ModelRenderer(this);
        legbackleft.setPos(3.0F, 12.0F, 7.0F);
        legbackleft.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, false);

        legfrontright = new ModelRenderer(this);
        legfrontright.setPos(-3.0F, 12.0F, -5.0F);
        legfrontright.texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, 1.0F, false);

        legfrontleft = new ModelRenderer(this);
        legfrontleft.setPos(3.0F, 12.0F, -5.0F);
        legfrontleft.texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, 1.0F, false);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legbackright.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legbackleft.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legfrontright.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        legfrontleft.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
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

    public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
        this.head.y = 6.0F + entityIn.getHeadEatPositionScale(partialTick) * 9.0F;
        this.head.xRot = entityIn.getHeadEatAngleScale(partialTick);
    }
}
