package com.Polarice3.Goety.client.gui.screen.inventory;
/*

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.inventory.container.SoulForgeContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoulForgeScreen extends ContainerScreen<SoulForgeContainer> {
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(Goety.MOD_ID, "textures/gui/soulforge.png");

    public SoulForgeScreen(SoulForgeContainer p_i51097_1_, PlayerInventory p_i51097_2_, ITextComponent p_i51097_3_) {
        super(p_i51097_1_, p_i51097_2_, p_i51097_3_);
    }

    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI_TEXTURES);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        int k = this.container.getFuelTotal();
        int f = MathHelper.clamp((18 * k + 20 - 1) / 20, 0, 18);
        if (f > 0) {
            this.blit(matrixStack, i + 144, j + 75, 200, 0, 4, f);
        }

        int l = this.container.getCookProgressionScaled();
        this.blit(matrixStack, i + 77, j + 19, 176, 14, l + 1, 16);
    }
}
*/
