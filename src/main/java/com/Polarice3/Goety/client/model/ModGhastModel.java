package com.Polarice3.Goety.client.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class ModGhastModel<T extends Entity> extends SegmentedModel<T> {
    private final ModelRenderer[] tentacles = new ModelRenderer[9];
    private final ModelRenderer body;

    public ModGhastModel() {
        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F);
        this.body.setPos(0.0F, 16.0F, 0.0F);
        Random random = new Random(1660L);

        for(int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i] = new ModelRenderer(this, 0, 0);
            float f = (((float)(i % 3) - (float)(i / 3 % 2) * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
            float f1 = ((float)(i / 3) / 2.0F * 2.0F - 1.0F) * 5.0F;
            int j = random.nextInt(7) + 8;
            this.tentacles[i].addBox(-1.0F, 0.0F, -1.0F, 2.0F, (float)j, 2.0F);
            this.tentacles[i].setPos(f, 7.0F, f1);
            this.body.addChild(this.tentacles[i]);
        }
    }

    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        for(int i = 0; i < this.tentacles.length; ++i) {
            this.tentacles[i].xRot = 0.2F * MathHelper.sin(pAgeInTicks * 0.3F + (float)i) + 0.4F;
        }
        this.body.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.body.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }

    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.body);
    }
}
