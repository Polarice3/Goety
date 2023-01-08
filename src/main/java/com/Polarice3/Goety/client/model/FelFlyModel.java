package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.ally.FelFlyEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.AgeableModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class FelFlyModel <T extends FelFlyEntity> extends AgeableModel<T> {
    private final ModelRenderer fel_fly;
    private final ModelRenderer body;
    private final ModelRenderer body_r1;
    private final ModelRenderer head;
    private final ModelRenderer rightwing_bone;
    private final ModelRenderer leftwing_bone;
    private final ModelRenderer leg_front;
    private final ModelRenderer leg_mid;
    private final ModelRenderer leg_back;

    public FelFlyModel() {
        texWidth = 64;
        texHeight = 64;

        fel_fly = new ModelRenderer(this);
        fel_fly.setPos(0.5F, 19.0F, 0.0F);

        body = new ModelRenderer(this);
        body.setPos(0.0F, 0.0F, 0.0F);
        fel_fly.addChild(body);
        body.texOffs(0, 6).addBox(-1.5F, 0.0F, -4.0F, 3.0F, 3.0F, 7.0F, 0.0F, false);

        body_r1 = new ModelRenderer(this);
        body_r1.setPos(0.0F, 1.5F, 2.5F);
        body.addChild(body_r1);
        setRotationAngle(body_r1, -0.1745F, 0.0F, 0.0F);
        body_r1.texOffs(13, 7).addBox(-1.5F, -1.5F, -0.5F, 3.0F, 3.0F, 3.0F, 0.25F, false);

        head = new ModelRenderer(this);
        head.setPos(2.0F, 2.0F, 0.0F);
        fel_fly.addChild(head);
        setRotationAngle(head, -0.3491F, 0.0F, 0.0F);
        head.texOffs(0, 0).addBox(-4.5F, -1.0F, -6.0F, 5.0F, 4.0F, 2.0F, 0.0F, false);

        rightwing_bone = new ModelRenderer(this);
        rightwing_bone.setPos(-0.5F, 0.0F, -3.0F);
        fel_fly.addChild(rightwing_bone);
        setRotationAngle(rightwing_bone, 0.0F, -0.2618F, 0.0F);
        rightwing_bone.texOffs(0, 18).addBox(-9.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.0F, false);

        leftwing_bone = new ModelRenderer(this);
        leftwing_bone.setPos(0.5F, 0.0F, -3.0F);
        fel_fly.addChild(leftwing_bone);
        setRotationAngle(leftwing_bone, 0.0F, 0.2618F, 0.0F);
        leftwing_bone.texOffs(9, 24).addBox(0.0F, 0.0F, 0.0F, 9.0F, 0.0F, 6.0F, 0.0F, false);

        leg_front = new ModelRenderer(this);
        leg_front.setPos(1.5F, 3.0F, -2.0F);
        fel_fly.addChild(leg_front);
        setRotationAngle(leg_front, 0.2182F, 0.0F, 0.0F);
        leg_front.texOffs(26, 1).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, 0.0F, false);

        leg_mid = new ModelRenderer(this);
        leg_mid.setPos(0.5F, 3.0F, 0.0F);
        fel_fly.addChild(leg_mid);
        setRotationAngle(leg_mid, 0.2618F, 0.0F, 0.0F);
        leg_mid.texOffs(26, 1).addBox(-2.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, 0.0F, false);

        leg_back = new ModelRenderer(this);
        leg_back.setPos(1.5F, 3.0F, 2.0F);
        fel_fly.addChild(leg_back);
        setRotationAngle(leg_back, 0.5236F, 0.0F, 0.0F);
        leg_back.texOffs(26, 1).addBox(-3.0F, 0.0F, 0.0F, 3.0F, 2.0F, 0.0F, 0.0F, false);
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.rightwing_bone.xRot = 0.0F;
        this.fel_fly.xRot = 0.0F;
        this.fel_fly.y = 19.0F;
        boolean flag = pEntity.isOnGround() && pEntity.getDeltaMovement().lengthSqr() < 1.0E-7D;
        if (flag) {
            this.rightwing_bone.yRot = -0.2618F;
            this.rightwing_bone.zRot = 0.0F;
            this.leftwing_bone.xRot = 0.0F;
            this.leftwing_bone.yRot = 0.2618F;
            this.leftwing_bone.zRot = 0.0F;
            this.leg_front.xRot = 0.0F;
            this.leg_mid.xRot = 0.0F;
            this.leg_back.xRot = 0.0F;
        } else {
            float f = pAgeInTicks * 2.1F;
            this.rightwing_bone.yRot = 0.0F;
            this.rightwing_bone.zRot = MathHelper.cos(f) * (float)Math.PI * 0.15F;
            this.leftwing_bone.xRot = this.rightwing_bone.xRot;
            this.leftwing_bone.yRot = this.rightwing_bone.yRot;
            this.leftwing_bone.zRot = -this.rightwing_bone.zRot;
            this.leg_front.xRot = ((float)Math.PI / 4F);
            this.leg_mid.xRot = ((float)Math.PI / 4F);
            this.leg_back.xRot = ((float)Math.PI / 4F);
            this.fel_fly.xRot = 0.0F;
            this.fel_fly.yRot = 0.0F;
            this.fel_fly.zRot = 0.0F;
        }

        if (!pEntity.isAggressive()) {
            this.fel_fly.xRot = 0.0F;
            this.fel_fly.yRot = 0.0F;
            this.fel_fly.zRot = 0.0F;
            if (!flag) {
                float f1 = MathHelper.cos(pAgeInTicks * 0.18F);
                this.fel_fly.xRot = 0.1F + f1 * (float)Math.PI * 0.025F;
                this.leg_front.xRot = -f1 * (float)Math.PI * 0.1F + ((float)Math.PI / 8F);
                this.leg_back.xRot = -f1 * (float)Math.PI * 0.05F + ((float)Math.PI / 4F);
                this.fel_fly.y = 19.0F - MathHelper.cos(pAgeInTicks * 0.18F) * 0.9F;
            }
        }

    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    protected Iterable<ModelRenderer> headParts() {
        return ImmutableList.of();
    }

    protected Iterable<ModelRenderer> bodyParts() {
        return ImmutableList.of(this.fel_fly);
    }

}
