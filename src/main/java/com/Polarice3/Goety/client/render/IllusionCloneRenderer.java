package com.Polarice3.Goety.client.render;

import com.Polarice3.Goety.common.entities.ally.IllusionCloneEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;

public class IllusionCloneRenderer extends BipedRenderer<IllusionCloneEntity, PlayerModel<IllusionCloneEntity>> {
    private final PlayerModel<IllusionCloneEntity> normalModel;
    private final PlayerModel<IllusionCloneEntity> slimModel;

    public IllusionCloneRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new PlayerModel<>(0.0F, false),0.5F);
        this.normalModel = this.getModel();
        this.slimModel = new PlayerModel<>(0.0F, true);
        this.addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
    }

    public void render(IllusionCloneEntity pEntity, float pEntityYaw, float pPartialTicks, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight) {
        this.setModelProperties(pEntity);
        if ((pEntity.getTrueOwner() instanceof AbstractClientPlayerEntity
                && DefaultPlayerSkin.getSkinModelName(pEntity.getTrueOwner().getUUID()).equals("slim") )
                || DefaultPlayerSkin.getSkinModelName(pEntity.getUUID()).equals("slim")) {
            this.model = slimModel;
        } else {
            this.model = normalModel;
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    public ResourceLocation getTextureLocation(IllusionCloneEntity entity) {
        if (entity.getTrueOwner() != null){
            if (entity.getTrueOwner() instanceof AbstractClientPlayerEntity){
                return ((AbstractClientPlayerEntity) entity.getTrueOwner()).getSkinTextureLocation();
            } else {
                return DefaultPlayerSkin.getDefaultSkin(entity.getUUID());
            }
        } else {
            return DefaultPlayerSkin.getDefaultSkin(entity.getUUID());
        }
    }

    private void setModelProperties(IllusionCloneEntity pClientPlayer) {
        PlayerModel<IllusionCloneEntity> playermodel = this.getModel();
        BipedModel.ArmPose bipedmodel$armpose = getArmPose(pClientPlayer, Hand.MAIN_HAND);
        BipedModel.ArmPose bipedmodel$armpose1 = getArmPose(pClientPlayer, Hand.OFF_HAND);
        if (bipedmodel$armpose.isTwoHanded()) {
            bipedmodel$armpose1 = pClientPlayer.getOffhandItem().isEmpty() ? BipedModel.ArmPose.EMPTY : BipedModel.ArmPose.ITEM;
        }

        if (pClientPlayer.getMainArm() == HandSide.RIGHT) {
            playermodel.rightArmPose = bipedmodel$armpose;
            playermodel.leftArmPose = bipedmodel$armpose1;
        } else {
            playermodel.rightArmPose = bipedmodel$armpose1;
            playermodel.leftArmPose = bipedmodel$armpose;
        }
    }

    private static BipedModel.ArmPose getArmPose(IllusionCloneEntity p_241741_0_, Hand p_241741_1_) {
        ItemStack itemstack = p_241741_0_.getItemInHand(p_241741_1_);
        if (itemstack.isEmpty()) {
            return BipedModel.ArmPose.EMPTY;
        } else {
            if (p_241741_0_.getUsedItemHand() == p_241741_1_ && p_241741_0_.getUseItemRemainingTicks() > 0) {
                UseAction useaction = itemstack.getUseAnimation();
                if (useaction == UseAction.BLOCK) {
                    return BipedModel.ArmPose.BLOCK;
                }

                if (useaction == UseAction.BOW) {
                    return BipedModel.ArmPose.BOW_AND_ARROW;
                }

                if (useaction == UseAction.SPEAR) {
                    return BipedModel.ArmPose.THROW_SPEAR;
                }

                if (useaction == UseAction.CROSSBOW && p_241741_1_ == p_241741_0_.getUsedItemHand()) {
                    return BipedModel.ArmPose.CROSSBOW_CHARGE;
                }
            } else if (!p_241741_0_.swinging && itemstack.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack)) {
                return BipedModel.ArmPose.CROSSBOW_HOLD;
            }

            return BipedModel.ArmPose.ITEM;
        }
    }

}
