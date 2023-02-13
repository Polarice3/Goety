package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.neutral.AbstractWraithEntity;
import com.Polarice3.Goety.utils.ModMathHelper;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class WraithModel<T extends LivingEntity> extends SegmentedModel<T> {
    private final ModelRenderer Ghost;
    private final ModelRenderer head;
    private final ModelRenderer RightArm;
    private final ModelRenderer LeftArm;
    private final ModelRenderer body;
    private final ModelRenderer leg;

    public WraithModel() {
        texWidth = 64;
        texHeight = 64;

        Ghost = new ModelRenderer(this);
        Ghost.setPos(0.0F, 24.0F, 0.0F);

        head = new ModelRenderer(this);
        head.setPos(0.0F, -24.0F, 0.0F);
        Ghost.addChild(head);
        head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        ModelRenderer hat = new ModelRenderer(this);
        hat.setPos(0.0F, 0.0F, 0.0F);
        head.addChild(hat);
        hat.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 13.0F, 8.0F, 0.5F, false);

        ModelRenderer hat_r1 = new ModelRenderer(this);
        hat_r1.setPos(0.0F, -6.5F, 4.0F);
        hat.addChild(hat_r1);
        setRotationAngle(hat_r1, -0.4363F, 0.0F, 0.0F);
        hat_r1.texOffs(42, 60).addBox(-4.0F, -1.5F, 0.0F, 8.0F, 1.0F, 3.0F, 0.5F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setPos(-5.0F, -22.0F, 0.0F);
        Ghost.addChild(RightArm);
        RightArm.texOffs(0, 16).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 12.0F, 1.0F, 0.0F, true);

        ModelRenderer robe2 = new ModelRenderer(this);
        robe2.setPos(0.0F, 0.0F, 0.0F);
        RightArm.addChild(robe2);
        robe2.texOffs(0, 33).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 14.0F, 4.0F, 0.0F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setPos(5.0F, -22.0F, 0.0F);
        Ghost.addChild(LeftArm);
        LeftArm.texOffs(0, 16).addBox(0.0F, -2.0F, -1.0F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        ModelRenderer robe = new ModelRenderer(this);
        robe.setPos(0.0F, 0.0F, 0.0F);
        LeftArm.addChild(robe);
        robe.texOffs(0, 33).addBox(-2.0F, -3.0F, -2.0F, 4.0F, 14.0F, 4.0F, 0.0F, true);

        body = new ModelRenderer(this);
        body.setPos(0.0F, -24.0F, 0.0F);
        Ghost.addChild(body);
        body.texOffs(8, 17).addBox(-3.0F, 0.0F, -5.0F, 6.0F, 12.0F, 4.0F, 0.0F, false);
        body.texOffs(32, 32).addBox(-5.0F, -1.0F, -3.0F, 10.0F, 20.0F, 6.0F, 0.0F, false);
        body.texOffs(0, 16).addBox(-0.5F, -1.0F, -1.5F, 1.0F, 12.0F, 1.0F, 0.0F, false);

        ModelRenderer body_r1 = new ModelRenderer(this);
        body_r1.setPos(0.0F, 15.0F, 0.0F);
        body.addChild(body_r1);
        setRotationAngle(body_r1, 0.4363F, 0.0F, 0.0F);
        body_r1.texOffs(0, 51).addBox(-5.0F, -2.0F, -2.0F, 10.0F, 7.0F, 6.0F, 0.0F, false);

        leg = new ModelRenderer(this);
        leg.setPos(0.0F, 10.0F, -1.0F);
        body.addChild(leg);
        leg.texOffs(60, 22).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 8.0F, 1.0F, 0.0F, false);
    }

    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.Ghost, this.head, this.body, this.LeftArm, this.RightArm);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch){
        float f = pAgeInTicks * 0.0025F;
        this.Ghost.y = MathHelper.sin(f * 40.0F) + 24.0F;
        this.body.xRot = 0.1745F + MathHelper.cos(pLimbSwing * 0.6662F) * 0.31F * pLimbSwingAmount;
        this.body.xRot += MathHelper.cos(pAgeInTicks * 0.09F) * 0.1F + 0.1F;
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.leg.xRot = MathHelper.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount / f;
        this.leg.yRot = 0.0F;
        this.leg.zRot = 0.0F;
        this.leg.xRot += ((float)Math.PI / 5F);
        if (pEntity instanceof AbstractWraithEntity){
            AbstractWraithEntity wraith = (AbstractWraithEntity) pEntity;
            if (wraith.isFiring()){
                float f7 = MathHelper.sin(((float)(wraith.firingTick - 20) - wraith.firingTick2) / 20.0F * (float)Math.PI * 0.25F);
                this.head.xRot = (((float)Math.PI) * f7) + 0.5F;
                this.RightArm.zRot = -((float)Math.PI) * f7;
                this.LeftArm.zRot = ((float)Math.PI) * f7;
            } else {
                float degrees;
                if (wraith.getLookControl().isHasWanted()){
                    degrees = 0.0F;
                } else if (pEntity.getDeltaMovement().x != 0 || pEntity.getDeltaMovement().z != 0){
                    degrees = ModMathHelper.modelDegrees(7.5F);
                } else {
                    degrees = ModMathHelper.modelDegrees(17.5F);
                }
                this.head.xRot = pHeadPitch * ((float)Math.PI / 180F) + degrees;
                animateArms(this.LeftArm, this.RightArm, 0, pAgeInTicks);
            }
        } else {
            this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
        }
    }

    public static void animateArms(ModelRenderer leftArm, ModelRenderer rightArm, float attackTime, float ageInTicks) {
        float f = MathHelper.sin(attackTime * (float)Math.PI);
        float f1 = MathHelper.sin((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * (float)Math.PI);
        rightArm.zRot = 0.0F;
        leftArm.zRot = 0.0F;
        rightArm.yRot = -(0.1F - f * 0.6F);
        leftArm.yRot = 0.1F - f * 0.6F;
        float f2 = -0.7854F;
        rightArm.xRot = f2;
        leftArm.xRot = f2;
        rightArm.xRot -= f * 1.2F - f1 * 0.4F;
        leftArm.xRot -= f * 1.2F - f1 * 0.4F;
        ModelHelper.bobArms(rightArm, leftArm, ageInTicks);
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
