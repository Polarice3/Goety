package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ApostleModel<T extends AbstractCultistEntity> extends AbstractCultistModel<T> {

    public ApostleModel(float modelSize, float p_i47227_2_) {
        super(modelSize, p_i47227_2_);

        ModelRenderer halo = new ModelRenderer(this);
        halo.setPos(0.0F, 0.0F, 11.0F);
        this.head.addChild(halo);
        halo.texOffs(32, 0).addBox(-8.0F, -17.0F, -6.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);
        this.hat.visible = false;
    }

}
