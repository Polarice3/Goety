package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.hostile.illagers.TormentorEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;

public class TormentorModel <T extends TormentorEntity> extends BipedModel<T> {
    public final ModelRenderer clothes;
    public final ModelRenderer all;
    private final ModelRenderer RightWing;
    private final ModelRenderer LeftWing;

    public TormentorModel(float modelSize, float p_i47227_2_) {
        super(modelSize);
        texWidth = 64;
        texHeight = 64;
        this.leftLeg.visible = false;
        this.hat.visible = false;

        this.all = new ModelRenderer(this);
        this.all.setPos(0.0F, 0.0F, 0.0F);

        this.head = new ModelRenderer(this);
        this.head.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, modelSize);

        ModelRenderer modelrenderer = new ModelRenderer(this);
        modelrenderer.setPos(0.0F, p_i47227_2_ - 2.0F, 0.0F);
        modelrenderer.texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, modelSize);
        this.head.addChild(modelrenderer);
        this.all.addChild(this.head);

        this.body = new ModelRenderer(this);
        this.body.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.body.texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, modelSize);
        this.clothes = new ModelRenderer(this);
        this.clothes.setPos(0.0F, 0.0F + p_i47227_2_, 0.0F);
        this.clothes.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, modelSize + 0.5F);
        this.all.addChild(this.body);
        this.all.addChild(this.clothes);

        this.rightLeg = new ModelRenderer(this);
        this.rightLeg.setPos(2.0F, 12.0F, 0.0F);
        this.rightLeg.texOffs(0, 22).addBox(-4.0F, 1.0F, -2.0F, 4.0F, 12.0F, 4.0F, 1.0F, true);

        this.rightArm = new ModelRenderer(this, 40, 46);
        this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.rightArm.setPos(-5.0F, 2.0F + p_i47227_2_, 0.0F);

        this.leftArm = new ModelRenderer(this, 40, 46);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, modelSize);
        this.leftArm.setPos(5.0F, 2.0F + p_i47227_2_, 0.0F);

        this.RightWing = new ModelRenderer(this);
        this.RightWing.setPos(0.0F, 3.0F, 4.0F);
        this.body.addChild(this.RightWing);
        this.RightWing.texOffs(32, 0).addBox(-16.0F, -1.0F, 0.0F, 16.0F, 12.0F, 0.0F, 0.0F, false);

        this.LeftWing = new ModelRenderer(this);
        this.LeftWing.setPos(0.0F, 3.0F, 4.0F);
        this.body.addChild(this.LeftWing);
        this.LeftWing.texOffs(32, 0).addBox(0.0F, -1.0F, 0.0F, 16.0F, 12.0F, 0.0F, 0.0F, true);
    }

    @Override
    protected Iterable<ModelRenderer> bodyParts() {
        return Iterables.concat(super.bodyParts(), ImmutableList.of(this.RightWing, this.LeftWing, this.clothes, this.all));
    }

    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.rightArm.y = 2.0F;
        this.leftArm.y = 2.0F;
        this.rightLeg.xRot += ((float)Math.PI / 5F);
        this.RightWing.z = 2.0F;
        this.LeftWing.z = 2.0F;
        this.RightWing.y = 1.0F;
        this.LeftWing.y = 1.0F;
        this.RightWing.yRot = 0.47123894F + MathHelper.cos(ageInTicks * 0.8F) * (float)Math.PI * 0.05F;
        this.LeftWing.yRot = -this.RightWing.yRot;
        this.LeftWing.zRot = -0.47123894F;
        this.LeftWing.xRot = 0.47123894F;
        this.RightWing.xRot = 0.47123894F;
        this.RightWing.zRot = 0.47123894F;

        if (entityIn.isCharging()) {
            if (entityIn.getMainHandItem().isEmpty()) {
                this.rightArm.xRot = ((float)Math.PI * 1.5F);
                this.leftArm.xRot = ((float)Math.PI * 1.5F);
            } else if (entityIn.getMainArm() == HandSide.RIGHT) {
                this.rightArm.xRot = 3.7699115F;
            } else {
                this.leftArm.xRot = 3.7699115F;
            }
        }

        AbstractIllagerEntity.ArmPose abstractillagerentity$armpose = entityIn.getArmPose();
        if (abstractillagerentity$armpose == AbstractIllagerEntity.ArmPose.CELEBRATING) {
            this.rightArm.z = 0.0F;
            this.rightArm.x = -5.0F;
            this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
            this.rightArm.zRot = 2.670354F;
            this.rightArm.yRot = 0.0F;
            this.leftArm.z = 0.0F;
            this.leftArm.x = 5.0F;
            this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.05F;
            this.leftArm.zRot = -2.3561945F;
            this.leftArm.yRot = 0.0F;
        }

        boolean flag2 = entityIn.getItemBySlot(EquipmentSlotType.CHEST).getItem() instanceof ArmorItem
                || entityIn.getItemBySlot(EquipmentSlotType.LEGS).getItem() instanceof ArmorItem;
        this.clothes.visible = !flag2;
    }
}
