package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ApostleModel<T extends ApostleEntity> extends AbstractCultistModel<T> {
    public ModelRenderer halo;
    public ModelRenderer halo1;
    public ModelRenderer hat2;

    public ApostleModel(float modelSize, float p_i47227_2_) {
        super(modelSize, p_i47227_2_, 64, 128);

        this.halo = new ModelRenderer(this).setTexSize(64, 128);
        this.halo.setPos(0.0F, -12.0F, 5.0F);
        this.head.addChild(this.halo);
        setRotationAngle(this.halo, 0.7854F, 0.0F, 0.0F);

        this.halo1 = new ModelRenderer(this).setTexSize(64, 128);
        this.halo1.setPos(0.0F, 0.0F, 0.0F);
        this.halo.addChild(this.halo1);
        setRotationAngle(this.halo1, 0.0F, 0.0F, 0.0F);
        this.halo1.texOffs(32, 0).addBox(-8.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);
        this.hat.visible = false;

        this.hat2 = new ModelRenderer(this).setTexSize(64, 128);
        this.hat2.setPos(0.0F, 0.0F, 0.0F);
        this.head.addChild(this.hat2);
        this.hat2.texOffs(0, 64).addBox(-8.0F, -7.0F, -8.0F, 16.0F, 1.0F, 16.0F, 0.0F, false);
        this.hat2.texOffs(0, 81).addBox(-5.0F, -11.0F, -5.0F, 10.0F, 4.0F, 10.0F, 0.0F, false);
    }


    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.halo1.zRot = entity.getSpin();
        this.hat2.visible = entity.hasHat();
    }

}
