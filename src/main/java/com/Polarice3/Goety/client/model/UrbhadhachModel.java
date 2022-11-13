package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.hostile.UrbhadhachEntity;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

public class UrbhadhachModel<T extends UrbhadhachEntity> extends SegmentedModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer jaw;
    private final ModelRenderer leg0;
    private final ModelRenderer leg1;
    private final ModelRenderer leg2;
    private final ModelRenderer leg3;

    public UrbhadhachModel() {
        texWidth = 128;
        texHeight = 64;

        body = new ModelRenderer(this);
        body.setPos(-2.0F, 9.0F, 12.0F);
        setRotationAngle(body, 1.5708F, 0.0F, 0.0F);
        body.texOffs(0, 19).addBox(-5.0F, -13.0F, -7.0F, 14.0F, 14.0F, 11.0F, -0.25F, false);
        body.texOffs(39, 0).addBox(-4.0F, -25.0F, -7.0F, 12.0F, 12.0F, 10.0F, 0.25F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 10.0F, -16.0F);
        head.texOffs(0, 0).addBox(-3.5F, -3.0F, -3.0F, 7.0F, 8.0F, 7.0F, 0.0F, false);
        head.texOffs(26, 0).addBox(-4.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        head.texOffs(26, 0).addBox(2.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F, 0.0F, true);
        head.texOffs(0, 44).addBox(-2.5F, 1.0F, -6.0F, 5.0F, 3.0F, 3.0F, 0.25F, false);

        jaw = new ModelRenderer(this);
        jaw.setPos(0.0F, 3.25F, -2.5F);
        head.addChild(jaw);
        jaw.texOffs(0, 52).addBox(-2.5F, -0.25F, -3.5F, 5.0F, 2.0F, 3.0F, 0.0F, false);

        leg0 = new ModelRenderer(this);
        leg0.setPos(-4.5F, 14.0F, 6.0F);
        leg0.texOffs(50, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 8.0F, 0.0F, true);

        leg1 = new ModelRenderer(this);
        leg1.setPos(4.5F, 14.0F, 6.0F);
        leg1.texOffs(50, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 8.0F, 0.0F, false);

        leg2 = new ModelRenderer(this);
        leg2.setPos(-3.5F, 14.0F, -8.0F);
        leg2.texOffs(50, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 6.0F, 0.0F, true);

        leg3 = new ModelRenderer(this);
        leg3.setPos(3.5F, 14.0F, -8.0F);
        leg3.texOffs(50, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.body.xRot = ((float)Math.PI / 2F);
        this.leg0.xRot = MathHelper.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;
        this.leg1.xRot = MathHelper.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.leg2.xRot = MathHelper.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount;
        this.leg3.xRot = MathHelper.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount;

        float f = pAgeInTicks - (float)pEntity.tickCount;
        float f1 = pEntity.getStandingAnimationScale(f);
        f1 = f1 * f1;
        float f2 = 1.0F - f1;
        this.body.xRot = ((float)Math.PI / 2F) - f1 * (float)Math.PI * 0.35F;
        this.body.y = 9.0F * f2 + 11.0F * f1;
        this.leg2.y = 14.0F * f2 - 6.0F * f1;
        this.leg2.z = -8.0F * f2 - 4.0F * f1;
        this.leg2.xRot -= f1 * (float)Math.PI * 0.45F;
        this.leg3.y = this.leg2.y;
        this.leg3.z = this.leg2.z;
        this.leg3.xRot -= f1 * (float)Math.PI * 0.45F;
        this.head.y = 10.0F * f2 - 14.0F * f1;
        this.head.z = -16.0F * f2 - 3.0F * f1;

        this.head.xRot += f1 * (float)Math.PI * 0.15F;
    }

    public void prepareMobModel(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick) {
        int j = pEntity.getRoarTick();
        if (pEntity.isStanding()){
            this.jaw.xRot = 0.7854F;
        } else {
            if (j > 0) {
                float f7 = MathHelper.sin(((float)(20 - j) - pPartialTick) / 20.0F * (float)Math.PI * 0.25F);
                this.jaw.xRot = ((float)Math.PI / 2F) * f7;
            } else {
                this.jaw.xRot = 0.0F;
            }
        }
    }

    @Override
    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.head, this.body, this.leg0, this.leg1, this.leg2, this.leg3);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
