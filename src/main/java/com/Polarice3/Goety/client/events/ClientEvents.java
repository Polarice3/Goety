package com.Polarice3.Goety.client.events;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.client.audio.IceStormSound;
import com.Polarice3.Goety.client.audio.LocustSound;
import com.Polarice3.Goety.client.gui.overlay.SoulEnergyGui;
import com.Polarice3.Goety.common.entities.hostile.cultists.BeldamEntity;
import com.Polarice3.Goety.common.entities.hostile.dead.IDeadMob;
import com.Polarice3.Goety.common.entities.hostile.dead.LocustEntity;
import com.Polarice3.Goety.common.entities.projectiles.IceStormEntity;
import com.Polarice3.Goety.common.network.ModNetwork;
import com.Polarice3.Goety.common.network.packets.client.CBagKeyPacket;
import com.Polarice3.Goety.common.network.packets.client.CWandAndBagKeyPacket;
import com.Polarice3.Goety.common.network.packets.client.CWandKeyPacket;
import com.Polarice3.Goety.init.ModBlocks;
import com.Polarice3.Goety.init.ModEffects;
import com.Polarice3.Goety.init.ModKeybindings;
import com.Polarice3.Goety.utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
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
            if (entity instanceof LocustEntity){
                minecraft.getSoundManager().queueTickingSound(new LocustSound((LocustEntity) entity));
            }
            if (entity instanceof IceStormEntity){
                minecraft.getSoundManager().queueTickingSound(new IceStormSound((IceStormEntity) entity));
            }
        }
    }

    @SubscribeEvent
    public static void onBreakingBlock(BlockEvent.BreakEvent event){
        PlayerEntity player = event.getPlayer();
        if (player.hasEffect(ModEffects.NOMINE.get())){
            if (BlockFinder.NoBreak(event.getState()) && !(event.getState().getBlock() == ModBlocks.GUARDIAN_OBELISK.get())){
                new SoundUtil(event.getPos(), SoundEvents.ELDER_GUARDIAN_CURSE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                new ParticleUtil(ParticleTypes.HAPPY_VILLAGER, event.getPos(), event.getState());
            }
        }
    }

    @SubscribeEvent
    public static void onConversion(LivingConversionEvent.Post event){
        if (event.getOutcome() instanceof BeldamEntity){
            if (event.getOutcome().level.isClientSide){
                for (int i = 0; i < 5; ++i) {
                    double d0 = event.getOutcome().getRandom().nextGaussian() * 0.02D;
                    double d1 = event.getOutcome().getRandom().nextGaussian() * 0.02D;
                    double d2 = event.getOutcome().getRandom().nextGaussian() * 0.02D;
                    event.getOutcome().level.addParticle(ParticleTypes.HAPPY_VILLAGER, event.getOutcome().getRandomX(1.0D), event.getOutcome().getRandomY() + 1.0D, event.getOutcome().getRandomZ(1.0D), d0, d1, d2);
                }
                new SoundUtil(event.getOutcome().blockPosition(), SoundEvents.WITCH_CELEBRATE, SoundCategory.HOSTILE, 1.0F, 1.0F);
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

    @SubscribeEvent
    public static void HurtEvents(LivingHurtEvent event){
        LivingEntity victim = event.getEntityLiving();
        if (victim != null) {
            if (ModDamageSource.desiccateAttacks(event.getSource())) {
                if (!(victim instanceof IDeadMob)) {
                    new SoundUtil(victim.blockPosition(), SoundEvents.STONE_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    IParticleData particleData = new BlockParticleData(ParticleTypes.BLOCK, ModBlocks.DEAD_SANDSTONE.get().defaultBlockState());
                    new ParticleUtil(victim, particleData);
                }
            }
        }
    }
}
