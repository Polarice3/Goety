package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.projectiles.SoulSkullEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SoulSkullRenderer extends EntityRenderer<SoulSkullEntity> {
    private static final ResourceLocation WITHER_INVULNERABLE_LOCATION = new ResourceLocation(Goety.MOD_ID,"textures/entity/projectiles/soulskull_destroy.png");
    private static final ResourceLocation WITHER_LOCATION = new ResourceLocation(Goety.MOD_ID,"textures/entity/projectiles/soulskull.png");
    private final GenericHeadModel model = new GenericHeadModel();

    public SoulSkullRenderer(EntityRendererManager p_i46129_1_) {
        super(p_i46129_1_);
    }

    protected int getBlockLightLevel(SoulSkullEntity pEntity, BlockPos pPos) {
        return 15;
    }

    public void render(SoulSkullEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        if (pEntity.isUpgraded()){
            pMatrixStack.scale(-2.0F, -2.0F, 2.0F);
        } else {
            pMatrixStack.scale(-1.0F, -1.0F, 1.0F);
        }
        float f = MathHelper.rotlerp(pEntity.yRotO, pEntity.yRot, pPartialTicks);
        float f1 = MathHelper.lerp(pPartialTicks, pEntity.xRotO, pEntity.xRot);
        IVertexBuilder ivertexbuilder = pBuffer.getBuffer(this.model.renderType(this.getTextureLocation(pEntity)));
        this.model.setupAnim(0.0F, f, f1);
        this.model.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public ResourceLocation getTextureLocation(SoulSkullEntity pEntity) {
        return pEntity.isDangerous() ? WITHER_INVULNERABLE_LOCATION : WITHER_LOCATION;
    }
}
