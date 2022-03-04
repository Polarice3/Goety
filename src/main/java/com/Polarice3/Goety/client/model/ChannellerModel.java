package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.hostile.cultists.ChannellerEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ChannellerModel extends SegmentedModel<ChannellerEntity> implements IHasArm, IHasHead {
    private final ModelRenderer body;
    private final ModelRenderer head;
    private final ModelRenderer hat;
    private final ModelRenderer nose;
    private final ModelRenderer halo;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer arms;
    private final ModelRenderer leg0;
    private final ModelRenderer leg1;
    private final ModelRenderer rightArm;
    private final ModelRenderer rightItem;
    private final ModelRenderer leftArm;
    public ArmPose leftArmPose = ArmPose.EMPTY;
    public ArmPose rightArmPose = ArmPose.EMPTY;

    public ChannellerModel() {
        texWidth = 64;
        texHeight = 64;

        body = new ModelRenderer(this);
        body.setPos(0.0F, 0.0F, 0.0F);
        body.texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);
        body.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, 0.5F, false);

        head = new ModelRenderer(this);
        head.setPos(0.0F, 0.0F, 0.0F);
        body.addChild(head);
        head.texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);

        hat = new ModelRenderer(this);
        hat.setPos(0.0F, -5.0F, 0.0F);
        head.addChild(hat);
        hat.texOffs(32, 0).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.2F, false);

        nose = new ModelRenderer(this);
        nose.setPos(0.0F, -2.0F, 0.0F);
        head.addChild(nose);
        nose.texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, false);

        halo = new ModelRenderer(this);
        halo.setPos(0.0F, 1.0F, -4.0F);
        head.addChild(halo);
        halo.texOffs(35, 39).addBox(-5.0F, -11.0F, 4.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        halo.texOffs(35, 39).addBox(4.0F, -11.0F, 4.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
        halo.texOffs(42, 62).addBox(-5.0F, -12.0F, 4.0F, 10.0F, 1.0F, 1.0F, 0.0F, false);
        halo.texOffs(29, 59).addBox(-1.0F, -15.0F, 4.0F, 2.0F, 3.0F, 1.0F, 0.0F, false);
        halo.texOffs(29, 39).addBox(5.0F, -8.0F, 4.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);
        halo.texOffs(29, 39).addBox(-7.0F, -8.0F, 4.0F, 2.0F, 2.0F, 1.0F, 0.0F, false);

        cube_r1 = new ModelRenderer(this);
        cube_r1.setPos(0.0F, 0.0F, 0.0F);
        halo.addChild(cube_r1);
        setRotationAngle(cube_r1, 0.0F, 0.0F, -0.48F);
        cube_r1.texOffs(29, 39).addBox(9.0F, -9.0F, 4.0F, 6.0F, 2.0F, 1.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setPos(0.0F, 0.0F, 0.0F);
        halo.addChild(cube_r2);
        setRotationAngle(cube_r2, 0.0F, 0.0F, 0.48F);
        cube_r2.texOffs(29, 39).addBox(-15.0F, -9.0F, 4.0F, 6.0F, 2.0F, 1.0F, 0.0F, false);

        arms = new ModelRenderer(this);
        arms.setPos(0.0F, 2.0F, 0.0F);
        body.addChild(arms);
        arms.texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
        arms.texOffs(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, 0.0F, false);
        arms.texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, 0.0F, false);

        leg0 = new ModelRenderer(this);
        leg0.setPos(-2.0F, 12.0F, 0.0F);
        body.addChild(leg0);
        leg0.texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        leg1 = new ModelRenderer(this);
        leg1.setPos(2.0F, 12.0F, 0.0F);
        body.addChild(leg1);
        leg1.texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

        rightArm = new ModelRenderer(this);
        rightArm.setPos(-5.0F, 2.0F, 0.0F);
        body.addChild(rightArm);
        rightArm.texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

        rightItem = new ModelRenderer(this);
        rightItem.setPos(-0.5F, 6.0F, 0.5F);
        rightArm.addChild(rightItem);


        leftArm = new ModelRenderer(this);
        leftArm.setPos(5.0F, 2.0F, 0.0F);
        body.addChild(leftArm);
        leftArm.texOffs(40, 46).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);
    }

    @Override
    public void setupAnim(ChannellerEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.arms.y = 3.0F;
        this.arms.z = -1.0F;
        this.arms.xRot = -0.75F;
        this.nose.setPos(0.0F, -2.0F, 0.0F);
        float f = 0.01F * (float)(entity.getId() % 10);
        this.nose.xRot = MathHelper.sin((float)entity.tickCount * f) * 4.5F * ((float)Math.PI / 180F);
        this.nose.yRot = 0.0F;
        this.nose.zRot = MathHelper.cos((float)entity.tickCount * f) * 2.5F * ((float)Math.PI / 180F);
        if (this.riding) {
            this.rightArm.xRot = (-(float)Math.PI / 5F);
            this.rightArm.yRot = 0.0F;
            this.rightArm.zRot = 0.0F;
            this.leftArm.xRot = (-(float)Math.PI / 5F);
            this.leftArm.yRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.leg0.xRot = -1.4137167F;
            this.leg0.yRot = ((float)Math.PI / 10F);
            this.leg0.zRot = 0.07853982F;
            this.leg1.xRot = -1.4137167F;
            this.leg1.yRot = (-(float)Math.PI / 10F);
            this.leg1.zRot = -0.07853982F;
        } else {
            this.rightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            this.rightArm.yRot = 0.0F;
            this.rightArm.zRot = 0.0F;
            this.leftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
            this.leftArm.yRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.leg0.xRot = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
            this.leg0.yRot = 0.0F;
            this.leg0.zRot = 0.0F;
            this.leg1.xRot = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
            this.leg1.yRot = 0.0F;
            this.leg1.zRot = 0.0F;
        }
        ChannellerEntity.ArmPose cultistentity$armpose = entity.getArmPose();
        switch (cultistentity$armpose){
            case CROSSED:
                this.rightArm.xRot = 0;
                this.leftArm.xRot = 0;
                break;
            case SPELLCASTING:
                this.rightArm.z = 0.0F;
                this.rightArm.x = -5.0F;
                this.leftArm.z = 0.0F;
                this.leftArm.x = 5.0F;
                this.rightArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
                this.leftArm.xRot = MathHelper.cos(ageInTicks * 0.6662F) * 0.25F;
                this.rightArm.zRot = 2.3561945F;
                this.leftArm.zRot = -2.3561945F;
                this.rightArm.yRot = 0.0F;
                this.leftArm.yRot = 0.0F;
        }

        boolean flag = cultistentity$armpose == ChannellerEntity.ArmPose.CROSSED;
        this.arms.visible = flag;
        this.leftArm.visible = !flag;
        this.rightArm.visible = !flag;
    }

    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.arms, this.leg0, this.leg1, this.leftArm, this.rightArm);
    }

    public void prepareMobModel(ChannellerEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.rightArmPose = ArmPose.EMPTY;
        this.leftArmPose = ArmPose.EMPTY;
        if (entityIn.getMainArm() == HandSide.RIGHT) {
            this.RightArmPoses(Hand.MAIN_HAND, entityIn);
            this.LeftArmPoses(Hand.OFF_HAND, entityIn);
        } else {
            this.RightArmPoses(Hand.OFF_HAND, entityIn);
            this.LeftArmPoses(Hand.MAIN_HAND, entityIn);
        }
        super.prepareMobModel(entityIn, limbSwing, limbSwingAmount, partialTick);
    }

    private void RightArmPoses (Hand hand, ChannellerEntity entityIn){
        ItemStack itemstack = entityIn.getItemInHand(hand);
        UseAction useAction = itemstack.getUseAnimation();
        if (entityIn.getArmPose() != ChannellerEntity.ArmPose.CROSSED){
            this.rightArmPose = ArmPose.EMPTY;
            if (!itemstack.isEmpty()) {
                this.rightArmPose = ArmPose.ITEM;
            }
        }
    }

    private void LeftArmPoses (Hand hand, ChannellerEntity entityIn){
        ItemStack itemstack = entityIn.getItemInHand(hand);
        UseAction useAction = itemstack.getUseAnimation();
        if (entityIn.getArmPose() != ChannellerEntity.ArmPose.CROSSED){
            this.leftArmPose = ArmPose.EMPTY;
            if (!itemstack.isEmpty()) {
                this.leftArmPose = ArmPose.ITEM;
            }
        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    private ModelRenderer getArm(HandSide p_191216_1_) {
        return p_191216_1_ == HandSide.LEFT ? this.leftArm : this.rightArm;
    }

    public void translateToHand(HandSide sideIn, MatrixStack matrixStackIn) {
        this.getArm(sideIn).translateAndRotate(matrixStackIn);
    }

    public ModelRenderer getNose() {
        return this.nose;
    }

    @Override
    public ModelRenderer getHead() {
        return head;
    }

    @OnlyIn(Dist.CLIENT)
    public static enum ArmPose {
        EMPTY(false),
        ITEM(false);

        private final boolean field_241656_h_;

        private ArmPose(boolean p_i241257_3_) {
            this.field_241656_h_ = p_i241257_3_;
        }

        public boolean func_241657_a_() {
            return this.field_241656_h_;
        }
    }
}
