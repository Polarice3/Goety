package com.Polarice3.Goety.common.network.packets.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.network.NetworkEvent;

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
        buffer.writeResourceLocation(packet.soundEvent.getLocation());
        buffer.writeFloat(packet.volume);
        buffer.writeFloat(packet.pitch);
    }

    public static SPlayPlayerSoundPacket decode(PacketBuffer buffer) {
        return new SPlayPlayerSoundPacket(new SoundEvent(buffer.readResourceLocation()), buffer.readFloat(), buffer.readFloat());
    }

    public static void consume(SPlayPlayerSoundPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null){
                player.playSound(packet.soundEvent, packet.volume, packet.pitch);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
