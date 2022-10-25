package com.Polarice3.Goety.client.render;


import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.entities.projectiles.SpikeGeyserEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.ShulkerBulletModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpikeGeyserRenderer extends EntityRenderer<SpikeGeyserEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = Goety.location("textures/entity/projectiles/spike_geyser.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(TEXTURE_LOCATION);
    private final ShulkerBulletModel<SpikeGeyserEntity> model = new ShulkerBulletModel<>();

    public SpikeGeyserRenderer(EntityRendererManager p_i46551_1_) {
        super(p_i46551_1_);
    }

    protected int getBlockLightLevel(SpikeGeyserEntity pEntity, BlockPos pPos) {
        return 15;
    }

    public void render(SpikeGeyserEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        float f = MathHelper.rotlerp(pEntity.yRotO, pEntity.yRot, pPartialTicks);
        float f1 = MathHelper.lerp(pPartialTicks, pEntity.xRotO, pEntity.xRot);
        float f2 = (float)pEntity.tickCount + pPartialTicks;
        pMatrixStack.translate(0.0D, (double)0.15F, 0.0D);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.sin(f2 * 0.1F) * 180.0F));
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.cos(f2 * 0.1F) * 180.0F));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.sin(f2 * 0.15F) * 360.0F));
        pMatrixStack.scale(-0.5F, -0.5F, 0.5F);
        this.model.setupAnim(pEntity, 0.0F, 0.0F, 0.0F, f, f1);
        IVertexBuilder ivertexbuilder = pBuffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        this.model.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pMatrixStack.scale(1.5F, 1.5F, 1.5F);
        IVertexBuilder ivertexbuilder1 = pBuffer.getBuffer(RENDER_TYPE);
        this.model.renderToBuffer(pMatrixStack, ivertexbuilder1, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(SpikeGeyserEntity pEntity) {
        return TEXTURE_LOCATION;
    }
}
