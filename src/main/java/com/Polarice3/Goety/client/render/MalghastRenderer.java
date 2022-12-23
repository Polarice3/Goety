package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ModGhastModel;
import com.Polarice3.Goety.common.entities.hostile.cultists.MalghastEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class MalghastRenderer extends MobRenderer<MalghastEntity, ModGhastModel<MalghastEntity>> {
    private static final ResourceLocation GHAST_LOCATION = Goety.location("textures/entity/malghast/malghast.png");
    private static final ResourceLocation GHAST_SHOOTING_LOCATION = Goety.location("textures/entity/malghast/malghast_shooting.png");
    private static final ResourceLocation GHAST_SCREAMING_LOCATION = Goety.location("textures/entity/malghast/malghast_scream.png");

    public MalghastRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new ModGhastModel<>(), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(MalghastEntity pEntity) {
        if (pEntity.isCharging()){
            return GHAST_SHOOTING_LOCATION;
        } else if (pEntity.hurtTime > 0 || pEntity.deathTime > 0){
            return GHAST_SCREAMING_LOCATION;
        }
        return GHAST_LOCATION;
    }

    protected void scale(MalghastEntity pLivingEntity, MatrixStack pMatrixStack, float pPartialTickTime) {
        float f = 2.0F;
        pMatrixStack.scale(f, f, f);
    }
}
