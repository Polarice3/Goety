package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.bosses.PenanceEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class PenanceModel<T extends PenanceEntity> extends SegmentedModel<T> {

    private final ModelRenderer all;
    private final ModelRenderer Body;
    private final ModelRenderer Leg1A;
    private final ModelRenderer Leg2A;
    private final ModelRenderer Leg3A;
    private final ModelRenderer Leg4A;
    private final ModelRenderer Neck;
    private final ModelRenderer Head;
    private final ModelRenderer lowjaw;
    private final ModelRenderer rider;
    private final ModelRenderer Head1;
    private final ModelRenderer horns;
    private final ModelRenderer righthorns;
    private final ModelRenderer horn_r1;
    private final ModelRenderer horn_r2;
    private final ModelRenderer horn_r3;
    private final ModelRenderer lefthorns;
    private final ModelRenderer horn_r4;
    private final ModelRenderer horn_r5;
    private final ModelRenderer horn_r6;
    private final ModelRenderer fronthorns;
    private final ModelRenderer horn_r7;
    private final ModelRenderer horn_r8;
    private final ModelRenderer horn_r9;
    private final ModelRenderer Body2;
    private final ModelRenderer RightArm;
    private final ModelRenderer spear;
    private final ModelRenderer LeftArm;

    public PenanceModel() {
        texWidth = 128;
        texHeight = 128;

        all = new ModelRenderer(this);
        all.setPos(0.0F, 24.0F, 0.0F);


        Body = new ModelRenderer(this);
        Body.setPos(0.0F, -13.0F, 9.0F);
        all.addChild(Body);
        Body.texOffs(0, 0).addBox(-5.0F, -8.0F, -20.0F, 10.0F, 10.0F, 22.0F, 0.0F, false);
        Body.texOffs(6, 84).addBox(-5.0F, -8.0F, -20.0F, 10.0F, 10.0F, 22.0F, 0.5F, false);

        Leg1A = new ModelRenderer(this);
        Leg1A.setPos(3.0F, -11.0F, 9.0F);
        all.addChild(Leg1A);
        Leg1A.texOffs(65, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F, false);
        Leg1A.texOffs(80, 64).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.5F, false);

        Leg2A = new ModelRenderer(this);
        Leg2A.setPos(-3.0F, -11.0F, 9.0F);
        all.addChild(Leg2A);
        Leg2A.texOffs(4, 34).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F, false);
        Leg2A.texOffs(80, 64).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.5F, false);

        Leg3A = new ModelRenderer(this);
        Leg3A.setPos(3.0F, -11.0F, -9.0F);
        all.addChild(Leg3A);
        Leg3A.texOffs(65, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F, false);
        Leg3A.texOffs(80, 64).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.5F, false);

        Leg4A = new ModelRenderer(this);
        Leg4A.setPos(-3.0F, -11.0F, -9.0F);
        all.addChild(Leg4A);
        Leg4A.texOffs(4, 34).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.0F, false);
        Leg4A.texOffs(80, 64).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, 0.5F, false);

        Neck = new ModelRenderer(this);
        Neck.setPos(0.0F, -17.0F, -8.0F);
        all.addChild(Neck);
        setRotationAngle(Neck, 0.5236F, 0.0F, 0.0F);
        Neck.texOffs(43, 0).addBox(-2.0F, -11.0F, -3.0F, 4.0F, 12.0F, 7.0F, 0.0F, false);
        Neck.texOffs(81, 43).addBox(-2.0F, -11.0F, -3.0F, 4.0F, 12.0F, 7.0F, 0.5F, false);
        Neck.texOffs(16, 67).addBox(-1.0F, -16.0F, 4.0F, 2.0F, 16.0F, 2.0F, 0.0F, false);
        Neck.texOffs(4, 68).addBox(-1.0F, -16.0F, 5.0F, 2.0F, 16.0F, 2.0F, 0.5F, false);

        Head = new ModelRenderer(this);
        Head.setPos(0.0F, -11.0F, 3.0F);
        Neck.addChild(Head);
        Head.texOffs(58, 26).addBox(-3.0F, -5.0F, -6.0F, 6.0F, 5.0F, 7.0F, 0.0F, false);
        Head.texOffs(95, 34).addBox(-3.0F, -5.0F, -6.0F, 6.0F, 5.0F, 7.0F, 0.5F, false);
        Head.texOffs(50, 55).addBox(-2.0F, -5.0F, -11.0F, 4.0F, 5.0F, 5.0F, 0.0F, false);
        Head.texOffs(58, 65).addBox(-2.0F, -5.0F, -11.0F, 4.0F, 5.0F, 5.0F, 0.2F, false);

        lowjaw = new ModelRenderer(this);
        lowjaw.setPos(0.0F, -1.5F, -5.5F);
        Head.addChild(lowjaw);
        lowjaw.texOffs(68, 55).addBox(-2.0F, -3.5F, -5.5F, 4.0F, 5.0F, 5.0F, 0.0F, false);

        rider = new ModelRenderer(this);
        rider.setPos(0.0F, 0.0F, 0.0F);
        all.addChild(rider);


        Head1 = new ModelRenderer(this);
        Head1.setPos(0.0F, -37.0F, 0.0F);
        rider.addChild(Head1);
        Head1.texOffs(33, 33).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        Head1.texOffs(77, 17).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.5F, false);

        horns = new ModelRenderer(this);
        horns.setPos(0.0F, 0.0F, 0.0F);
        Head1.addChild(horns);


        righthorns = new ModelRenderer(this);
        righthorns.setPos(0.0F, 0.0F, 0.0F);
        horns.addChild(righthorns);


        horn_r1 = new ModelRenderer(this);
        horn_r1.setPos(0.0F, 0.0F, 0.0F);
        righthorns.addChild(horn_r1);
        setRotationAngle(horn_r1, 0.0F, 0.0F, 0.6545F);
        horn_r1.texOffs(0, 9).addBox(-13.0F, 1.0F, -2.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);

        horn_r2 = new ModelRenderer(this);
        horn_r2.setPos(0.0F, 0.0F, 0.0F);
        righthorns.addChild(horn_r2);
        setRotationAngle(horn_r2, 0.0F, 0.0F, 0.2618F);
        horn_r2.texOffs(0, 9).addBox(-11.0F, -3.0F, -2.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);

        horn_r3 = new ModelRenderer(this);
        horn_r3.setPos(0.0F, 0.0F, 0.0F);
        righthorns.addChild(horn_r3);
        setRotationAngle(horn_r3, 0.0F, 0.0F, 0.1309F);
        horn_r3.texOffs(0, 0).addBox(-8.0F, -4.0F, -3.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        lefthorns = new ModelRenderer(this);
        lefthorns.setPos(0.0F, 0.0F, 0.0F);
        horns.addChild(lefthorns);


        horn_r4 = new ModelRenderer(this);
        horn_r4.setPos(0.0F, 0.0F, 0.0F);
        lefthorns.addChild(horn_r4);
        setRotationAngle(horn_r4, 0.0F, 0.0F, -0.6545F);
        horn_r4.texOffs(0, 9).addBox(10.0F, 1.0F, -2.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);

        horn_r5 = new ModelRenderer(this);
        horn_r5.setPos(0.0F, 0.0F, 0.0F);
        lefthorns.addChild(horn_r5);
        setRotationAngle(horn_r5, 0.0F, 0.0F, -0.2618F);
        horn_r5.texOffs(0, 9).addBox(8.0F, -3.0F, -2.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);

        horn_r6 = new ModelRenderer(this);
        horn_r6.setPos(0.0F, 0.0F, 0.0F);
        lefthorns.addChild(horn_r6);
        setRotationAngle(horn_r6, 0.0F, 0.0F, -0.1309F);
        horn_r6.texOffs(0, 0).addBox(4.0F, -4.0F, -3.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        fronthorns = new ModelRenderer(this);
        fronthorns.setPos(1.0F, 0.0F, -1.0F);
        horns.addChild(fronthorns);
        setRotationAngle(fronthorns, -1.5708F, 1.0472F, -1.5708F);


        horn_r7 = new ModelRenderer(this);
        horn_r7.setPos(0.0F, 0.0F, 0.0F);
        fronthorns.addChild(horn_r7);
        setRotationAngle(horn_r7, 0.0F, 0.0F, -0.6545F);
        horn_r7.texOffs(0, 9).addBox(10.0F, 1.0F, -2.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);

        horn_r8 = new ModelRenderer(this);
        horn_r8.setPos(0.0F, 0.0F, 0.0F);
        fronthorns.addChild(horn_r8);
        setRotationAngle(horn_r8, 0.0F, 0.0F, -0.2618F);
        horn_r8.texOffs(0, 9).addBox(8.0F, -3.0F, -2.0F, 3.0F, 3.0F, 2.0F, 0.0F, false);

        horn_r9 = new ModelRenderer(this);
        horn_r9.setPos(0.0F, 0.0F, 0.0F);
        fronthorns.addChild(horn_r9);
        setRotationAngle(horn_r9, 0.0F, 0.0F, -0.1309F);
        horn_r9.texOffs(0, 0).addBox(2.0F, -4.0F, -3.0F, 6.0F, 4.0F, 4.0F, 0.0F, false);

        Body2 = new ModelRenderer(this);
        Body2.setPos(0.0F, -33.0F, 0.0F);
        rider.addChild(Body2);
        Body2.texOffs(25, 50).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        Body2.texOffs(0, 50).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.25F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setPos(-5.0F, -31.0F, 1.0F);
        rider.addChild(RightArm);
        RightArm.texOffs(80, 0).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);
        RightArm.texOffs(25, 66).addBox(-3.0F, -2.0F, -3.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);

        spear = new ModelRenderer(this);
        spear.setPos(-0.5F, 9.5F, -11.0F);
        RightArm.addChild(spear);
        spear.texOffs(62, 72).addBox(-0.5F, -0.5F, -10.0F, 1.0F, 1.0F, 32.0F, 0.0F, false);
        spear.texOffs(107, 62).addBox(-0.5F, -0.5F, -15.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
        spear.texOffs(108, 69).addBox(-1.5F, -1.5F, -11.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        spear.texOffs(107, 61).addBox(0.0F, -1.0F, -14.0F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        spear.texOffs(107, 61).addBox(0.0F, 0.0F, -14.0F, 0.0F, 1.0F, 3.0F, 0.0F, false);
        spear.texOffs(107, 61).addBox(-1.0F, 0.0F, -14.0F, 1.0F, 0.0F, 3.0F, 0.0F, false);
        spear.texOffs(107, 61).addBox(0.0F, 0.0F, -14.0F, 1.0F, 0.0F, 3.0F, 0.0F, false);

        LeftArm = new ModelRenderer(this);
        LeftArm.setPos(5.0F, -31.0F, 1.0F);
        rider.addChild(LeftArm);
        LeftArm.texOffs(80, 0).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 12.0F, 2.0F, 0.0F, false);
        LeftArm.texOffs(41, 66).addBox(-1.0F, -2.0F, -3.0F, 4.0F, 12.0F, 4.0F, 0.25F, false);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.Head1.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.Head1.xRot = headPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        all.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public Iterable<ModelRenderer> headParts() {
        return ImmutableList.of(this.Head);
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.Body, this.LeftArm, this.RightArm, this.Head1, this.Leg1A, this.Leg2A, this.Leg3A, this.Leg4A, this.Head);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public void prepareMobModel(T entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
        int i = entityIn.func_213684_dX();
        int j = entityIn.func_213687_eg();
        float f = MathHelper.rotLerp(entityIn.yBodyRotO, entityIn.yBodyRot, partialTick);
        float f1 = MathHelper.rotLerp(entityIn.yHeadRotO, entityIn.yHeadRot, partialTick);
        float f2 = MathHelper.lerp(partialTick, entityIn.xRotO, entityIn.xRot);
        float f3 = f1 - f;
        float f4 = f2 * ((float)Math.PI / 180F);
        if (f3 > 20.0F) {
            f3 = 20.0F;
        }

        if (f3 < -20.0F) {
            f3 = -20.0F;
        }

        if (limbSwingAmount > 0.2F) {
            f4 += MathHelper.cos(limbSwing * 0.4F) * 0.15F * limbSwingAmount;
        }

        float f5 = 0;
        float f6 = entityIn.getRearingAmount(partialTick);
        float f7 = 1.0F - f6;
        float f8 = 0;
        float f9 = (float)entityIn.tickCount + partialTick;
/*        this.Head.y = -11.0F;
        this.Head.z = -12.0F;*/
        this.Body.xRot = 0.0F;
        this.Head.xRot = ((float)Math.PI / 6F) + f4;
        this.Head.yRot = f3 * ((float)Math.PI / 180F);
        float f10 = entityIn.isInWater() ? 0.2F : 1.0F;
        float f11 = MathHelper.cos(f10 * limbSwing * 0.6662F + (float)Math.PI);
        float f12 = f11 * 0.8F * limbSwingAmount;
        float f13 = (1.0F - Math.max(f6, f5)) * (((float)Math.PI / 6F) + f4 + f8 * MathHelper.sin(f9) * 0.05F);
        this.Head.xRot = f6 * (0.2617994F + f4) + f5 * (2.1816616F + MathHelper.sin(f9) * 0.05F) + f13;
        this.Head.yRot = f6 * f3 * ((float)Math.PI / 180F) + (1.0F - Math.max(f6, f5)) * this.Head.yRot;
        this.Head.y = f6 * -4.0F + f5 * 11.0F + (1.0F - Math.max(f6, f5)) * this.Head.y;
        this.Head.z = f6 * -4.0F + f5 * -12.0F + (1.0F - Math.max(f6, f5)) * this.Head.z;
        this.Body.xRot = f6 * (-(float)Math.PI / 4F) + f7 * this.Body.xRot;
        float f14 = 0.2617994F * f6;
        float f15 = MathHelper.cos(f9 * 0.6F + (float)Math.PI);
        this.Leg3A.y = -10.0F * f6 + 14.0F * f7;
        this.Leg3A.z = -6.0F * f6 - 10.0F * f7;
        this.Leg4A.y = this.Leg3A.y;
        this.Leg4A.z = this.Leg3A.z;
        float f16 = ((-(float)Math.PI / 3F) + f15) * f6 + f12 * f7;
        float f17 = ((-(float)Math.PI / 3F) - f15) * f6 - f12 * f7;
        this.Leg1A.xRot = f14 - f11 * 0.5F * limbSwingAmount * f7;
        this.Leg2A.xRot = f14 + f11 * 0.5F * limbSwingAmount * f7;
        this.Leg3A.xRot = f16;
        this.Leg4A.xRot = f17;

        boolean flag = i > 0;
        if (flag) {
            double d0 = (double)i / 40.0D;
            this.Head.x = (float)Math.sin(d0 * 10.0D) * 3.0F;
        } else if (j > 0) {
            float f18 = MathHelper.sin(((float)(20 - j) - partialTick) / 20.0F * (float)Math.PI * 0.25F);
            this.lowjaw.xRot = ((float)Math.PI / 2F) * f18;
        }
    }
}
