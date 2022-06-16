package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.layers.FallenEyesLayer;
import com.Polarice3.Goety.common.entities.hostile.dead.FallenEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.util.ResourceLocation;

public class FallenRenderer extends BipedRenderer<FallenEntity, ZombieModel<FallenEntity>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/dead/fallen.png");

    public FallenRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new ZombieModel<>(0.0F, false),0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ZombieModel<>(0.5F, true), new ZombieModel<>(1.0F, true)));
        this.addLayer(new FallenEyesLayer<>(this));
    }

    public ResourceLocation getTextureLocation(FallenEntity entity) {
        return TEXTURE;
    }
}
