package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.ZPiglinModel;
import com.Polarice3.Goety.common.entities.neutral.ZPiglinBruteMinionEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;

public class ZPiglinRenderer extends BipedRenderer<MobEntity, ZPiglinModel<MobEntity>> {
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/cultist/zpiglinminion.png");
    protected static final ResourceLocation TEXTURE2 = Goety.location("textures/entity/cultist/zpiglinbruteminion.png");

    public ZPiglinRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, createModel(), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
        this.addLayer(new BipedArmorLayer<>(this, new BipedModel(0.5F), new BipedModel(1.02F)));
    }

    private static ZPiglinModel<MobEntity> createModel() {
        ZPiglinModel<MobEntity> piglinmodel = new ZPiglinModel<>(0.0F, 64, 64);
        piglinmodel.earLeft.visible = false;
        return piglinmodel;
    }

    public ResourceLocation getTextureLocation(MobEntity entity) {
        if (entity instanceof ZPiglinBruteMinionEntity){
            return TEXTURE2;
        } else {
            return TEXTURE;
        }
    }

}
