package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.model.ACArmorModel;
import com.Polarice3.Goety.client.model.AbstractCultistModel;
import com.Polarice3.Goety.client.model.ApostleModel;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.common.entities.hostile.cultists.AbstractCultistEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.Random;

public class ApostleRenderer extends AbstractCultistRenderer<ApostleEntity>{
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/apostle.png");
    protected static final ResourceLocation TEXTURE_2 = Goety.location("textures/entity/cultist/apostle_second.png");
    protected static final ResourceLocation EXPLODE = Goety.location("textures/entity/cultist/apostle_explode.png");
    private static final float HALF_SQRT_3 = (float)(Math.sqrt(3.0D) / 2.0D);

    public ApostleRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ApostleModel<>(0.0F, 0.0F), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
        this.addLayer(new HeldItemLayer<ApostleEntity, AbstractCultistModel<ApostleEntity>>(this) {
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, ApostleEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                if (entitylivingbaseIn.getArmPose() != AbstractCultistEntity.ArmPose.CROSSED) {
                    super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
                }

            }
        });
    }

    @Override
    public void render(ApostleEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
        if (pEntity.deathTime > 0) {
            pMatrixStack.pushPose();
            boolean flag = pEntity.hurtTime > 0;
            float f = MathHelper.rotLerp(pPartialTicks, pEntity.yBodyRotO, pEntity.yBodyRot);
            float f1 = MathHelper.rotLerp(pPartialTicks, pEntity.yHeadRotO, pEntity.yHeadRot);
            float f2 = f1 - f;
            float f6 = MathHelper.lerp(pPartialTicks, pEntity.xRotO, pEntity.xRot);
            float f71 = this.getBob(pEntity, pPartialTicks);
            this.setupRotations(pEntity, pMatrixStack, f71, f, pPartialTicks);
            pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
            this.scale(pEntity, pMatrixStack, pPartialTicks);
            pMatrixStack.translate(0.0D, (double)-1.501F, 0.0D);
            this.model.prepareMobModel(pEntity, 0.0F, 0.0F, pPartialTicks);
            this.model.setupAnim(pEntity, 0.0F, 0.0F, f71, f2, f6);
            IVertexBuilder ivertexbuilder1 = pBuffer.getBuffer(RenderType.entityDecal(this.getTextureLocation(pEntity)));
            this.model.renderToBuffer(pMatrixStack, ivertexbuilder1, pPackedLight, OverlayTexture.pack(0.0F, flag), 1.0F, 1.0F, 1.0F, 1.0F);
            pMatrixStack.popPose();

            if (MainConfig.FancierApostleDeath.get()) {
                if (pEntity.deathTime > 20) {
                    float f5 = ((float) pEntity.deathTime + pPartialTicks) / 200.0F;
                    float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
                    Random random = new Random(432L);
                    IVertexBuilder ivertexbuilder2 = pBuffer.getBuffer(RenderType.lightning());
                    pMatrixStack.pushPose();
                    pMatrixStack.translate(0.0D, 1.0D, 0.0D);

                    for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 10.0F; ++i) {
                        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
                        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
                        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F));
                        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(random.nextFloat() * 360.0F));
                        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(random.nextFloat() * 360.0F));
                        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(random.nextFloat() * 360.0F + f5 * 90.0F));
                        float f3 = random.nextFloat() * 2.0F + 5.0F + f7 * 10.0F;
                        float f4 = random.nextFloat() + 1.0F + f7;
                        Matrix4f matrix4f = pMatrixStack.last().pose();
                        int j = (int) (255.0F * (1.0F - f7));
                        vertex01(ivertexbuilder2, matrix4f, j);
                        vertex2(ivertexbuilder2, matrix4f, f3, f4);
                        vertex3(ivertexbuilder2, matrix4f, f3, f4);
                        vertex01(ivertexbuilder2, matrix4f, j);
                        vertex3(ivertexbuilder2, matrix4f, f3, f4);
                        vertex4(ivertexbuilder2, matrix4f, f3, f4);
                        vertex01(ivertexbuilder2, matrix4f, j);
                        vertex4(ivertexbuilder2, matrix4f, f3, f4);
                        vertex2(ivertexbuilder2, matrix4f, f3, f4);
                    }

                    pMatrixStack.popPose();
                }
            }
        }
    }

    private static void vertex01(IVertexBuilder p_229061_0_, Matrix4f p_229061_1_, int p_229061_2_) {
        p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
        p_229061_0_.vertex(p_229061_1_, 0.0F, 0.0F, 0.0F).color(255, 255, 255, p_229061_2_).endVertex();
    }

    private static void vertex2(IVertexBuilder p_229060_0_, Matrix4f p_229060_1_, float pY, float p_229060_3_) {
        p_229060_0_.vertex(p_229060_1_, -HALF_SQRT_3 * p_229060_3_, pY, -0.5F * p_229060_3_).color(255, 0, 0, 0).endVertex();
    }

    private static void vertex3(IVertexBuilder p_229062_0_, Matrix4f p_229062_1_, float pY, float p_229062_3_) {
        p_229062_0_.vertex(p_229062_1_, HALF_SQRT_3 * p_229062_3_, pY, -0.5F * p_229062_3_).color(255, 0, 0, 0).endVertex();
    }

    private static void vertex4(IVertexBuilder p_229063_0_, Matrix4f p_229063_1_, float pY, float p_229063_3_) {
        p_229063_0_.vertex(p_229063_1_, 0.0F, pY, 1.0F * p_229063_3_).color(255, 0, 0, 0).endVertex();
    }

    @Nullable
    protected RenderType getRenderType(ApostleEntity p_230496_1_, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        if (p_230496_1_.deathTime > 0){
            float f1 = MainConfig.FancierApostleDeath.get() ? 300.0F : 20.0F;
            float f2 = (float)p_230496_1_.deathTime / f1;
            return RenderType.dragonExplosionAlpha(EXPLODE, f2);
        } else {
            return super.getRenderType(p_230496_1_, p_230496_2_, p_230496_3_, p_230496_4_);
        }
    }

    @Override
    protected boolean isShaking(ApostleEntity p_230495_1_) {
        return p_230495_1_.isDeadOrDying() && p_230495_1_.deathTime < 180;
    }

    @Override
    public ResourceLocation getTextureLocation(ApostleEntity entity) {
        return entity.isSecondPhase() ? TEXTURE_2 : TEXTURE;
    }
}
