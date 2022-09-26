package com.Polarice3.Goety.client.gui.overlay;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.init.ModEffects;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class DeadHeartsGui extends IngameGui {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/gui/dead_hearts.png");
    private final Minecraft minecraft;
    private final PlayerEntity player;
    private int previousHealth;
    private long lastSystemTime = 0;
    protected int screenWidth;
    protected int screenHeight;

    public DeadHeartsGui(Minecraft pMinecraft, PlayerEntity playerEntity) {
        super(pMinecraft);
        this.minecraft = pMinecraft;
        this.player = playerEntity;
    }

    public boolean shouldDisplayBar(){
        return player.hasEffect(ModEffects.DESICCATE.get());
    }

    public void drawHearts(MatrixStack ms) {
        this.screenWidth = this.minecraft.getWindow().getGuiScaledWidth();
        this.screenHeight = this.minecraft.getWindow().getGuiScaledHeight();
        if(!shouldDisplayBar()) {
            return;
        }
        Minecraft.getInstance().getProfiler().push("dead_hearts");

        int left_height = 39;
        int currentHealth = MathHelper.ceil(player.getHealth());
        int ticks = Minecraft.getInstance().gui.getGuiTicks();
        long systemTime = Util.getMillis();

        if (currentHealth < previousHealth && player.invulnerableTime > 0) {
            lastSystemTime = systemTime;
        } else if (currentHealth > previousHealth && player.invulnerableTime > 0) {
            lastSystemTime = systemTime;
        }

        if (Util.getMillis() - this.lastSystemTime > 1000L) {
            this.previousHealth = currentHealth;
            this.lastSystemTime = Util.getMillis();
        }

        ModifiableAttributeInstance attribute = player.getAttribute(Attributes.MAX_HEALTH);
        float maxHealth = (float) attribute.getValue();

        Random random = new Random();
        random.setSeed(ticks * 312871L);

        int left = screenWidth / 2 - 91;
        int top = screenHeight - left_height;

        int regen = -1;
        if (player.hasEffect(Effects.REGENERATION)) {
            regen = ticks % 25;
        }

        final int finalTop = 0;
        int margin = 0;
        int background = 9;

        RenderSystem.pushTextureAttributes();
        for (int i = MathHelper.ceil(maxHealth / 2.0F) - 1; i >= 0; --i) {
            int x = left + i % 10 * 8;
            int y = top;

            if (currentHealth <= 4) {
                y += random.nextInt(2);
            }
            if (i == regen) {
                y -= 2;
            }
            RenderSystem.disableAlphaTest();
            RenderSystem.enableBlend();
            drawHearts(ms, x, y, margin, background, 9, 9);
            if (i * 2 + 1 < currentHealth) {
                drawHearts(ms, x, y, margin, finalTop, 9, 9);
            }
            else if (i * 2 + 1 == currentHealth) {
                drawHearts(ms, x, y, margin + 9, finalTop, 9, 9);
            }
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
        }

        RenderSystem.popAttributes();
        Minecraft.getInstance().getProfiler().pop();
    }

    private void drawHearts(MatrixStack stackin, int x, int y, int textureX, int textureY, int width, int height) {
        Minecraft.getInstance().textureManager.bind(TEXTURE);
        blit(stackin, x, y, textureX, textureY, width, height);
    }

}
