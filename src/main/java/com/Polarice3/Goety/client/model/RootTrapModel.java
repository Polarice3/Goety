package com.Polarice3.Goety.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class RootTrapModel<T extends Entity> extends SegmentedModel<T> {
    private final ModelRenderer rootTrap;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;

    public RootTrapModel() {
        texWidth = 16;
        texHeight = 16;

        rootTrap = new ModelRenderer(this);
        rootTrap.setPos(0.0F, 24.0F, 0.0F);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(-4.0F, 0.0F, -4.0F);
        rootTrap.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.0873F, -3.1416F, -0.0873F);
        cube_r1.texOffs(-8, 0).addBox(-4.0F, -0.5F, -4.0F, 8.0F, 0.0F, 8.0F, 0.0F, true);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(4.0F, 0.0F, -4.0F);
        rootTrap.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.0873F, 3.1416F, 0.0873F);
        cube_r2.texOffs(-8, 8).addBox(-4.0F, -0.5F, -4.0F, 8.0F, 0.0F, 8.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(4.0F, 0.0F, 4.0F);
        rootTrap.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.0873F, 0.0F, 0.0873F);
        cube_r3.texOffs(-8, 0).addBox(-4.0F, -0.5F, -4.0F, 8.0F, 0.0F, 8.0F, 0.0F, true);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(-4.0F, 0.0F, 4.0F);
        rootTrap.addChild(cube_r4);
        setRotationAngle(cube_r4, -0.0873F, 0.0F, -0.0873F);
        cube_r4.texOffs(-8, 8).addBox(-4.0F, -0.5F, -4.0F, 8.0F, 0.0F, 8.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        rootTrap.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.rootTrap);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
