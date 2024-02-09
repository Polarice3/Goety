package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.PlayerZombieModel;
import com.Polarice3.Goety.common.entities.ally.JungleZombieMinionEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.util.ResourceLocation;

public class JungleZombieRenderer extends BipedRenderer<JungleZombieMinionEntity, PlayerZombieModel<JungleZombieMinionEntity>> {
   protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/servants/zombie/jungle_zombie_servant.png");

   public JungleZombieRenderer(EntityRendererManager entityRendererManager) {
      super(entityRendererManager, new PlayerZombieModel<>(0.0F), 0.5F);
      this.addLayer(new BipedArmorLayer<>(this, new BipedModel(0.5F), new BipedModel(1.02F)));
   }

   public ResourceLocation getTextureLocation(JungleZombieMinionEntity p_113771_) {
      return TEXTURE;
   }

   protected boolean isShaking(JungleZombieMinionEntity p_113773_) {
      return super.isShaking(p_113773_) || p_113773_.isUnderWaterConverting();
   }
}