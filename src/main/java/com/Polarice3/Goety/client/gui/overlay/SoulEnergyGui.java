package com.Polarice3.Goety.client.gui.overlay;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.items.magic.GoldTotemItem;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.SEHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;

/**
 * Gui and implementation based of codes from @baileyholl
 */
public class SoulEnergyGui extends AbstractGui {
    private final Minecraft minecraft;
    private final PlayerEntity player;
    protected int screenWidth;
    protected int screenHeight;

    public SoulEnergyGui(Minecraft pMinecraft, PlayerEntity playerEntity) {
        this.minecraft = pMinecraft;
        this.player = playerEntity;
    }

    public boolean shouldDisplayBar(){
        return SEHelper.getSoulsContainer(player) && MainConfig.SoulGuiShow.get() && (minecraft.gameMode != null && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR);
    }

    public FontRenderer getFont() {
        return this.minecraft.font;
    }

    public void drawHUD(MatrixStack ms, float partialTicks) {
        this.screenWidth = this.minecraft.getWindow().getGuiScaledWidth();
        this.screenHeight = this.minecraft.getWindow().getGuiScaledHeight();
        if(!shouldDisplayBar()) {
            return;
        }

        ItemStack stack = GoldTotemFinder.FindTotem(player);
        int SoulEnergy = 0;
        int SoulEnergyTotal = MainConfig.MaxSouls.get();
        if (SEHelper.getSEActive(player)){
            SoulEnergy = SEHelper.getSESouls(player);
            SoulEnergyTotal = MainConfig.MaxArcaSouls.get();
        } else if (!stack.isEmpty()) {
            if (stack.getTag() != null) {
                SoulEnergy = GoldTotemItem.currentSouls(stack);
            }
        }

        int i = (this.screenWidth/2) + (MainConfig.SoulGuiHorizontal.get());
        int energylength = (int)(117 * (SoulEnergy / (double)SoulEnergyTotal));
        int maxenergy = (int) (117 * (MainConfig.MaxSouls.get() / (double)SoulEnergyTotal));

        int height = this.screenHeight + (MainConfig.SoulGuiVertical.get());

        int offset = (int) ((player.tickCount + partialTicks) % 234);

        if (SEHelper.getSEActive(player)){
            Minecraft.getInstance().textureManager.bind(Goety.location("textures/gui/soul_energy.png"));
            blit(ms, i, height - 9, 0, 9, 128, 9, 128, 90);
            Minecraft.getInstance().textureManager.bind(Goety.location("textures/gui/soul_energy.png"));
            blit(ms, i + 9, height - 9, 9, 18, maxenergy, 9, 128, 90);
        } else {
            Minecraft.getInstance().textureManager.bind(Goety.location("textures/gui/soul_energy.png"));
            blit(ms, i, height - 9, 0, 0, 128, 9, 128, 90);
        }
        Minecraft.getInstance().textureManager.bind(Goety.location("textures/gui/soul_energy_bar.png"));
        blit(ms, i + 9, height - 7, offset, 0, energylength, 5, 234, 5);

        if (MobUtil.isSpellCasting(player)){
            int useDuration = player.getUseItem().getUseDuration();
            int remain = player.getUseItemRemainingTicks();
            float useTime0 = (float) (useDuration - remain) / useDuration;
            int useTime = (int) (117 * useTime0);
            Minecraft.getInstance().textureManager.bind(Goety.location("textures/gui/soul_energy.png"));
            blit(ms, i + 9, height - 9, 9, 27, useTime, 9, 128, 90);
        }

        if (MainConfig.ShowNum.get()) {
            this.minecraft.getProfiler().push("soulenergy");
            String s = "" + SoulEnergy + "/" + "" + SoulEnergyTotal;
            int i1 = i + 36;
            int j1 = height - 8;
            this.getFont().draw(ms, s, (float) i1, (float) j1, 16777215);
            this.minecraft.getProfiler().pop();
        }
    }
}
