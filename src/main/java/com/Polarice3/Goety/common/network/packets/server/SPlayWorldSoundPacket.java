package com.Polarice3.Goety.common.network.packets.server;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class SPlayWorldSoundPacket {
    private BlockPos blockPos;
    private SoundEvent soundEvent;
    private float volume;
    private float pitch;

    public SPlayWorldSoundPacket(BlockPos blockPos, SoundEvent soundEvent, float volume, float pitch){
        this.blockPos = blockPos;
        this.soundEvent = soundEvent;
        this.volume = volume;
        this.pitch = pitch;
    }

    public static void encode(SPlayWorldSoundPacket packet, PacketBuffer buffer) {
        buffer.writeBlockPos(packet.blockPos);
        ResourceLocation resourceLocation = ForgeRegistries.SOUND_EVENTS.getKey(packet.soundEvent);
        if (resourceLocation != null){
            buffer.writeResourceLocation(resourceLocation);
        } else {
            buffer.writeResourceLocation(ForgeRegistries.SOUND_EVENTS.getKey(SoundEvents.ITEM_PICKUP));
        }
        buffer.writeFloat(packet.volume);
        buffer.writeFloat(packet.pitch);
    }

    public static SPlayWorldSoundPacket decode(PacketBuffer buffer) {
        return new SPlayWorldSoundPacket(buffer.readBlockPos(), ForgeRegistries.SOUND_EVENTS.getValue(buffer.readResourceLocation()), buffer.readFloat(), buffer.readFloat());
    }

    public static void consume(SPlayWorldSoundPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld clientWorld = Minecraft.getInstance().level;
            if (clientWorld != null){
                clientWorld.playLocalSound(packet.blockPos, packet.soundEvent, SoundCategory.NEUTRAL, packet.volume, packet.pitch, false);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
