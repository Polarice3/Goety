package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.DrownedMinionModel;
import com.Polarice3.Goety.client.render.layers.DrownedMinionOuterLayer;
import com.Polarice3.Goety.common.entities.ally.DrownedMinionEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class DrownedMinionRenderer extends BipedRenderer<DrownedMinionEntity, DrownedMinionModel<DrownedMinionEntity>> {
    private static final ResourceLocation DROWNED_LOCATION = Goety.location("textures/entity/servants/drowned_minion.png");

    public DrownedMinionRenderer(EntityRendererManager p_i48906_1_) {
        super(p_i48906_1_, new DrownedMinionModel<>(0.0F, 0.0F, 64, 64), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new DrownedMinionModel<>(0.5F, true), new DrownedMinionModel<>(1.0F, true)));
        this.addLayer(new DrownedMinionOuterLayer<>(this));
    }

    public ResourceLocation getTextureLocation(DrownedMinionEntity pEntity) {
        return DROWNED_LOCATION;
    }

    protected void setupRotations(DrownedMinionEntity pEntityLiving, MatrixStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        float f = pEntityLiving.getSwimAmount(pPartialTicks);
        if (f > 0.0F) {
            pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(MathHelper.lerp(f, pEntityLiving.xRot, -10.0F - pEntityLiving.xRot)));
        }

    }
}
