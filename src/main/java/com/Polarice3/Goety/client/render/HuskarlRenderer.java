package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ZombieMinionModel;
import com.Polarice3.Goety.client.render.layers.HuskarlGarmetLayer;
import com.Polarice3.Goety.client.render.layers.ZombieMinionClothingLayer;
import com.Polarice3.Goety.common.entities.ally.ZombieMinionEntity;
import com.Polarice3.Goety.common.entities.hostile.HuskarlEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.util.ResourceLocation;

public class HuskarlRenderer extends BipedRenderer<HuskarlEntity, ZombieModel<HuskarlEntity>> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/entity/huskarl.png");

    public HuskarlRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new ZombieModel<>(0.0F, false),0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ZombieModel<>(0.5F, true), new ZombieModel<>(1.0F, true)));
        this.addLayer(new HuskarlGarmetLayer(this));
    }

    public ResourceLocation getTextureLocation(HuskarlEntity entity) {
        return TEXTURE;
    }
}
