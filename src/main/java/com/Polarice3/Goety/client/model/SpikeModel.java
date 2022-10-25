package com.Polarice3.Goety.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class SpikeModel<T extends Entity> extends SegmentedModel<T> {
    private final ModelRenderer base = new ModelRenderer(this, 0, 0);
    private final ModelRenderer upper_jaw;

    public SpikeModel() {
        texWidth = 64;
        texHeight = 32;

        this.base.setPos(-5.0F, 22.0F, -5.0F);
        this.base.addBox(0.0F, 0.0F, 0.0F, 10.0F, 12.0F, 10.0F);

        upper_jaw = new ModelRenderer(this);
        upper_jaw.setPos(0.0F, 22.0F, 0.0F);
        upper_jaw.texOffs(46, 0).addBox(-2.5F, -7.0F, -2.0F, 5.0F, 14.0F, 4.0F, 0.0F, false);
        upper_jaw.texOffs(56, 18).addBox(2.5F, -7.0F, 0.0F, 4.0F, 14.0F, 0.0F, 0.01F, false);

        ModelRenderer upper_jaw_r1 = new ModelRenderer(this);
        upper_jaw_r1.setPos(0.0F, 0.0F, 0.0F);
        upper_jaw.addChild(upper_jaw_r1);
        setRotationAngle(upper_jaw_r1, 0.0F, 3.1416F, 0.0F);
        upper_jaw_r1.texOffs(56, 18).addBox(2.5F, -7.0F, 0.0F, 4.0F, 14.0F, 0.0F, 0.01F, false);

        ModelRenderer upper_jaw_r2 = new ModelRenderer(this);
        upper_jaw_r2.setPos(0.0F, 0.0F, -4.0F);
        upper_jaw.addChild(upper_jaw_r2);
        setRotationAngle(upper_jaw_r2, 0.0F, 1.5708F, 0.0F);
        upper_jaw_r2.texOffs(56, 18).addBox(-1.5F, -7.0F, 0.0F, 4.0F, 14.0F, 0.0F, 0.01F, false);

        ModelRenderer upper_jaw_r3 = new ModelRenderer(this);
        upper_jaw_r3.setPos(0.0F, 0.0F, 4.0F);
        upper_jaw.addChild(upper_jaw_r3);
        setRotationAngle(upper_jaw_r3, 0.0F, -1.5708F, 0.0F);
        upper_jaw_r3.texOffs(56, 18).addBox(-1.5F, -7.0F, 0.0F, 4.0F, 14.0F, 0.0F, 0.01F, false);

        ModelRenderer upper_jaw_r4 = new ModelRenderer(this);
        upper_jaw_r4.setPos(0.0F, 0.0F, 0.0F);
        upper_jaw.addChild(upper_jaw_r4);
        setRotationAngle(upper_jaw_r4, 0.0F, -1.5708F, 0.0F);
        upper_jaw_r4.texOffs(46, 0).addBox(-2.5F, -7.0F, -2.0F, 5.0F, 14.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch){
        float f1 = (pLimbSwing + MathHelper.sin(pLimbSwing * 2.7F)) * 0.6F * 12.0F;
        this.upper_jaw.yRot -= pAgeInTicks;
        this.upper_jaw.y = 18.0F - f1;
        this.base.y = 24.0F - f1;
    }

    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.base, this.upper_jaw);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
