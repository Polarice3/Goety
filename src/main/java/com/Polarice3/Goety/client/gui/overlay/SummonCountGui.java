package com.Polarice3.Goety.client.gui.overlay;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

public class SummonCountGui extends AbstractGui {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/gui/summoncount.png");
    private final Minecraft minecraft;
    private final PlayerEntity player;
    protected int screenWidth;
    protected int screenHeight;

    public SummonCountGui(Minecraft pMinecraft, PlayerEntity playerEntity) {
        this.minecraft = pMinecraft;
        this.player = playerEntity;
    }

    public boolean shouldDisplayBar(){
        ItemStack stack = GoldTotemFinder.FindTotem(player);
        return !stack.isEmpty();
    }

    public FontRenderer getFont() {
        return this.minecraft.font;
    }

    public void drawHUD(MatrixStack ms, float pt) {
        this.screenWidth = this.minecraft.getWindow().getGuiScaledWidth();
        this.screenHeight = this.minecraft.getWindow().getGuiScaledHeight();
        if(!shouldDisplayBar()) {
            return;
        }

        ItemStack stack = GoldTotemFinder.FindTotem(player);
        int ZombieCount = 0;
        int i = (this.screenWidth/2) + 100;

        int height = this.screenHeight - 3;

        Minecraft.getInstance().textureManager.bind(new ResourceLocation(Goety.MOD_ID, "textures/gui/summoncount.png"));
        blit(ms,i, height - 9, 0, 0, 128,9, 128, 9);
        this.minecraft.getProfiler().push("zombiecount");
        String s = "" + ZombieCount;
        int i1 = i + 36;
        int j1 = height - 8;
        this.getFont().draw(ms, s, (float) i1, (float) j1, 16777215);
        this.minecraft.getProfiler().pop();
    }

}
