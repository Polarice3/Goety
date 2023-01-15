package com.Polarice3.Goety.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GhostFireModel<T extends Entity> extends SegmentedModel<T> {
    private final ModelRenderer frostFire;

    public GhostFireModel() {
        texWidth = 32;
        texHeight = 16;

        frostFire = new ModelRenderer(this);
        frostFire.setPos(0.0F, 16.0F, 0.0F);
        frostFire.texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);
        frostFire.texOffs(0, 0).addBox(-8.0F, -8.0F, 8.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);

        ModelRenderer cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(7.0F, 0.0F, 0.0F);
        frostFire.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, -1.5708F, 0.0F);
        cube_r1.texOffs(0, 0).addBox(-8.0F, -8.0F, 15.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);
        cube_r1.texOffs(0, 0).addBox(-8.0F, -8.0F, -1.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);

        ModelRenderer middle = new ModelRenderer(this);
        middle.setPos(0.0F, 0.4567F, 0.0F);
        frostFire.addChild(middle);

        ModelRenderer cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(0.0F, 5.5433F, 0.0F);
        middle.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.3927F, 0.0F, 0.0F);
        cube_r2.texOffs(0, 0).addBox(-8.0F, -14.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);

        ModelRenderer cube_r3 = new ModelRenderer(this);
        cube_r3.setPos(0.0F, 5.5433F, 0.0F);
        middle.addChild(cube_r3);
        setRotationAngle(cube_r3, -0.3927F, 0.0F, 0.0F);
        cube_r3.texOffs(0, 0).addBox(-8.0F, -14.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);

        ModelRenderer cube_r4 = new ModelRenderer(this);
        cube_r4.setPos(0.0F, 5.5433F, 0.0F);
        middle.addChild(cube_r4);
        setRotationAngle(cube_r4, 0.0F, -1.5708F, 0.3927F);
        cube_r4.texOffs(0, 0).addBox(-8.0F, -14.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);

        ModelRenderer cube_r5 = new ModelRenderer(this);
        cube_r5.setPos(0.0F, 5.5433F, 0.0F);
        middle.addChild(cube_r5);
        setRotationAngle(cube_r5, 0.0F, -1.5708F, -0.3927F);
        cube_r5.texOffs(0, 0).addBox(-8.0F, -14.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        frostFire.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.frostFire);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
