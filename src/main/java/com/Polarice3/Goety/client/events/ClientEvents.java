package com.Polarice3.Goety.client.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.audio.LocustSound;
import com.Polarice3.Goety.client.gui.overlay.SoulEnergyGui;
import com.Polarice3.Goety.common.entities.hostile.dead.LocustEntity;
import com.Polarice3.Goety.utils.GoldTotemFinder;
import com.Polarice3.Goety.utils.LichdomHelper;
import com.Polarice3.Goety.utils.SEHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Goety.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (event.getWorld() instanceof ClientWorld){
            if (entity instanceof LocustEntity){
                Minecraft minecraft = Minecraft.getInstance();
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

        if (player != null && LichdomHelper.isLich(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void renderSoulEnergyHUD(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;

        final PlayerEntity player = Minecraft.getInstance().player;

        if (player != null) {
            if (SEHelper.getSEActive(player)){
                new SoulEnergyGui(Minecraft.getInstance(), player).drawHUD(event.getMatrixStack());
            } else if (!GoldTotemFinder.FindTotem(player).isEmpty()) {
                new SoulEnergyGui(Minecraft.getInstance(), player).drawHUD(event.getMatrixStack());
            }
        }
    }
}
