package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.client.model.ACArmorModel;
import com.Polarice3.Goety.client.model.VillagerMinionModel;
import com.Polarice3.Goety.common.entities.hostile.cultists.ZombieVillagerMinionEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

public class ZombieVillagerMinionRenderer extends BipedRenderer<ZombieVillagerMinionEntity, VillagerMinionModel<ZombieVillagerMinionEntity>> {

    public ZombieVillagerMinionRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new VillagerMinionModel<>(0.0F, 0.5F),0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new ACArmorModel<>(0.5F), new ACArmorModel<>(1.0F)));
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieVillagerMinionEntity entity) {
        return entity.getResourceLocation();
    }
}
