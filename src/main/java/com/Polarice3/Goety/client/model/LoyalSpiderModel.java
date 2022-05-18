package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.ally.LoyalSpiderEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class LoyalSpiderModel<T extends Entity> extends SegmentedModel<T> {
    private final ModelRenderer head;
    private final ModelRenderer mouth1;
    private final ModelRenderer mouth2;
    private final ModelRenderer body0;
    private final ModelRenderer body1;
    private final ModelRenderer leg0;
    private final ModelRenderer leg1;
    private final ModelRenderer leg2;
    private final ModelRenderer leg3;
    private final ModelRenderer leg4;
    private final ModelRenderer leg5;
    private final ModelRenderer leg6;
    private final ModelRenderer leg7;

    public LoyalSpiderModel() {
        texWidth = 64;
        texHeight = 32;

        head = new ModelRenderer(this);
        head.setPos(0.0F, 15.0F, -3.0F);
        head.texOffs(32, 4).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        mouth1 = new ModelRenderer(this);
        mouth1.setPos(0.0F, 0.0F, 0.0F);
        head.addChild(mouth1);
        mouth1.texOffs(32, 4).addBox(1.0F, 1.0F, -10.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);

        mouth2 = new ModelRenderer(this);
        mouth2.setPos(0.0F, 0.0F, 0.0F);
        head.addChild(mouth2);
        mouth2.texOffs(32, 4).addBox(-3.0F, 1.0F, -10.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        body0 = new ModelRenderer(this);
        body0.setPos(0.0F, 15.0F, 0.0F);
        body0.texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, 0.0F, false);

        body1 = new ModelRenderer(this);
        body1.setPos(0.0F, 13.0F, 7.0F);
        setRotationAngle(body1, 0.3491F, 0.0F, 0.0F);
        body1.texOffs(0, 12).addBox(-5.0F, -4.0F, -6.0F, 10.0F, 8.0F, 12.0F, 0.0F, false);

        leg0 = new ModelRenderer(this);
        leg0.setPos(-4.0F, 15.0F, 2.0F);
        setRotationAngle(leg0, 0.0F, 0.7854F, -0.7854F);
        leg0.texOffs(18, 0).addBox(-7.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        ModelRenderer leg1_r1 = new ModelRenderer(this);
        leg1_r1.setPos(-11.0F, 0.0F, 0.0F);
        leg0.addChild(leg1_r1);
        setRotationAngle(leg1_r1, 0.0F, -0.5236F, 0.0F);
        leg1_r1.texOffs(38, 0).addBox(-4.0F, -1.0F, -3.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        leg1 = new ModelRenderer(this);
        leg1.setPos(4.0F, 15.0F, 2.0F);
        setRotationAngle(leg1, 0.0F, -0.7854F, 0.7854F);
        leg1.texOffs(18, 0).addBox(0.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        ModelRenderer leg2_r1 = new ModelRenderer(this);
        leg2_r1.setPos(11.0F, 0.0F, 0.0F);
        leg1.addChild(leg2_r1);
        setRotationAngle(leg2_r1, 0.0F, 0.5236F, 0.0F);
        leg2_r1.texOffs(38, 0).addBox(-4.0F, -1.0F, -3.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        leg2 = new ModelRenderer(this);
        leg2.setPos(-4.0F, 15.0F, 1.0F);
        setRotationAngle(leg2, 0.0F, 0.2618F, -0.6109F);
        leg2.texOffs(18, 0).addBox(-7.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        ModelRenderer leg3_r1 = new ModelRenderer(this);
        leg3_r1.setPos(-11.0F, 0.0F, 0.0F);
        leg2.addChild(leg3_r1);
        setRotationAngle(leg3_r1, 0.0F, -0.2182F, 0.0F);
        leg3_r1.texOffs(38, 0).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        leg3 = new ModelRenderer(this);
        leg3.setPos(4.0F, 15.0F, 1.0F);
        setRotationAngle(leg3, 0.0F, -0.2618F, 0.6109F);
        leg3.texOffs(18, 0).addBox(-1.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        ModelRenderer leg4_r1 = new ModelRenderer(this);
        leg4_r1.setPos(11.0F, 0.0F, 0.0F);
        leg3.addChild(leg4_r1);
        setRotationAngle(leg4_r1, 0.0F, 0.2182F, 0.0F);
        leg4_r1.texOffs(38, 0).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        leg4 = new ModelRenderer(this);
        leg4.setPos(-4.0F, 15.0F, 0.0F);
        setRotationAngle(leg4, 0.0F, -0.2618F, -0.6109F);
        leg4.texOffs(18, 0).addBox(-7.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        ModelRenderer leg5_r1 = new ModelRenderer(this);
        leg5_r1.setPos(-11.0F, 0.0F, 0.0F);
        leg4.addChild(leg5_r1);
        setRotationAngle(leg5_r1, 0.0F, -0.2618F, 0.0F);
        leg5_r1.texOffs(38, 0).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        leg5 = new ModelRenderer(this);
        leg5.setPos(4.0F, 15.0F, 0.0F);
        setRotationAngle(leg5, 0.0F, 0.2618F, 0.6109F);
        leg5.texOffs(18, 0).addBox(0.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        ModelRenderer leg6_r1 = new ModelRenderer(this);
        leg6_r1.setPos(11.0F, 0.0F, 0.0F);
        leg5.addChild(leg6_r1);
        setRotationAngle(leg6_r1, 0.0F, 0.2618F, 0.0F);
        leg6_r1.texOffs(38, 0).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        leg6 = new ModelRenderer(this);
        leg6.setPos(-4.0F, 15.0F, -1.0F);
        setRotationAngle(leg6, 0.0F, -0.6109F, -0.7854F);
        leg6.texOffs(18, 0).addBox(-7.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        ModelRenderer leg7_r1 = new ModelRenderer(this);
        leg7_r1.setPos(-11.0F, 0.0F, 0.0F);
        leg6.addChild(leg7_r1);
        setRotationAngle(leg7_r1, 0.0F, -0.2182F, 0.0F);
        leg7_r1.texOffs(38, 0).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        leg7 = new ModelRenderer(this);
        leg7.setPos(4.0F, 15.0F, -1.0F);
        setRotationAngle(leg7, 0.0F, 0.6109F, 0.7854F);
        leg7.texOffs(18, 0).addBox(-1.0F, -1.0F, -1.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);

        ModelRenderer leg8_r1 = new ModelRenderer(this);
        leg8_r1.setPos(11.0F, 0.0F, 0.0F);
        leg7.addChild(leg8_r1);
        setRotationAngle(leg8_r1, 0.0F, 0.2182F, 0.0F);
        leg8_r1.texOffs(38, 0).addBox(-4.0F, -1.0F, -2.0F, 8.0F, 2.0F, 2.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch){
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
        float n = 0.01F * (float)(pEntity.getId() % 10);
        this.mouth1.xRot = MathHelper.sin((float)pEntity.tickCount * n) * 4.5F * ((float)Math.PI / 180F);
        this.mouth1.zRot = MathHelper.cos((float)pEntity.tickCount * n) * 2.5F * ((float)Math.PI / 180F);
        this.mouth2.xRot = -this.mouth1.xRot;
        this.mouth2.zRot = -this.mouth1.zRot;
        float f = ((float)Math.PI / 4F);
        this.leg0.zRot = -f;
        this.leg1.zRot = f;
        this.leg2.zRot = -0.58119464F;
        this.leg3.zRot = 0.58119464F;
        this.leg4.zRot = -0.58119464F;
        this.leg5.zRot = 0.58119464F;
        this.leg6.zRot = -f;
        this.leg7.zRot = f;
        float f1 = -0.0F;
        float f2 = ((float)Math.PI / 8F);
        this.leg0.yRot = f;
        this.leg1.yRot = -f;
        this.leg2.yRot = f2;
        this.leg3.yRot = -f2;
        this.leg4.yRot = -f2;
        this.leg5.yRot = f2;
        this.leg6.yRot = -f;
        this.leg7.yRot = f;
        float f3 = -(MathHelper.cos(pLimbSwing * 0.6662F * 2.0F + 0.0F) * 0.4F) * pLimbSwingAmount;
        float f4 = -(MathHelper.cos(pLimbSwing * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * pLimbSwingAmount;
        float f5 = -(MathHelper.cos(pLimbSwing * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * pLimbSwingAmount;
        float f6 = -(MathHelper.cos(pLimbSwing * 0.6662F * 2.0F + ((float)Math.PI * 1.5F)) * 0.4F) * pLimbSwingAmount;
        float f7 = Math.abs(MathHelper.sin(pLimbSwing * 0.6662F + 0.0F) * 0.4F) * pLimbSwingAmount;
        float f8 = Math.abs(MathHelper.sin(pLimbSwing * 0.6662F + (float)Math.PI) * 0.4F) * pLimbSwingAmount;
        float f9 = Math.abs(MathHelper.sin(pLimbSwing * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * pLimbSwingAmount;
        float f10 = Math.abs(MathHelper.sin(pLimbSwing * 0.6662F + ((float)Math.PI * 1.5F)) * 0.4F) * pLimbSwingAmount;
        this.leg0.yRot += f3;
        this.leg1.yRot += -f3;
        this.leg2.yRot += f4;
        this.leg3.yRot += -f4;
        this.leg4.yRot += f5;
        this.leg5.yRot += -f5;
        this.leg6.yRot += f6;
        this.leg7.yRot += -f6;
        this.leg0.zRot += f7;
        this.leg1.zRot += -f7;
        this.leg2.zRot += f8;
        this.leg3.zRot += -f8;
        this.leg4.zRot += f9;
        this.leg5.zRot += -f9;
        this.leg6.zRot += f10;
        this.leg7.zRot += -f10;
        if (pEntity instanceof LoyalSpiderEntity){
            if (((LoyalSpiderEntity) pEntity).isSitting()){
                this.leg0.yRot = ((float)Math.PI / 4F);
                this.leg1.yRot = (-(float)Math.PI / 4F);
                this.leg2.yRot = ((float)Math.PI / 8F);
                this.leg3.yRot = (-(float)Math.PI / 8F);
                this.leg4.yRot = (-(float)Math.PI / 8F);
                this.leg5.yRot = ((float)Math.PI / 8F);
                this.leg6.yRot = (-(float)Math.PI / 4F);
                this.leg7.yRot = ((float)Math.PI / 4F);
                this.leg0.zRot = 0;
                this.leg1.zRot = 0;
                this.leg2.zRot = 0;
                this.leg3.zRot = 0;
                this.leg4.zRot = 0;
                this.leg5.zRot = 0;
                this.leg6.zRot = 0;
                this.leg7.zRot = 0;
            }
        }
    }

    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.head, this.body0, this.body1, this.leg0, this.leg1, this.leg2, this.leg3, this.leg4, this.leg5, this.leg6, this.leg7);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
