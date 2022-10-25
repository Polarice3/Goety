package com.Polarice3.Goety.client.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.audio.LocustSound;
import com.Polarice3.Goety.client.gui.overlay.DeadHeartsGui;
import com.Polarice3.Goety.client.gui.overlay.SoulEnergyGui;
import com.Polarice3.Goety.common.entities.bosses.ApostleEntity;
import com.Polarice3.Goety.common.entities.hostile.dead.LocustEntity;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.packets.client.CBagKeyPacket;
import com.Polarice3.Goety.common.network.packets.client.CWandAndBagKeyPacket;
import com.Polarice3.Goety.common.network.packets.client.CWandKeyPacket;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModKeybindings;
import com.Polarice3.Goety.utils.LichdomHelper;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (event.getWorld() instanceof ClientWorld){
            Minecraft minecraft = Minecraft.getInstance();
            if (entity instanceof LocustEntity){
                minecraft.getSoundManager().queueTickingSound(new LocustSound((LocustEntity) entity));
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
    public static void renderFogColors(EntityViewRenderEvent.FogColors event){
        ApostleEntity apostleEntity = null;
        Set<MobEntity> bosses = BossBarEvent.getBosses();
        if (!bosses.isEmpty()){
            for (MobEntity boss : bosses){
                if (boss instanceof ApostleEntity && apostleEntity == null){
                    apostleEntity = (ApostleEntity) boss;
                }
            }
        }
        if (apostleEntity != null && !apostleEntity.removed){
            if (!apostleEntity.isSecondPhase()) {
                event.setRed(event.getRed() * 0.7F);
                event.setGreen(event.getGreen() * 0.5F);
                event.setBlue(event.getBlue() * 0.5F);
            } else {
                event.setRed(event.getRed() * 0.3F);
                event.setGreen(event.getGreen() * 0.3F);
                event.setBlue(event.getBlue() * 0.3F);
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

        final PlayerEntity player = Minecraft.getInstance().player;

        if (player != null) {
            if (SEHelper.getSoulsContainer(player)){
                new SoulEnergyGui(Minecraft.getInstance(), player).drawHUD(event.getMatrixStack(), event.getPartialTicks());
            }
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
    }

}
