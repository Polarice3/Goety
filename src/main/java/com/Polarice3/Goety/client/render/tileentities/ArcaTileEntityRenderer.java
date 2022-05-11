package com.Polarice3.Goety.client.render.tileentities;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.tileentities.ArcaTileEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public class ArcaTileEntityRenderer extends TileEntityRenderer<ArcaTileEntity> {
    public static final ResourceLocation GLASS_CAGE_TEXTURE = Goety.location("textures/entity/arca/cage.png");
    public static final ResourceLocation CORE_TEXTURE = Goety.location("textures/entity/arca/core.png");
    public static final ResourceLocation D_CORE_TEXTURE = Goety.location("textures/entity/arca/d_core.png");
    private static final RenderType GLASS_CAGE_RENDER = RenderType.entityCutoutNoCull(GLASS_CAGE_TEXTURE);
    private static final RenderType CORE_RENDER = RenderType.entityCutoutNoCull(CORE_TEXTURE);
    private static final RenderType D_CORE_RENDER = RenderType.entityCutoutNoCull(D_CORE_TEXTURE);
    private final ModelRenderer cage;
    private final ModelRenderer core;

    public ArcaTileEntityRenderer(TileEntityRendererDispatcher p_i226009_1_) {
        super(p_i226009_1_);
        this.cage = new ModelRenderer(32, 16, 0, 0);
        this.cage.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
        this.core = new ModelRenderer(32, 16, 0, 0);
        this.core.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
    }
    public void render(ArcaTileEntity pBlockEntity, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pCombinedLight, int pCombinedOverlay) {
        float f = (float)pBlockEntity.tickCount + pPartialTicks;
        float f1 = pBlockEntity.getActiveRotation(pPartialTicks) * (180F / (float)Math.PI);
        float f2 = MathHelper.sin(f * 0.1F) / 2.0F + 0.5F;
        f2 = f2 * f2 + f2;
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.5D, 0.3F + f2 * 0.1F, 0.5D);
        Vector3f vector3f = new Vector3f(0.5F, 1.0F, 0.5F);
        vector3f.normalize();
        pMatrixStack.mulPose(new Quaternion(vector3f, f1, true));
        IVertexBuilder ivertexbuilder = pBuffer.getBuffer(GLASS_CAGE_RENDER);
        this.cage.render(pMatrixStack, ivertexbuilder, pCombinedLight, pCombinedOverlay);
        pMatrixStack.popPose();
        pMatrixStack.pushPose();
        pMatrixStack.translate(0.5D, 0.3F + f2 * 0.1F, 0.5D);
        pMatrixStack.scale(0.5F, 0.5F, 0.5F);
        pMatrixStack.mulPose(Vector3f.YP.rotationDegrees(3 * f));
        IVertexBuilder ivertexbuilder2 = pBuffer.getBuffer(CORE_RENDER);
        this.core.render(pMatrixStack, ivertexbuilder2, pCombinedLight, pCombinedOverlay);
        pMatrixStack.popPose();
    }

}
