package com.Polarice3.Goety.client.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import static net.minecraft.client.gui.AbstractGui.blit;

public class BossBarEvent {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(Goety.MOD_ID, "textures/gui/boss_bar.png");
    protected static final ResourceLocation BOSS_BAR_1 = new ResourceLocation(Goety.MOD_ID, "textures/gui/boss_bar_1.png");
    private static Minecraft minecraft;
    public static final Set<MobEntity> BOSSES = Collections.newSetFromMap(new WeakHashMap<>());

    @SubscribeEvent
    public static void renderBossBar(RenderGameOverlayEvent.BossInfo event){
        minecraft = Minecraft.getInstance();
        if (MainConfig.SpecialBossBar.get()) {
            if (!BOSSES.isEmpty()) {
                int i = minecraft.getWindow().getGuiScaledWidth();
                for (MobEntity boss : BOSSES) {
                    if (event.getBossInfo().getId() == boss.getUUID()) {
                        event.setCanceled(true);
                        int k = i / 2 - 100;
                        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                        drawBar(event.getMatrixStack(), k, event.getY(), event.getPartialTicks(), boss);
                        ITextComponent itextcomponent = boss.getDisplayName();
                        int l = minecraft.font.width(itextcomponent);
                        int i1 = i / 2 - l / 2;
                        minecraft.font.drawShadow(event.getMatrixStack(), itextcomponent, (float) i1, (float) event.getY() - 9, 16777215);
                        if (event.getY() >= minecraft.getWindow().getGuiScaledHeight() / 3) {
                            break;
                        }
                        event.setIncrement(12 + minecraft.font.lineHeight);
                    }
                }

            }
        }

    }

    private static void drawBar(MatrixStack pPoseStack, int pX, int pY, float partialTicks, MobEntity pEntity) {
        float percent = pEntity.getHealth() / pEntity.getMaxHealth();
        int i = (int) (percent * 182.0F);
        int pX2 = pX + 9;
        int pY2 = pY + 4;
        int offset = (int) ((pEntity.tickCount + partialTicks) % 364);
        if (percent <= 0.25F){
            offset = (int) (((pEntity.tickCount + partialTicks) * 4) % 364);
        } else if (percent <= 0.5F){
            offset = (int) (((pEntity.tickCount + partialTicks) * 2) % 364);
        }
        if (pEntity instanceof ApostleEntity) {
            boolean flag = false;
            ApostleEntity apostleEntity = (ApostleEntity) pEntity;
            if (apostleEntity.isSecondPhase()) {
                flag = true;
            }
            int shake = 0;
            int damage = 36;
            if (i > 0) {
                minecraft.getTextureManager().bind(BOSS_BAR_1);
                blit(pPoseStack, pX2, pY2, offset, 0, i, 8, 364, 64);
                if (pEntity.hurtTime >= 5) {
                    damage = 32 + pEntity.getRandom().nextInt(pEntity.hurtTime);
                    shake = pEntity.getRandom().nextInt(pEntity.hurtTime);
                    minecraft.getTextureManager().bind(TEXTURE);
                    blit(pPoseStack, pX2, pY2, shake, damage, i, 8, 256, 256);
                }
            }
            minecraft.getTextureManager().bind(TEXTURE);
            blit(pPoseStack, pX, pY, 0, flag ? 16 : 0, 200, 16, 256, 256);
        } else if (pEntity instanceof VizierEntity) {
            int shake = 0;
            int damage = 100;
            if (i > 0) {
                minecraft.getTextureManager().bind(BOSS_BAR_1);
                blit(pPoseStack, pX2, pY2, offset, 8, i, 8, 364, 64);
                if (pEntity.hurtTime >= 5) {
                    damage = 64 + pEntity.getRandom().nextInt(pEntity.hurtTime);
                    shake = pEntity.getRandom().nextInt(pEntity.hurtTime);
                    minecraft.getTextureManager().bind(TEXTURE);
                    blit(pPoseStack, pX2, pY2, shake, damage, i, 8, 256, 256);
                }
            }
            minecraft.getTextureManager().bind(TEXTURE);
            blit(pPoseStack, pX, pY, 0, 48, 200, 16, 256, 256);
        }
    }

    public static void addBoss(MobEntity mob){
        BOSSES.add(mob);
    }

    public static void removeBoss(MobEntity mob){
        BOSSES.remove(mob);
    }

    public static Set<MobEntity> getBosses(){
        return BOSSES;
    }

}
