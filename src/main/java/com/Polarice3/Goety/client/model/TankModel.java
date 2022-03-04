package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.ally.FriendlyTankEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class TankModel <T extends LivingEntity> extends EntityModel<T> {
    private final ModelRenderer Tank;
    private final ModelRenderer Body;
    private final ModelRenderer Turret;
    private final ModelRenderer Bottom;
    private final ModelRenderer BottomParts;
    private final ModelRenderer LeftWheels;
    private final ModelRenderer BackLW;
    private final ModelRenderer UBackLW;
    private final ModelRenderer UFrontLW;
    private final ModelRenderer FrontLW;
    private final ModelRenderer RightWheels;
    private final ModelRenderer BackRW;
    private final ModelRenderer UBackRW;
    private final ModelRenderer UFrontRW;
    private final ModelRenderer FrontRW;

    public TankModel() {
        texWidth = 256;
        texHeight = 256;

        Tank = new ModelRenderer(this);
        Tank.setPos(0.0F, 19.0F, 0.0F);


        Body = new ModelRenderer(this);
        Body.setPos(0.0F, -3.0F, 0.0F);
        Tank.addChild(Body);
        Body.texOffs(0, 0).addBox(-16.0F, -32.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, false);
        Body.texOffs(98, 0).addBox(-16.0F, -8.0F, -24.0F, 32.0F, 8.0F, 8.0F, 0.0F, false);
        Body.texOffs(130, 30).addBox(-16.0F, -4.0F, -28.0F, 32.0F, 4.0F, 4.0F, 0.0F, false);
        Body.texOffs(58, 126).addBox(-16.0F, -12.0F, -20.0F, 32.0F, 4.0F, 4.0F, 0.0F, false);
        Body.texOffs(0, 0).addBox(-8.0F, -40.0F, 16.0F, 4.0F, 24.0F, 4.0F, 0.0F, false);
        Body.texOffs(0, 66).addBox(4.0F, -40.0F, 16.0F, 4.0F, 24.0F, 4.0F, 0.0F, false);
        Body.texOffs(0, 0).addBox(4.0F, -40.0F, 20.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
        Body.texOffs(0, 66).addBox(-8.0F, -40.0F, 20.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        Turret = new ModelRenderer(this);
        Turret.setPos(0.0F, 14.0F, 0.0F);
        Body.addChild(Turret);
        Turret.texOffs(116, 126).addBox(-4.0F, -34.0F, -32.0F, 8.0F, 8.0F, 16.0F, 0.0F, false);

        Bottom = new ModelRenderer(this);
        Bottom.setPos(0.0F, 11.0F, 0.0F);
        Tank.addChild(Bottom);
        Bottom.texOffs(0, 66).addBox(-16.0F, -14.0F, -16.0F, 32.0F, 2.0F, 32.0F, 0.0F, false);

        BottomParts = new ModelRenderer(this);
        BottomParts.setPos(0.0F, 11.0F, 0.0F);
        Tank.addChild(BottomParts);
        BottomParts.texOffs(98, 18).addBox(-20.0F, -12.0F, -16.0F, 40.0F, 2.0F, 2.0F, 0.0F, false);
        BottomParts.texOffs(98, 18).addBox(-20.0F, -12.0F, -6.0F, 40.0F, 2.0F, 2.0F, 0.0F, false);
        BottomParts.texOffs(98, 18).addBox(-20.0F, -12.0F, 4.0F, 40.0F, 2.0F, 2.0F, 0.0F, false);
        BottomParts.texOffs(98, 18).addBox(-20.0F, -12.0F, 14.0F, 40.0F, 2.0F, 2.0F, 0.0F, false);
        BottomParts.texOffs(90, 62).addBox(-26.0F, -16.0F, -20.0F, 8.0F, 10.0F, 40.0F, 0.0F, false);
        BottomParts.texOffs(90, 62).addBox(18.0F, -16.0F, -20.0F, 8.0F, 10.0F, 40.0F, 0.0F, false);

        LeftWheels = new ModelRenderer(this);
        LeftWheels.setPos(0.0F, 7.0F, 0.0F);
        Tank.addChild(LeftWheels);


        BackLW = new ModelRenderer(this);
        BackLW.setPos(22.0F, -7.0F, 15.0F);
        LeftWheels.addChild(BackLW);
        BackLW.texOffs(98, 66).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        UBackLW = new ModelRenderer(this);
        UBackLW.setPos(22.0F, -7.0F, 5.0F);
        LeftWheels.addChild(UBackLW);
        UBackLW.texOffs(98, 66).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        UFrontLW = new ModelRenderer(this);
        UFrontLW.setPos(22.0F, -7.0F, -5.0F);
        LeftWheels.addChild(UFrontLW);
        UFrontLW.texOffs(98, 66).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        FrontLW = new ModelRenderer(this);
        FrontLW.setPos(22.0F, -7.0F, -15.0F);
        LeftWheels.addChild(FrontLW);
        FrontLW.texOffs(98, 66).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        RightWheels = new ModelRenderer(this);
        RightWheels.setPos(0.0F, 7.0F, 0.0F);
        Tank.addChild(RightWheels);


        BackRW = new ModelRenderer(this);
        BackRW.setPos(-22.0F, -7.0F, 15.0F);
        RightWheels.addChild(BackRW);
        BackRW.texOffs(0, 118).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        UBackRW = new ModelRenderer(this);
        UBackRW.setPos(-22.0F, -7.0F, 5.0F);
        RightWheels.addChild(UBackRW);
        UBackRW.texOffs(0, 118).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        UFrontRW = new ModelRenderer(this);
        UFrontRW.setPos(-22.0F, -7.0F, -5.0F);
        RightWheels.addChild(UFrontRW);
        UFrontRW.texOffs(0, 118).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);

        FrontRW = new ModelRenderer(this);
        FrontRW.setPos(-22.0F, -7.0F, -15.0F);
        RightWheels.addChild(FrontRW);
        FrontRW.texOffs(0, 118).addBox(-2.0F, -3.0F, -3.0F, 4.0F, 6.0F, 6.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = ageInTicks / 60.0F;
        float wheel = FrontLW.xRot + ageInTicks * 0.3F;
        this.Body.yRot = netHeadYaw * ((float)Math.PI /180F);
        this.Body.xRot = headPitch * ((float)Math.PI / 180F);
        this.BackRW.xRot = wheel;
        this.UBackRW.xRot = wheel;
        this.UFrontRW.xRot = wheel;
        this.FrontRW.xRot = wheel;
        this.BackLW.xRot = wheel;
        this.UBackLW.xRot = wheel;
        this.UFrontLW.xRot = wheel;
        this.FrontLW.xRot = wheel;
        if (entityIn instanceof FriendlyTankEntity){
            if (((FriendlyTankEntity) entityIn).isQueuedToSit()){
                this.Body.y = 0.4F;
            } else {
                this.Body.y = MathHelper.sin(f * 40.0F) + 0.4F;
            }
        } else {
            this.Body.y = MathHelper.sin(f * 40.0F) + 0.4F;

        }
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        Tank.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
