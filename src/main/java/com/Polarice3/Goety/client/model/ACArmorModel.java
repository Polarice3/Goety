package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ACArmorModel<T extends AbstractCultistEntity> extends BipedModel<T> {
    public final ModelRenderer all;

    public ACArmorModel(float modelSizeIn) {
        super(modelSizeIn);
        this.all = new ModelRenderer(this);
        this.all.setPos(0.0F, 0.0F, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-4.0F, -10.0F, -4.0F, 8.0F, 8.0F, 8.0F, modelSizeIn);
        this.head.setPos(0.0F, 0.0F, 0.0F);
    }

}
