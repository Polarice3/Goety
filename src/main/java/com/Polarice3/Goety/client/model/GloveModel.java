package com.Polarice3.Goety.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

import javax.annotation.Nonnull;

public class GloveModel extends BipedModel<LivingEntity> {
    public GloveModel() {
        super(1.0F);
        texWidth = 16;
        texHeight = 16;
        this.rightArm = new ModelRenderer(this, 0, 0);
        this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.rightArm.addBox(-3, -2, -2, 4, 12, 4, 0.5F);
        this.leftArm = new ModelRenderer(this, 0, 0);
        this.leftArm.mirror = true;
        this.leftArm.setPos(5.0F, 2.0F, 0.0F);
        this.leftArm.addBox(-1, -2, -2, 4, 12, 4, 0.5F);
    }

    @Override
    public void renderToBuffer(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder vertexBuilder,
                       int light, int overlay, float red, float green, float blue, float alpha) {
        this.rightArm.render(matrixStack, vertexBuilder, light, overlay);
        this.leftArm.render(matrixStack, vertexBuilder, light, overlay);
    }
}
