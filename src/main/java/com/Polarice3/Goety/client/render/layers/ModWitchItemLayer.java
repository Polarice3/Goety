package com.Polarice3.Goety.client.render.layers;

import com.Polarice3.Goety.client.model.ModWitchModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.model.WitchModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.vector.Vector3f;

public class ModWitchItemLayer<T extends LivingEntity> extends CrossedArmsItemLayer<T, ModWitchModel<T>> {
    public ModWitchItemLayer(IEntityRenderer<T, ModWitchModel<T>> p_i50916_1_) {
        super(p_i50916_1_);
    }

    public void render(MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack itemstack = pLivingEntity.getMainHandItem();
        pMatrixStack.pushPose();
        if (itemstack.getItem() == Items.POTION) {
            this.getParentModel().getHead().translateAndRotate(pMatrixStack);
            this.getParentModel().getNose().translateAndRotate(pMatrixStack);
            pMatrixStack.translate(0.0625D, 0.25D, 0.0D);
            pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(140.0F));
            pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(10.0F));
            pMatrixStack.translate(0.0D, (double)-0.4F, (double)0.4F);
        }

        super.render(pMatrixStack, pBuffer, pPackedLight, pLivingEntity, pLimbSwing, pLimbSwingAmount, pPartialTicks, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        pMatrixStack.popPose();
    }
}
