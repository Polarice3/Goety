package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.SmallCubeModel;
import com.Polarice3.Goety.common.entities.projectiles.SoulBulletEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class SoulBulletRenderer extends EntityRenderer<SoulBulletEntity> {
    private static final ResourceLocation TEXTURE = Goety.location("textures/entity/soul_bullet.png");
    private final SmallCubeModel<SoulBulletEntity> model = new SmallCubeModel<>();

    public SoulBulletRenderer(EntityRendererManager p_i47202_1_) {
        super(p_i47202_1_);
    }

    public void render(SoulBulletEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.0D, 0.0F, 0.0D);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(pPartialTicks, pEntity.yRotO, pEntity.yRot) - 90.0F));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(pPartialTicks, pEntity.xRotO, pEntity.xRot)));
        this.model.setupAnim(pEntity, pPartialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
        IVertexBuilder ivertexbuilder = pBuffer.getBuffer(this.model.renderType(TEXTURE));
        this.model.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public ResourceLocation getTextureLocation(SoulBulletEntity pEntity) {
        return TEXTURE;
    }
}
