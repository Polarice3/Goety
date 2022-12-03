package com.Polarice3.Goety.client.model;

import net.minecraft.client.renderer.entity.model.WitchModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModWitchModel<T extends Entity> extends WitchModel<T> {
    protected ModelRenderer hood;
    public ModWitchModel(float p_i46361_1_) {
        super(p_i46361_1_);
        this.hat.setPos(-5.0F, -11.03125F, -5.0F);
        this.hood = (new ModelRenderer(this)).setTexSize(64, 128);
        this.hood.setPos(0.0F, 0.0F, 0.0F);
        this.hood.texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, p_i46361_1_ + 0.5F);
        this.head.addChild(this.hood);
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        if (this.riding) {
            this.leg0.xRot = -1.4137167F;
            this.leg0.yRot = ((float)Math.PI / 10F);
            this.leg0.zRot = 0.07853982F;
            this.leg1.xRot = -1.4137167F;
            this.leg1.yRot = (-(float)Math.PI / 10F);
            this.leg1.zRot = -0.07853982F;
        } else {
            this.leg0.xRot = MathHelper.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount * 0.5F;
            this.leg1.xRot = MathHelper.cos(pLimbSwing * 0.6662F + (float)Math.PI) * 1.4F * pLimbSwingAmount * 0.5F;
            this.leg0.yRot = 0.0F;
            this.leg1.yRot = 0.0F;
        }
    }
}
