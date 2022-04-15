package com.Polarice3.Goety.client.gui.overlay;

/*import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.lichdom.ILichdom;
import com.Polarice3.Goety.utils.LichdomHelper;
import com.Polarice3.Goety.utils.LichdomUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import static net.minecraftforge.client.gui.ForgeIngameGui.right_height;

public class LichLevelGui extends AbstractGui {
    private final Minecraft minecraft;
    private final PlayerEntity player;
    protected int screenWidth;
    protected int screenHeight;

    public LichLevelGui(Minecraft pMinecraft, PlayerEntity playerEntity) {
        this.minecraft = pMinecraft;
        this.player = playerEntity;
    }

    public boolean shouldDisplayBar(){
        return LichdomUtil.isLich(player) && !player.isSpectator();
    }

    public void drawHUD(MatrixStack mStack) {
        this.screenWidth = this.minecraft.getWindow().getGuiScaledWidth();
        this.screenHeight = this.minecraft.getWindow().getGuiScaledHeight();
        if (!shouldDisplayBar()){
            return;
        }

        RenderSystem.enableBlend();
        int left = screenWidth / 2 + 91;
        int top = screenHeight - right_height;
        right_height += 10;
        int x = left - 70 - 9;
        int x2 = left + 70 + 9;

        ILichdom lichdom = LichdomHelper.getCapability(player);
        int level = lichdom.getLichLevel();
        int total = 80;
        total = (int)((total) * (level / (double)20));

        Minecraft.getInstance().textureManager.bind(new ResourceLocation(Goety.MOD_ID, "textures/gui/lichlevelslots.png"));
        blit(mStack, x, top, 0, 0, 80, 9, 80, 9);
        Minecraft.getInstance().textureManager.bind(new ResourceLocation(Goety.MOD_ID, "textures/gui/lichlevel.png"));
        blit(mStack, x2, top, 0, 0, -total, 9, 80, 9);
    }

}*/
