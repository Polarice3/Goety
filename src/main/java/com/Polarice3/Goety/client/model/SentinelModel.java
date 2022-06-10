package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.hostile.dead.SentinelEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class SentinelModel extends SegmentedModel<SentinelEntity> {
    private final ModelRenderer body;
    private final ModelRenderer body_r1;
    private final ModelRenderer head;
    private final ModelRenderer lowerjaw;
    private final ModelRenderer arm0;
    private final ModelRenderer arm1_r1;
    private final ModelRenderer arm1;
    private final ModelRenderer arm1_r2;
    private final ModelRenderer leg0;
    private final ModelRenderer leg0_r1;
    private final ModelRenderer leg1;
    private final ModelRenderer leg1_r1;

    public SentinelModel() {
        texWidth = 128;
        texHeight = 128;

        body = new ModelRenderer(this);
        body.setPos(0.0F, -13.0F, 3.0F);
        body.texOffs(57, 26).addBox(-4.5F, 5.0F, -3.0F, 9.0F, 8.0F, 6.0F, 0.5F, false);

        body_r1 = new ModelRenderer(this);
        body_r1.setPos(0.0F, 29.0F, 0.0F);
        body.addChild(body_r1);
        setRotationAngle(body_r1, 0.2182F, 0.0F, 0.0F);
        body_r1.texOffs(0, 0).addBox(-10.0F, -33.0F, 0.0F, 20.0F, 12.0F, 11.0F, 0.0F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 1.0F, -5.0F);
        body.addChild(head);
        head.texOffs(0, 50).addBox(-4.0F, -14.0F, -3.5F, 8.0F, 7.0F, 8.0F, 0.0F, false);

        lowerjaw = new ModelRenderer(this);
        lowerjaw.setPos(0.0F, 0.0F, 0.0F);
        head.addChild(lowerjaw);
        lowerjaw.texOffs(33, 53).addBox(-5.0F, -7.0F, -5.0F, 10.0F, 3.0F, 10.0F, 0.0F, false);
        lowerjaw.texOffs(24, 50).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 2.0F, 0.0F, 0.0F, false);
        lowerjaw.texOffs(43, 40).addBox(5.0F, -9.0F, -5.0F, 0.0F, 2.0F, 10.0F, 0.0F, false);
        lowerjaw.texOffs(43, 40).addBox(-5.0F, -9.0F, -5.0F, 0.0F, 2.0F, 10.0F, 0.0F, false);

        arm0 = new ModelRenderer(this);
        arm0.setPos(-1.0F, 0.0F, 0.0F);
        body.addChild(arm0);
        arm0.texOffs(88, 24).addBox(-18.0F, -6.0F, -6.0F, 10.0F, 6.0F, 10.0F, 0.0F, false);
        arm0.texOffs(68, 0).addBox(-17.0F, -4.5F, -5.0F, 8.0F, 15.0F, 8.0F, 0.0F, false);

        arm1_r1 = new ModelRenderer(this);
        arm1_r1.setPos(0.0F, 29.0F, 0.0F);
        arm0.addChild(arm1_r1);
        setRotationAngle(arm1_r1, -0.4363F, 0.0F, 0.0F);
        arm1_r1.texOffs(100, 0).addBox(-16.0F, -19.5F, -14.0F, 6.0F, 15.0F, 8.0F, 0.0F, false);

        arm1 = new ModelRenderer(this);
        arm1.setPos(1.0F, 0.0F, 0.0F);
        body.addChild(arm1);
        arm1.texOffs(88, 24).addBox(8.0F, -6.0F, -5.0F, 10.0F, 6.0F, 10.0F, 0.0F, false);
        arm1.texOffs(68, 0).addBox(9.0F, -4.5F, -4.0F, 8.0F, 15.0F, 8.0F, 0.0F, false);

        arm1_r2 = new ModelRenderer(this);
        arm1_r2.setPos(0.0F, 29.0F, 0.0F);
        arm1.addChild(arm1_r2);
        setRotationAngle(arm1_r2, -0.4363F, 0.0F, 0.0F);
        arm1_r2.texOffs(100, 0).addBox(10.0F, -19.5F, -13.0F, 6.0F, 15.0F, 8.0F, 0.0F, false);

        leg0 = new ModelRenderer(this);
        leg0.setPos(-5.0F, 18.0F, 0.0F);
        body.addChild(leg0);
        leg0.texOffs(0, 23).addBox(-6.5F, 3.0F, -8.0F, 10.0F, 16.0F, 10.0F, 0.0F, false);

        leg0_r1 = new ModelRenderer(this);
        leg0_r1.setPos(4.0F, 13.0F, 0.0F);
        leg0.addChild(leg0_r1);
        setRotationAngle(leg0_r1, -0.2618F, 0.0F, 0.0F);
        leg0_r1.texOffs(66, 44).addBox(-9.5F, -17.0F, -8.0F, 8.0F, 10.0F, 6.0F, 0.0F, false);

        leg1 = new ModelRenderer(this);
        leg1.setPos(6.0F, 18.0F, 1.0F);
        body.addChild(leg1);
        leg1.texOffs(0, 23).addBox(-5.5F, 3.0F, -9.0F, 10.0F, 16.0F, 10.0F, 0.0F, false);

        leg1_r1 = new ModelRenderer(this);
        leg1_r1.setPos(-5.0F, 13.0F, 0.0F);
        leg1.addChild(leg1_r1);
        setRotationAngle(leg1_r1, -0.2618F, 0.0F, 0.0F);
        leg1_r1.texOffs(66, 44).addBox(0.5F, -17.0F, -9.0F, 8.0F, 10.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(SentinelEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.leg1.xRot = -1.5F * MathHelper.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.leg0.xRot = 1.5F * MathHelper.triangleWave(limbSwing, 13.0F) * limbSwingAmount;
        this.leg1.yRot = 0.0F;
        this.leg0.yRot = 0.0F;    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void prepareMobModel(SentinelEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        int i = entityIn.getAttackTimer();
        if (i > 0) {
            this.arm0.xRot = -2.0F + 1.5F * MathHelper.triangleWave((float)i - partialTick, 10.0F);
            this.arm1.xRot = -2.0F + 1.5F * MathHelper.triangleWave((float)i - partialTick, 10.0F);
        } else {
            this.arm0.xRot = (-0.2F + 1.5F * MathHelper.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
            this.arm1.xRot = (-0.2F - 1.5F * MathHelper.triangleWave(limbSwing, 13.0F)) * limbSwingAmount;
        }

    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.head, this.body, this.leg0, this.leg1, this.arm0, this.arm1);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
