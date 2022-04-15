package com.Polarice3.Goety.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class SmallCubeModel <T extends Entity> extends SegmentedModel<T> {
    private final ModelRenderer cube;

    public SmallCubeModel() {
        texWidth = 16;
        texHeight = 16;

        cube = new ModelRenderer(this);
        cube.setPos(0.0F, 24.0F, 0.0F);
        cube.texOffs(0, 0).addBox(-1.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
    }

    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.cube);
    }
}
