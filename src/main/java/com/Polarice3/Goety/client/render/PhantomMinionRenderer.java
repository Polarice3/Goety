package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.layers.PhantomMinionBandsLayer;
import com.Polarice3.Goety.client.render.layers.PhantomMinionEyesLayer;
import com.Polarice3.Goety.common.entities.ally.PhantomMinionEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.PhantomModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class PhantomMinionRenderer extends MobRenderer<PhantomMinionEntity, PhantomModel<PhantomMinionEntity>> {
    private static final ResourceLocation PHANTOM_LOCATION = new ResourceLocation("textures/entity/phantom.png");
    private static final ResourceLocation NETHER_PHANTOM = Goety.location("textures/entity/nether_phantom.png");

    public PhantomMinionRenderer(EntityRendererManager p_i48829_1_) {
        super(p_i48829_1_, new PhantomModel<>(), 0.75F);
        this.addLayer(new PhantomMinionEyesLayer<>(this));
        this.addLayer(new PhantomMinionBandsLayer(this));
    }

    public ResourceLocation getTextureLocation(PhantomMinionEntity pEntity) {
        if (pEntity.isNether()){
            return NETHER_PHANTOM;
        } else {
            return PHANTOM_LOCATION;
        }
    }

    protected void scale(PhantomMinionEntity pLivingEntity, MatrixStack pMatrixStack, float pPartialTickTime) {
        int i = pLivingEntity.getPhantomSize();
        float f = 1.0F + 0.15F * (float)i;
        pMatrixStack.scale(f, f, f);
        pMatrixStack.translate(0.0D, 1.3125D, 0.1875D);
    }

    protected void setupRotations(PhantomMinionEntity pEntityLiving, MatrixStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(pEntityLiving, pMatrixStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        pMatrixStack.mulPose(Vector3f.XP.rotationDegrees(pEntityLiving.xRot));
    }
}
