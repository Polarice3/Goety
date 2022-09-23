package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.common.entities.projectiles.DeadTNTEntity;
import com.Polarice3.Goety.init.ModBlocks;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class DeadTNTRenderer extends EntityRenderer<DeadTNTEntity> {

    public DeadTNTRenderer(EntityRendererManager p_i46134_1_) {
        super(p_i46134_1_);
        this.shadowRadius = 0.5F;
    }

    public void render(DeadTNTEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.0D, 0.5D, 0.0D);
        if ((float)pEntity.getLife() - pPartialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)pEntity.getLife() - pPartialTicks + 1.0F) / 10.0F;
            f = MathHelper.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            pMatrixStack.scale(f1, f1, f1);
        }

        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        pMatrixStack.translate(-0.5D, -0.5D, 0.5D);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        TNTMinecartRenderer.renderWhiteSolidBlock(ModBlocks.DEAD_TNT.get().defaultBlockState(), pMatrixStack, pBuffer, pPackedLight, pEntity.getLife() / 5 % 2 == 0);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public ResourceLocation getTextureLocation(DeadTNTEntity pEntity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}
