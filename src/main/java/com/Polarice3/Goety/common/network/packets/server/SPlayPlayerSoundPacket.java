package com.Polarice3.Goety.common.network.packets.server;

import com.Polarice3.Goety.Goety;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class SPlayPlayerSoundPacket {
    private SoundEvent soundEvent;
    private float volume;
    private float pitch;

    public SPlayPlayerSoundPacket(SoundEvent soundEvent, float volume, float pitch){
        this.soundEvent = soundEvent;
        this.volume = volume;
        this.pitch = pitch;
    }

    public static void encode(SPlayPlayerSoundPacket packet, PacketBuffer buffer) {
        ResourceLocation resourceLocation = ForgeRegistries.SOUND_EVENTS.getKey(packet.soundEvent);
        if (resourceLocation != null){
            buffer.writeResourceLocation(resourceLocation);
        } else {
            buffer.writeResourceLocation(ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.ITEM_PICKUP));
        }
        buffer.writeFloat(packet.volume);
        buffer.writeFloat(packet.pitch);
    }

    public static SPlayPlayerSoundPacket decode(PacketBuffer buffer) {
        return new SPlayPlayerSoundPacket(ForgeRegistries.SOUND_EVENTS.getValue(buffer.readResourceLocation()), buffer.readFloat(), buffer.readFloat());
    }

    public static void consume(SPlayPlayerSoundPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = Goety.PROXY.getPlayer();
            if (player != null){
                player.playSound(packet.soundEvent, packet.volume, packet.pitch);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
