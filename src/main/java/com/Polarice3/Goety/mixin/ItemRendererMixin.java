package com.Polarice3.Goety.mixin;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.items.magic.SoulWand;
import com.Polarice3.Goety.utils.SEHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @Shadow
    private void fillRect(BufferBuilder p_115153_, int p_115154_, int p_115155_, int p_115156_, int p_115157_, int p_115158_, int p_115159_, int p_115160_, int p_115161_) {
    }

    @Inject(method = "renderGuiItemDecorations*", at = @At(value = "TAIL"))
    public void renderFocusCooldown(FontRenderer font, ItemStack stack, int one, int two, CallbackInfo ci) {
        Item item = stack.getItem();
        ClientPlayerEntity localplayer = Minecraft.getInstance().player;
        if (localplayer != null) {
            if (item instanceof SoulWand && MainConfig.ShowWandCooldown.get()) {
                float f;
                if (SoulWand.getFocus(stack) != null && SEHelper.getFocusCoolDown(localplayer).isOnCooldown(SoulWand.getFocus(stack).getItem())) {
                    Item focus = SoulWand.getFocus(stack).getItem();
                    f = SEHelper.getFocusCoolDown(localplayer).getCooldownPercent(focus);
                } else {
                    f = 0;
                }
                renderFocusCooldown(one, two, f);
            } else {
                float f;
                if (SEHelper.getFocusCoolDown(localplayer).isOnCooldown(item)) {
                    f = SEHelper.getFocusCoolDown(localplayer).getCooldownPercent(item);
                } else {
                    f = 0;
                }
                renderFocusCooldown(one, two, f);
            }
        }
    }


    private void renderFocusCooldown(int one, int two, float f) {
        if (f > 0.0F) {
            RenderSystem.disableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            Tessellator tesselator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            fillRect(bufferbuilder, one, two + MathHelper.floor(16.0F * (1.0F - f)), 16, MathHelper.ceil(16.0F * f), 255, 255, 255, 127);
            RenderSystem.enableTexture();
            RenderSystem.enableDepthTest();
        }
    }
}
