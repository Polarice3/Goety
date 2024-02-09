package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.render.layers.SkeletonMinionClothingLayer;
import com.Polarice3.Goety.common.entities.ally.AbstractSMEntity;
import com.Polarice3.Goety.common.entities.ally.MossySkeletonMinionEntity;
import com.Polarice3.Goety.common.entities.ally.StrayMinionEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;

public class SkeletonMinionRenderer extends BipedRenderer<AbstractSMEntity, SkeletonModel<AbstractSMEntity>> {
    private static final ResourceLocation TEXTURES = new ResourceLocation(Goety.MOD_ID, "textures/entity/servants/skeleton_minion.png");
    private static final ResourceLocation STRAY = new ResourceLocation(Goety.MOD_ID, "textures/entity/servants/stray_minion.png");
    private static final ResourceLocation MOSSY = new ResourceLocation(Goety.MOD_ID, "textures/entity/servants/skeleton/mossy_skeleton_minion.png");

    public SkeletonMinionRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SkeletonModel<>(), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new SkeletonModel<>(0.5F, true), new SkeletonModel<>(1.0F, true)));
        this.addLayer(new SkeletonMinionClothingLayer<>(this));
    }

    public ResourceLocation getTextureLocation(AbstractSMEntity entity) {
        if (entity instanceof StrayMinionEntity){
            return STRAY;
        } else if (entity instanceof MossySkeletonMinionEntity){
            return MOSSY;
        } else {
            return TEXTURES;
        }
    }


}
