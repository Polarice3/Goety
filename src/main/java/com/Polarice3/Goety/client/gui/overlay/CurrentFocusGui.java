package com.Polarice3.Goety.client.gui.overlay;

import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.gui.screen.inventory.FocusRadialMenuScreen;
import com.Polarice3.Goety.utils.WandUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameType;

public class CurrentFocusGui extends AbstractGui {
    private final Minecraft minecraft;
    private final PlayerEntity player;
    protected int screenWidth;
    protected int screenHeight;

    public CurrentFocusGui(Minecraft pMinecraft, PlayerEntity playerEntity) {
        this.minecraft = pMinecraft;
        this.player = playerEntity;
    }

    public boolean shouldDisplayBar(){
        return !WandUtil.findFocus(player).isEmpty() && (minecraft.gameMode != null && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) && !(minecraft.screen instanceof FocusRadialMenuScreen) && MainConfig.FocusGuiShow.get();
    }

    public void drawHUD(MatrixStack ms) {
        this.screenWidth = this.minecraft.getWindow().getGuiScaledWidth();
        this.screenHeight = this.minecraft.getWindow().getGuiScaledHeight();
        if(!this.shouldDisplayBar()) {
            return;
        }

        ms.pushPose();
        ms.scale(1.0F, 1.0F, 1.0F);
        if (WandUtil.findFocus(minecraft.player) != null) {
            minecraft.getItemRenderer().renderGuiItem(WandUtil.findFocus(minecraft.player), ((screenWidth - 16) / 2) + MainConfig.FocusGuiHorizontal.get(), (screenHeight - 52) + MainConfig.FocusGuiVertical.get());
            minecraft.getItemRenderer().renderGuiItemDecorations(minecraft.font, WandUtil.findFocus(minecraft.player), ((screenWidth - 16) / 2) + MainConfig.FocusGuiHorizontal.get(), (screenHeight - 52) + MainConfig.FocusGuiVertical.get());        }
        ms.popPose();
    }
}
