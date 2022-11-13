package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.neutral.ISpewing;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class DredenModel<T extends LivingEntity> extends SegmentedModel<T> {
    private final ModelRenderer Ghost;
    private final ModelRenderer head;
    private final ModelRenderer RightArm;
    private final ModelRenderer LeftArm;
    private final ModelRenderer body;

    public DredenModel() {
        texWidth = 64;
        texHeight = 64;

        Ghost = new ModelRenderer(this);
        Ghost.setPos(0.0F, 24.0F, 0.0F);

        head = new ModelRenderer(this);
        head.setPos(0.0F, -23.0F, -2.0F);
        Ghost.addChild(head);
        head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        ModelRenderer hat = new ModelRenderer(this);
        hat.setPos(0.0F, 0.0F, 0.0F);
        head.addChild(hat);
        hat.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);

        ModelRenderer hat_r1 = new ModelRenderer(this);
        hat_r1.setPos(0.0F, -6.5F, 4.0F);
        hat.addChild(hat_r1);
        setRotationAngle(hat_r1, -0.4363F, 0.0F, 0.0F);
        hat_r1.texOffs(42, 60).addBox(-4.0F, -1.5F, 0.0F, 8.0F, 1.0F, 3.0F, 0.5F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setPos(-5.0F, -22.0F, 0.0F);
        Ghost.addChild(RightArm);
        RightArm.texOffs(12, 18).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);

        ModelRenderer robe2 = new ModelRenderer(this);
        robe2.setPos(0.0F, 0.0F, 0.0F);
        RightArm.addChild(robe2);
        robe2.texOffs(0, 33).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 14.0F, 4.0F, 0.0F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setPos(5.0F, -22.0F, 0.0F);
        Ghost.addChild(LeftArm);
        LeftArm.texOffs(12, 18).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);

        ModelRenderer robe = new ModelRenderer(this);
        robe.setPos(0.0F, 0.0F, 0.0F);
        LeftArm.addChild(robe);
        robe.texOffs(0, 33).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 14.0F, 4.0F, 0.0F, false);

        body = new ModelRenderer(this);
        body.setPos(0.0F, -24.0F, 0.0F);
        Ghost.addChild(body);
        body.texOffs(20, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        body.texOffs(32, 32).addBox(-5.0F, -1.0F, -3.0F, 10.0F, 20.0F, 6.0F, 0.0F, false);

        ModelRenderer body_r1 = new ModelRenderer(this);
        body_r1.setPos(0.0F, 15.0F, 0.0F);
        body.addChild(body_r1);
        setRotationAngle(body_r1, 0.4363F, 0.0F, 0.0F);
        body_r1.texOffs(0, 51).addBox(-5.0F, -3.0F, -2.0F, 10.0F, 7.0F, 6.0F, 0.0F, false);
    }

    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.Ghost, this.head, this.body, this.LeftArm, this.RightArm);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        float f = ageInTicks * 0.0025F;
        this.Ghost.y = MathHelper.sin(f * 40.0F) + 24.0F;
        if (entity instanceof ISpewing){
            animateArms(this.LeftArm, this.RightArm, ((ISpewing) entity).isSpewing(), this.attackTime, ageInTicks);
        }
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

    public static void animateArms(ModelRenderer leftArm, ModelRenderer rightArm, boolean spewing, float attackTime, float ageInTicks) {
        float f = MathHelper.sin(attackTime * (float)Math.PI);
        float f1 = MathHelper.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * (float)Math.PI);
        rightArm.zRot = 0.0F;
        leftArm.zRot = 0.0F;
        rightArm.yRot = -(0.1F - f * 0.6F);
        leftArm.yRot = 0.1F - f * 0.6F;
        float f2 = -(spewing ? 0.17F : 0.7854F);
        rightArm.xRot = f2;
        leftArm.xRot = f2;
        rightArm.xRot -= f * 1.2F - f1 * 0.4F;
        leftArm.xRot -= f * 1.2F - f1 * 0.4F;
        ModelHelper.bobArms(rightArm, leftArm, ageInTicks);
    }
}
