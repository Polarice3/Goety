package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.ally.PhantomMinionEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PhantomModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class PhantomMinionEyesLayer<T extends PhantomMinionEntity> extends LayerRenderer<T, PhantomModel<T>> {
    private static final RenderType PHANTOM_EYES = RenderType.eyes(new ResourceLocation("textures/entity/phantom_eyes.png"));
    private static final RenderType NETHER_PHANTOM_EYES = RenderType.eyes(Goety.location("textures/entity/nether_phantom_eyes.png"));

    public PhantomMinionEyesLayer(IEntityRenderer<T, PhantomModel<T>> p_i50928_1_) {
        super(p_i50928_1_);
    }

    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        RenderType renderType = pLivingEntity.isNether() ? NETHER_PHANTOM_EYES : PHANTOM_EYES;
        IVertexBuilder ivertexbuilder = pBuffer.getBuffer(renderType);
        this.getParentModel().renderToBuffer(pMatrixStack, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
