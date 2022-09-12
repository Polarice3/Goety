package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.UndeadWolfModel;
import com.Polarice3.Goety.common.entities.ally.UndeadWolfEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class UndeadWolfRenderer extends MobRenderer<UndeadWolfEntity, UndeadWolfModel<UndeadWolfEntity>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/undead_wolf.png");

    public UndeadWolfRenderer(EntityRendererManager p_i47187_1_) {
        super(p_i47187_1_, new UndeadWolfModel<>(), 0.5F);
    }

    protected float getBob(UndeadWolfEntity pLivingBase, float pPartialTicks) {
        return pLivingBase.getTailAngle();
    }
    public ResourceLocation getTextureLocation(UndeadWolfEntity pEntity) {
        return TEXTURE;
    }
}
