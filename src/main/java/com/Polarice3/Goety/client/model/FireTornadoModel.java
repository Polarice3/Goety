package com.Polarice3.Goety.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class FireTornadoModel<T extends Entity> extends EntityModel<T> {
    private final ModelRenderer Gust;
    private final ModelRenderer base;
    private final ModelRenderer gust1;
    private final ModelRenderer gust2;
    private final ModelRenderer gust3;
    private final ModelRenderer wind;

    public FireTornadoModel() {
        texWidth = 128;
        texHeight = 128;

        Gust = new ModelRenderer(this);
        Gust.setPos(0.0F, 24.0F, 0.0F);


        base = new ModelRenderer(this);
        base.setPos(0.0F, -2.0F, 0.0F);
        Gust.addChild(base);
        base.texOffs(0, 0).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 6.0F, 4.0F, 0.0F, false);

        gust1 = new ModelRenderer(this);
        gust1.setPos(0.0F, -7.0F, 0.0F);
        Gust.addChild(gust1);
        gust1.texOffs(65, 26).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 6.0F, 8.0F, 0.0F, false);

        gust2 = new ModelRenderer(this);
        gust2.setPos(0.0F, -13.0F, 0.0F);
        Gust.addChild(gust2);
        gust2.texOffs(65, 0).addBox(-6.0F, -4.0F, -6.0F, 12.0F, 6.0F, 12.0F, 0.0F, false);

        gust3 = new ModelRenderer(this);
        gust3.setPos(0.0F, -14.0F, 0.0F);
        Gust.addChild(gust3);
        gust3.texOffs(0, 40).addBox(-8.0F, -9.0F, -8.0F, 16.0F, 6.0F, 16.0F, 0.0F, false);

        wind = new ModelRenderer(this);
        wind.setPos(0.0F, 0.0F, 0.0F);
        Gust.addChild(wind);
        wind.texOffs(0, 0).addBox(-8.0F, -24.0F, -8.0F, 16.0F, 24.0F, 16.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        base.yRot -= ageInTicks;
        gust1.yRot -= base.yRot + ageInTicks * 0.3F;
        gust2.yRot -= base.yRot + gust1.yRot + ageInTicks * 0.2F;
        gust3.yRot -= base.yRot + gust1.yRot + gust2.yRot + ageInTicks * 0.34F;
        wind.yRot += ageInTicks * 0.5F;
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Gust.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
