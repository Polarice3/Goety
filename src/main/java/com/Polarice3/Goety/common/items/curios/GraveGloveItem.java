package com.Polarice3.Goety.common.items.curios;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.model.GloveModel;
import com.Polarice3.Goety.common.items.capability.ModCurioCapability;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class GraveGloveItem extends GloveItem{
    protected static final ResourceLocation TEXTURE = Goety.location("textures/entity/grave_glove.png");

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT unused) {
        return new ModCurioCapability(stack) {
            private Object model;

            @Override
            public boolean canRender(String identifier, int index, LivingEntity livingEntity) {
                return true;
            }

            @Override
            public void render(String identifier, int index, MatrixStack matrixStack,
                               IRenderTypeBuffer renderTypeBuffer, int light, LivingEntity livingEntity,
                               float limbSwing, float limbSwingAmount, float partialTicks,
                               float ageInTicks, float netHeadYaw, float headPitch) {

                if (!(this.model instanceof GloveModel)) {
                    this.model = new GloveModel();
                }
                GloveModel glove = (GloveModel) this.model;
                glove.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
                glove.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                ICurio.RenderHelper.followBodyRotations(livingEntity, glove);
                IVertexBuilder vertexBuilder = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, glove.renderType(TEXTURE), false, false);
                glove.renderToBuffer(matrixStack, vertexBuilder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        };
    }

    public final void renderFPGraveGlove(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, PlayerEntity player) {
        if (!player.isSpectator()) {
            GloveModel model = new GloveModel();

            ModelRenderer arm = player.getMainArm() == HandSide.LEFT ? model.leftArm : model.rightArm;

            model.setAllVisible(false);
            arm.visible = true;

            model.crouching = false;
            model.attackTime = model.swimAmount = 0;
            model.setupAnim(player, 0, 0, 0, 0, 0);
            arm.xRot = 0;

            RenderType renderType = model.renderType(TEXTURE);
            IVertexBuilder builder = ItemRenderer.getFoilBuffer(buffer, renderType, false, false);
            arm.render(matrixStack, builder, light, OverlayTexture.NO_OVERLAY);
        }
    }
}
