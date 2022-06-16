package com.Polarice3.Goety.client.model;

import com.Polarice3.Goety.common.entities.hostile.SkullLordEntity;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SkullMobModel extends SegmentedModel<SkullLordEntity> {
    private final ModelRenderer head;

    public SkullMobModel(float scaleFactor) {
        super(RenderType::entityTranslucent);
        texWidth = 64;
        texHeight = 32;
        this.head = new ModelRenderer(this);
        this.head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, scaleFactor);
        this.head.texOffs(32, 0).addBox(-4.0F, -12.0F, -4.0F, 8.0F, 6.0F, 8.0F, 0.5F, false);
        this.head.setPos(0.0F, 24.0F, 0.0F);
    }

    @Override
    public void setupAnim(SkullLordEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.head.yRot = pNetHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = pHeadPitch * ((float)Math.PI / 180F);
    }

    public Iterable<ModelRenderer> parts() {
        return ImmutableList.of(this.head);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        head.render(matrixStack, buffer, packedLight, packedOverlay);
    }
}
