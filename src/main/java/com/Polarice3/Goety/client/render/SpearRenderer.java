package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.SpearModel;
import com.Polarice3.Goety.common.entities.projectiles.SpearEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemTier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class SpearRenderer extends EntityRenderer<SpearEntity> {
    public static final ResourceLocation WOODEN_SPEAR = new ResourceLocation(Goety.MOD_ID, "textures/entity/wooden_spear.png");
    public static final ResourceLocation STONE_SPEAR = new ResourceLocation(Goety.MOD_ID, "textures/entity/stone_spear.png");
    public static final ResourceLocation IRON_SPEAR = new ResourceLocation(Goety.MOD_ID, "textures/entity/iron_spear.png");
    public static final ResourceLocation DIAMOND_SPEAR = new ResourceLocation(Goety.MOD_ID, "textures/entity/diamond_spear.png");
    public static final ResourceLocation NETHERITE_SPEAR = new ResourceLocation(Goety.MOD_ID, "textures/entity/netherite_spear.png");
    private final SpearModel model = new SpearModel();

    public SpearRenderer(EntityRendererManager p_i48828_1_) {
        super(p_i48828_1_);
    }

    public void render(SpearEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(pPartialTicks, pEntity.yRotO, pEntity.yRot) - 90.0F));
        pMatrixStack.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(pPartialTicks, pEntity.xRotO, pEntity.xRot) + 90.0F));
        IVertexBuilder ivertexbuilder = net.minecraft.client.renderer.ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, pEntity.isFoil());
        this.model.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pMatrixStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(SpearEntity pEntity) {
        if (pEntity.getTier() == ItemTier.WOOD){
            return WOODEN_SPEAR;
        } else if (pEntity.getTier() == ItemTier.STONE){
            return STONE_SPEAR;
        } else if (pEntity.getTier() == ItemTier.IRON){
            return IRON_SPEAR;
        } else if (pEntity.getTier() == ItemTier.DIAMOND){
            return DIAMOND_SPEAR;
        } else if (pEntity.getTier() == ItemTier.NETHERITE){
            return NETHERITE_SPEAR;
        } else {
            return IRON_SPEAR;
        }
    }
}
