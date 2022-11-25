package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ChannellerModel;
import com.Polarice3.Goety.common.entities.hostile.cultists.ChannellerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChannellerRenderer extends MobRenderer<ChannellerEntity, ChannellerModel> {
    public static final ResourceLocation BEAM_TEXTURES = Goety.location("textures/entity/cultist/channeller_connection.png");
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/channeller.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entitySmoothCutout(BEAM_TEXTURES);

    public ChannellerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ChannellerModel(), 0.5F);
    }

    private Vector3d blockPosition(LivingEntity entityLivingBaseIn, double p_177110_2_, float p_177110_4_) {
        double d0 = MathHelper.lerp((double)p_177110_4_, entityLivingBaseIn.xOld, entityLivingBaseIn.getX());
        double d1 = MathHelper.lerp((double)p_177110_4_, entityLivingBaseIn.yOld, entityLivingBaseIn.getY()) + p_177110_2_;
        double d2 = MathHelper.lerp((double)p_177110_4_, entityLivingBaseIn.zOld, entityLivingBaseIn.getZ());
        return new Vector3d(d0, d1, d2);
    }

    public boolean shouldRender(ChannellerEntity livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            if (livingEntityIn.getAlly() != null) {
                LivingEntity livingentity = livingEntityIn.getAlly();
                if (livingentity != null) {
                    Vector3d vector3d = this.blockPosition(livingentity, (double)livingentity.getBbHeight() * 0.5D, 1.0F);
                    Vector3d vector3d1 = this.blockPosition(livingEntityIn, (double)livingEntityIn.getEyeHeight(), 1.0F);
                    return camera.isVisible(new AxisAlignedBB(vector3d1.x, vector3d1.y, vector3d1.z, vector3d.x, vector3d.y, vector3d.z));
                }
            }

            return false;
        }
    }

    public void render(ChannellerEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        LivingEntity target = entityIn.getAlly();
        if (target != null && !target.isDeadOrDying() && entityIn.isPraying()) {
            float f = partialTicks/4;
            float f1 = (float)entityIn.level.getGameTime() + partialTicks;
            float f2 = f1 * 0.5F % 1.0F;
            float f3 = entityIn.getEyeHeight();
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0D, (double)f3, 0.0D);
            Vector3d vector3d = this.blockPosition(target, (double)target.getBbHeight() * 0.5D, partialTicks);
            Vector3d vector3d1 = this.blockPosition(entityIn, (double)f3, partialTicks);
            Vector3d vector3d2 = vector3d.subtract(vector3d1);
            float f4 = (float)(vector3d2.length());
            vector3d2 = vector3d2.normalize();
            float f5 = (float)Math.acos(vector3d2.y);
            float f6 = (float)Math.atan2(vector3d2.z, vector3d2.x);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees((((float)Math.PI / 2F) - f6) * (180F / (float)Math.PI)));
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(f5 * (180F / (float)Math.PI)));
            int i = 1;
            float f7 = f1 * 0.05F * -1.5F;
            float f8 = f * f;
            int j = 125 + (int)(f8 * 191.0F);
            int k = 70 + (int)(f8 * 191.0F);
            int l = 20 - (int)(f8 * 64.0F);
            float f9 = 0.2F;
            float f10 = 0.282F;
            float f11 = MathHelper.cos(f7 + 2.3561945F) * 0.282F;
            float f12 = MathHelper.sin(f7 + 2.3561945F) * 0.282F;
            float f13 = MathHelper.cos(f7 + ((float)Math.PI / 4F)) * 0.282F;
            float f14 = MathHelper.sin(f7 + ((float)Math.PI / 4F)) * 0.282F;
            float f15 = MathHelper.cos(f7 + 3.926991F) * 0.282F;
            float f16 = MathHelper.sin(f7 + 3.926991F) * 0.282F;
            float f17 = MathHelper.cos(f7 + 5.4977875F) * 0.282F;
            float f18 = MathHelper.sin(f7 + 5.4977875F) * 0.282F;
            float f19 = MathHelper.cos(f7 + (float)Math.PI) * 0.2F;
            float f20 = MathHelper.sin(f7 + (float)Math.PI) * 0.2F;
            float f21 = MathHelper.cos(f7 + 0.0F) * 0.2F;
            float f22 = MathHelper.sin(f7 + 0.0F) * 0.2F;
            float f23 = MathHelper.cos(f7 + ((float)Math.PI / 2F)) * 0.2F;
            float f24 = MathHelper.sin(f7 + ((float)Math.PI / 2F)) * 0.2F;
            float f25 = MathHelper.cos(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
            float f26 = MathHelper.sin(f7 + ((float)Math.PI * 1.5F)) * 0.2F;
            float f27 = 0.0F;
            float f28 = 0.4999F;
            float f29 = -1.0F + f2;
            float f30 = f4 * 2.5F + f29;
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(BEAM_RENDER_TYPE);
            MatrixStack.Entry matrixstack$entry = matrixStackIn.last();
            Matrix4f matrix4f = matrixstack$entry.pose();
            Matrix3f matrix3f = matrixstack$entry.normal();
            vertex(ivertexbuilder, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
            vertex(ivertexbuilder, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
            vertex(ivertexbuilder, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
            vertex(ivertexbuilder, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
            vertex(ivertexbuilder, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
            vertex(ivertexbuilder, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
            vertex(ivertexbuilder, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
            vertex(ivertexbuilder, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
            float f31 = 0.0F;
            if (entityIn.tickCount % 2 == 0) {
                f31 = 0.5F;
            }

            vertex(ivertexbuilder, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
            vertex(ivertexbuilder, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
            vertex(ivertexbuilder, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
            vertex(ivertexbuilder, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
            matrixStackIn.popPose();
        }
    }

    private static void vertex(IVertexBuilder vertexBuilder, Matrix4f pMatrix4f, Matrix3f pMatrix3f, float pX, float pY, float pZ, int pRed, int pGreen, int pBlue, float pU, float pV) {
        vertexBuilder.vertex(pMatrix4f, pX, pY, pZ).color(pRed, pGreen, pBlue, 255).uv(pU, pV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(pMatrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(ChannellerEntity entity) {
        return TEXTURE;
    }

    protected void scale(ChannellerEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.9375F;
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }
}
