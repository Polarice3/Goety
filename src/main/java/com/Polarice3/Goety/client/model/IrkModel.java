package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.hostile.IrkEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelHelper;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.util.math.MathHelper;

public class IrkModel extends BipedModel<IrkEntity> {
    private final ModelRenderer leftWing;
    private final ModelRenderer rightWing;

    public IrkModel() {
        super(0.0F, 0.0F, 64, 64);
        this.leftLeg.visible = false;
        this.hat.visible = false;
        this.rightLeg = new ModelRenderer(this, 32, 0);
        this.rightLeg.addBox(-1.0F, -1.0F, -2.0F, 6.0F, 10.0F, 4.0F, 0.0F);
        this.rightLeg.setPos(-1.9F, 12.0F, 0.0F);
        this.rightWing = new ModelRenderer(this, 0, 32);
        this.rightWing.addBox(-20.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F);
        this.leftWing = new ModelRenderer(this, 0, 32);
        this.leftWing.mirror = true;
        this.leftWing.addBox(0.0F, 0.0F, 0.0F, 20.0F, 12.0F, 1.0F);
    }

    protected Iterable<ModelRenderer> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.rightWing, this.leftWing));
    }

    /**
     * Sets this entity's model rotation angles
     */
    public void setupAnim(IrkEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        AbstractIllagerEntity.ArmPose abstractillagerentity$armpose = entityIn.getArmPose();
        if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CROSSBOW_HOLD) {
            ModelHelper.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
        } else if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CROSSBOW_CHARGE) {
            ModelHelper.animateCrossbowCharge(this.rightArm, this.leftArm, entityIn, true);
        }

        this.rightLeg.xRot += ((float)Math.PI / 5F);
        this.rightWing.z = 2.0F;
        this.leftWing.z = 2.0F;
        this.rightWing.y = 1.0F;
        this.leftWing.y = 1.0F;
        this.rightWing.yRot = 0.47123894F + MathHelper.cos(ageInTicks * 0.8F) * (float)Math.PI * 0.05F;
        this.leftWing.yRot = -this.rightWing.yRot;
        this.leftWing.zRot = -0.47123894F;
        this.leftWing.xRot = 0.47123894F;
        this.rightWing.xRot = 0.47123894F;
        this.rightWing.zRot = 0.47123894F;
    }
}
