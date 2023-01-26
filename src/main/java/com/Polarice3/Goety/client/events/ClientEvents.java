package com.Polarice3.Goety.client.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.MainConfig;
import com.Polarice3.Goety.client.audio.BossLoopMusic;
import com.Polarice3.Goety.client.audio.FelFlySound;
import com.Polarice3.Goety.client.audio.LocustSound;
import com.Polarice3.Goety.client.gui.overlay.DeadHeartsGui;
import com.Polarice3.Goety.client.gui.overlay.SoulEnergyGui;
import com.Polarice3.Goety.common.entities.ally.FelFlyEntity;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.common.entities.bosses.VizierEntity;
import com.Polarice3.Goety.common.entities.hostile.dead.LocustEntity;
import com.Polarice3.Goety.common.items.equipment.NetheriteBowItem;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.packets.client.*;
import com.Polarice3.Goety.common.tileentities.ArcaTileEntity;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModKeybindings;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.LichdomHelper;
import com.Polarice3.Goety.utils.SEHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (event.getWorld() instanceof ClientWorld){
            Minecraft minecraft = Minecraft.getInstance();
            SoundHandler soundHandler = minecraft.getSoundManager();
            if (entity instanceof LocustEntity){
                soundHandler.queueTickingSound(new LocustSound((LocustEntity) entity));
            }
            if (entity instanceof FelFlyEntity){
                soundHandler.queueTickingSound(new FelFlySound((FelFlyEntity) entity));
            }
            if (MainConfig.BossMusic.get()) {
                if (entity instanceof ApostleEntity && !((ApostleEntity) entity).isNoAi()) {
                    minecraft.getMusicManager().stopPlaying();
                    minecraft.gui.setNowPlaying(new TranslationTextComponent("goety.apostle_theme"));
                    soundHandler.play(new BossLoopMusic(ModSounds.APOSTLE_THEME.get(), (ApostleEntity) entity));
                }
                if (entity instanceof VizierEntity && !((VizierEntity) entity).isNoAi()) {
                    minecraft.getMusicManager().stopPlaying();
                    minecraft.gui.setNowPlaying(new TranslationTextComponent("goety.vizier_theme"));
                    soundHandler.play(new BossLoopMusic(ModSounds.VIZIER_THEME.get(), (VizierEntity) entity));
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderLichHUD(final RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.FOOD) {
            return;
        }

        final PlayerEntity player = Minecraft.getInstance().player;

        if (player != null) {
            if (LichdomHelper.isLich(player)){
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void renderDeadHearts(RenderGameOverlayEvent.Post event){
        Minecraft minecraft = Minecraft.getInstance();
        PlayerEntity player = minecraft.player;

        if (event.getType() == RenderGameOverlayEvent.ElementType.HEALTH
                && player.hasEffect(ModEffects.DESICCATE.get())) {
            new DeadHeartsGui(minecraft, player).drawHearts(event.getMatrixStack());
        }
    }

    @SubscribeEvent
    public static void renderSoulEnergyHUD(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft minecraft = Minecraft.getInstance();
        final PlayerEntity player = minecraft.player;

        if (player != null) {
            if (SEHelper.getSoulsContainer(player)){
                new SoulEnergyGui(Minecraft.getInstance(), player).drawHUD(event.getMatrixStack(), event.getPartialTicks());
            }
            RayTraceResult hitResult = minecraft.hitResult;
            FontRenderer fontRenderer = minecraft.font;
            if (minecraft.level != null) {
                if (hitResult instanceof BlockRayTraceResult) {
                    BlockRayTraceResult blockRayTraceResult = ((BlockRayTraceResult) hitResult);
                    TileEntity tileEntity = minecraft.level.getBlockEntity(blockRayTraceResult.getBlockPos());
                    if (tileEntity instanceof ArcaTileEntity) {
                        ArcaTileEntity arcaTile = (ArcaTileEntity) tileEntity;
                        if (arcaTile.getPlayer() == player && SEHelper.getSEActive(player) && (player.isShiftKeyDown() || player.isCrouching())) {
                            RenderSystem.pushMatrix();
                            RenderSystem.translatef((float)(minecraft.getWindow().getGuiScaledWidth() / 2), (float)(minecraft.getWindow().getGuiScaledHeight() - 68), 0.0F);
                            RenderSystem.enableBlend();
                            RenderSystem.defaultBlendFunc();
                            int SoulEnergy = SEHelper.getSESouls(player);
                            int SoulEnergyTotal = MainConfig.MaxArcaSouls.get();
                            String s = "" + SoulEnergy + "/" + "" + SoulEnergyTotal;
                            int l = fontRenderer.width(s);
                            fontRenderer.drawShadow(event.getMatrixStack(), s, (float)(-l / 2), -4.0F, 0xFFFFFF);
                            RenderSystem.disableBlend();
                            RenderSystem.popMatrix();
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void fogDensityEvent(EntityViewRenderEvent.FogDensity event){
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null){
            if (player.hasEffect(ModEffects.FEL_VISION.get()) && !player.hasEffect(Effects.NIGHT_VISION)){
                event.setDensity(event.getDensity() * 2);
                event.setCanceled(true);
            }
        }
    }

    private static float darkenWorldAmount;
    private static float darkenWorldAmountO;
    private static ApostleEntity apostleEntity;

/*    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event){
        if (apostleEntity == null) {
            if (!BossBarEvent.getBosses().isEmpty()) {
                for (MobEntity mob : BossBarEvent.getBosses()) {
                    if (mob instanceof ApostleEntity && mob.isAlive()) {
                        apostleEntity = (ApostleEntity) mob;
                    }
                }
            }
        }
        darkenWorldAmountO = darkenWorldAmount;
        if (apostleEntity != null) {
            darkenWorldAmount += 0.05F;
            if (darkenWorldAmount > 1.0F) {
                darkenWorldAmount = 1.0F;
            }
            if (apostleEntity.isDeadOrDying() || !apostleEntity.isAlive()){
                apostleEntity = null;
            }
        } else if (darkenWorldAmount > 0.0F) {
            darkenWorldAmount -= 0.0125F;
        }
    }



    @SubscribeEvent
    public static void fogColors(EntityViewRenderEvent.FogColors event){
        float fogRed = event.getRed();
        float fogGreen = event.getGreen();
        float fogBlue = event.getBlue();
        float modifier = (float) MathHelper.lerp(event.getRenderPartialTicks(), darkenWorldAmountO, darkenWorldAmount);
        if (apostleEntity != null) {
            if (apostleEntity.isSecondPhase()){
                fogRed = fogRed * (1.0F - modifier) + fogRed * 0.5F * modifier;
                fogGreen = fogGreen * (1.0F - modifier) + fogGreen * 0.3F * modifier;
                fogBlue = fogBlue * (1.0F - modifier) + fogBlue * 0.3F * modifier;
            } else {
                fogRed = fogRed * (1.0F - modifier) + fogRed * 0.8F * modifier;
                fogGreen = fogGreen * (1.0F - modifier) + fogGreen * 0.6F * modifier;
                fogBlue = fogBlue * (1.0F - modifier) + fogBlue * 0.6F * modifier;
            }
        }
        event.setRed(fogRed);
        event.setGreen(fogGreen);
        event.setBlue(fogBlue);
    }*/

    @SubscribeEvent
    public static void offsetFOV(FOVUpdateEvent event){
        float f = 1.0F;
        if (event.getEntity().isUsingItem() && event.getEntity().getUseItem().getItem() instanceof NetheriteBowItem) {
            int i = event.getEntity().getTicksUsingItem();
            float f1 = (float)i / NetheriteBowItem.getBowTime();
            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 = f1 * f1;
            }

            f *= 1.0F - f1 * 0.15F;
            event.setNewfov(f);
        }
    }

    @SubscribeEvent
    public static void KeyInputs(InputEvent.KeyInputEvent event){
        Minecraft MINECRAFT = Minecraft.getInstance();

        if (ModKeybindings.keyBindings[0].isDown() && MINECRAFT.isWindowActive()){
            ModNetwork.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CWandKeyPacket());
        }
        if (ModKeybindings.keyBindings[1].isDown() && MINECRAFT.isWindowActive()){
            ModNetwork.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CWandAndBagKeyPacket());
        }
        if (ModKeybindings.keyBindings[2].isDown() && MINECRAFT.isWindowActive()){
            ModNetwork.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CBagKeyPacket());
        }
        if (ModKeybindings.keyBindings[3].isDown() && MINECRAFT.isWindowActive()){
            ModNetwork.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CStopAttackPacket());
        }
        if (ModKeybindings.keyBindings[4].isDown() && MINECRAFT.isWindowActive()){
            ModNetwork.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CLichKissPacket());
        }
        if (ModKeybindings.keyBindings[5].isDown() && MINECRAFT.isWindowActive()){
            ModNetwork.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CMagnetPacket());
        }
    }

}
