package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.hostile.ShadeEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class ShadeModel extends SegmentedModel<ShadeEntity> {
    private final ModelRenderer Ghost;
    private final ModelRenderer head;
    private final ModelRenderer hat;
    private final ModelRenderer RightArm;
    private final ModelRenderer LeftArm;
    private final ModelRenderer leg0;
    private final ModelRenderer body;
    private final ModelRenderer Ribs;
    private final ModelRenderer bone2;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;

    public ShadeModel() {
        texWidth = 64;
        texHeight = 64;

        Ghost = new ModelRenderer(this);
        Ghost.setPos(0.0F, 24.0F, 0.0F);

        head = new ModelRenderer(this);
        head.setPos(0.0F, -24.0F, 0.0F);
        Ghost.addChild(head);
        head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        hat = new ModelRenderer(this);
        hat.setPos(0.0F, 0.0F, 0.0F);
        head.addChild(hat);
        hat.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setPos(-5.0F, -22.0F, 0.0F);
        Ghost.addChild(RightArm);
        RightArm.texOffs(12, 18).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);
        RightArm.texOffs(0, 33).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setPos(5.0F, -22.0F, 0.0F);
        Ghost.addChild(LeftArm);
        LeftArm.texOffs(12, 18).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);
        LeftArm.texOffs(0, 33).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        leg0 = new ModelRenderer(this);
        leg0.setPos(0.0F, -12.0F, 0.0F);
        Ghost.addChild(leg0);
        leg0.texOffs(44, 16).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 12.0F, 4.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setPos(0.0F, -24.0F, 0.0F);
        Ghost.addChild(body);
        body.texOffs(20, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        body.texOffs(32, 32).addBox(-5.0F, 0.0F, -3.0F, 10.0F, 12.0F, 6.0F, 0.0F, false);

        Ribs = new ModelRenderer(this);
        Ribs.setPos(0.0F, 24.0F, 0.0F);
        body.addChild(Ribs);

        bone2 = new ModelRenderer(this);
        bone2.setPos(-2.0F, -22.0F, -2.5F);
        Ribs.addChild(bone2);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(4.0F, 0.0F, 0.0F);
        bone2.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, -0.7854F, 0.0F);
        cube_r1.texOffs(0, 22).addBox(-1.0F, 2.0F, -1.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        cube_r1.texOffs(0, 22).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(0.0F, 0.0F, 0.0F);
        bone2.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.7854F, 0.0F);
        cube_r2.texOffs(0, 22).addBox(-1.0F, 2.0F, -1.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        cube_r2.texOffs(0, 22).addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
    }

    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.Ghost, this.head, this.body, this.leg0, this.LeftArm, this.RightArm);
    }

    @Override
    public void setupAnim(ShadeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        float f = 1.0F;
        this.leg0.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.leg0.yRot = 0.0F;
        this.leg0.zRot = 0.0F;
        this.leg0.xRot += ((float)Math.PI / 5F);
        ModelHelper.animateZombieArms(this.LeftArm, this.RightArm, entity.isAggressive(), this.attackTime, ageInTicks);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Ghost.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
