package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.SkullMobModel;
import com.Polarice3.Goety.common.entities.hostile.SkullLordEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class SkullLordRenderer extends MobRenderer<SkullLordEntity, SkullMobModel> {
    private static final ResourceLocation LOCATION = new ResourceLocation(Goety.MOD_ID,"textures/entity/skull_lord.png");
    private static final ResourceLocation VULNERABLE = new ResourceLocation(Goety.MOD_ID,"textures/entity/skull_lord_vulnerable.png");
    private static final ResourceLocation CHARGE = new ResourceLocation(Goety.MOD_ID,"textures/entity/skull_lord_charging.png");

    public SkullLordRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new SkullMobModel(0.0F), 0.5F);
    }

    protected int getBlockLightLevel(SkullLordEntity pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(SkullLordEntity pEntity) {
        if (pEntity.isCharging()){
            return CHARGE;
        } else {
            return pEntity.isInvulnerable() ? LOCATION: VULNERABLE;
        }
    }
}
