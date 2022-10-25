package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.VizierModel;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class VizierCapeLayer extends LayerRenderer<VizierEntity, VizierModel> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/illagers/vizier.png");

    public VizierCapeLayer(IEntityRenderer<VizierEntity, VizierModel> p_i50950_1_) {
        super(p_i50950_1_);
    }

    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, VizierEntity pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (!pLivingEntity.isInvisible()) {
            ItemStack itemstack = pLivingEntity.getItemBySlot(EquipmentSlotType.CHEST);
            if (itemstack.isEmpty()) {
                pMatrixStack.pushPose();
                pMatrixStack.translate(0.0D, 0.0D, 0.425D);
                double d0 = MathHelper.lerp((double)pPartialTicks, pLivingEntity.xCloakO, pLivingEntity.xCloak) - MathHelper.lerp((double)pPartialTicks, pLivingEntity.xo, pLivingEntity.getX());
                double d1 = MathHelper.lerp((double)pPartialTicks, pLivingEntity.yCloakO, pLivingEntity.yCloak) - MathHelper.lerp((double)pPartialTicks, pLivingEntity.yo, pLivingEntity.getY());
                double d2 = MathHelper.lerp((double)pPartialTicks, pLivingEntity.zCloakO, pLivingEntity.zCloak) - MathHelper.lerp((double)pPartialTicks, pLivingEntity.zo, pLivingEntity.getZ());
                float f = pLivingEntity.yBodyRotO + (pLivingEntity.yBodyRot - pLivingEntity.yBodyRotO);
                double d3 = (double)MathHelper.sin(f * ((float)Math.PI / 180F));
                double d4 = (double)(-MathHelper.cos(f * ((float)Math.PI / 180F)));
                float f1 = (float)d1 * 10.0F;
                f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
                float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
                f2 = MathHelper.clamp(f2, 0.0F, 150.0F);
                float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;
                f3 = MathHelper.clamp(f3, -20.0F, 20.0F);
                if (f2 < 0.0F) {
                    f2 = 0.0F;
                }

                float f4 = MathHelper.lerp(pPartialTicks, pLivingEntity.oBob, pLivingEntity.bob);
                f1 = f1 + MathHelper.sin(MathHelper.lerp(pPartialTicks, pLivingEntity.walkDistO, pLivingEntity.walkDist) * 6.0F) * 32.0F * f4;
                if (pLivingEntity.isCrouching()) {
                    f1 += 25.0F;
                }

                pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(6.0F + f2 / 2.0F + f1));
                pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(f3 / 2.0F));
                pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - f3 / 2.0F));
                IVertexBuilder ivertexbuilder = pBuffer.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
                this.getParentModel().renderCape(pMatrixStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY);
                pMatrixStack.popPose();
            }
        }
    }
}
