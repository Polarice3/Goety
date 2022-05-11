package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.ally.SummonedEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;

public class FarmerMinionModel<T extends SummonedEntity> extends BipedModel<T> {
    protected final ModelRenderer hatRim;

    public FarmerMinionModel(float modelSize, boolean p_i1168_2_) {
        this(modelSize, 0.0F, 64, p_i1168_2_ ? 32 : 64);
    }

    protected FarmerMinionModel(float p_i48914_1_, float p_i48914_2_, int p_i48914_3_, int p_i48914_4_) {
        super(p_i48914_1_, p_i48914_2_, p_i48914_3_, p_i48914_4_);
        this.hatRim = (new ModelRenderer(this)).setTexSize(p_i48914_3_, p_i48914_4_);
        this.hatRim.setPos(0.0F, 1.0F, 0.0F);
        this.hatRim.texOffs(30, 47).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F, p_i48914_2_);
        this.hatRim.xRot = (-(float)Math.PI / 2F);
        this.hat.addChild(this.hatRim);
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        ModelHelper.animateZombieArms(this.leftArm, this.rightArm, this.isAggressive(pEntity), this.attackTime, pAgeInTicks);
    }

    public boolean isAggressive(T entityIn) {
        return entityIn.isAggressive();
    }

}
