package com.Polarice3.Goety.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.ResourceLocation;

public class SoulBulletRenderer extends EntityRenderer<ProjectileEntity> {

    public SoulBulletRenderer(EntityRendererManager p_i47202_1_) {
        super(p_i47202_1_);
    }

    public void render(ProjectileEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public ResourceLocation getTextureLocation(ProjectileEntity pEntity) {
        return null;
    }
}
